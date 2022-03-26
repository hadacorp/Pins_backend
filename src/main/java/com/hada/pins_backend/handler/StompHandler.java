package com.hada.pins_backend.handler;

import com.hada.pins_backend.chatting.repository.CommunityMessageRepository;
import com.hada.pins_backend.chatting.repository.MeetingMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*
 * Created by parksuho on 2022/03/26.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    @Qualifier("subRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private final MeetingMessageRepository meetingMessageRepository;
    private final CommunityMessageRepository communityMessageRepository;
    private static final String KEY = "PINS:";


    private String getPinId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        SetOperations<String, String> subscribers = redisTemplate.opsForSet();
        log.info("StompCommand : " + accessor.getCommand());
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("[CONNECT]");
        }  else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String pinId = getPinId(Optional.ofNullable((String) message.getHeaders().get("destination")).orElse("InvalidPinId"));
            String senderId = Optional.ofNullable((String) message.getHeaders().get("senderId")).orElse("InvalidSenderId");
            if (!subscribers.isMember(KEY + pinId, Long.valueOf(senderId))) {
                subscribers.add(KEY + pinId, senderId);
                log.info("[SUBSCRIBE] pin Id : {}, sender Id : {}", pinId, senderId);
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String pinId = getPinId(Optional.ofNullable((String) message.getHeaders().get("destination")).orElse("InvalidPinId"));
            String senderId = Optional.ofNullable((String) message.getHeaders().get("senderId")).orElse("InvalidSenderId");
            if (subscribers.isMember(KEY + pinId, Long.valueOf(senderId))) {
                subscribers.remove(KEY + pinId, senderId);
                log.info("[DISCONNECT] pin Id : {}, sender Id : {}", pinId, senderId);
            }
            if (subscribers.size(KEY + pinId) == 0) {
                log.info("There is no subscriber in chatting");
                meetingMessageRepository.deleteAllByPinIdInBatch(Long.valueOf(pinId));
                communityMessageRepository.deleteAllByPinIdInBatch(Long.valueOf(pinId));
                log.info("Delete all messages");
            }
        }
        return message;
    }
}
