package com.hada.pins_backend.chatting.service;

import com.hada.pins_backend.account.exception.CUserNotFoundException;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.account.repository.UserRepository;
import com.hada.pins_backend.chatting.exception.CUserNotSubscribeException;
import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.CommunityMessage;
import com.hada.pins_backend.chatting.model.MeetingMessage;
import com.hada.pins_backend.chatting.model.dto.ChatMessageDto;
import com.hada.pins_backend.chatting.model.enumerate.MessageClass;
import com.hada.pins_backend.chatting.model.enumerate.MessageType;
import com.hada.pins_backend.chatting.repository.CommunityMessageRepository;
import com.hada.pins_backend.chatting.repository.MeetingMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/*
 * Created by parksuho on 2022/03/26.
 * Modified by parksuho on 2022/03/27.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Qualifier("subRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisPubService redisPubService;
    private final MeetingMessageRepository meetingMessageRepository;
    private final CommunityMessageRepository communityMessageRepository;
    private final UserRepository userRepository;

    private static final String KEY = "PINS:";

    @Transactional(readOnly = true)
    public Long numOfSubscribers(MessageClass messageClass, Long pinId) {
        return redisTemplate.opsForSet().size(KEY + pinId);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> loadMessages(Long userId, MessageClass messageClass, Long pinId) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(KEY + pinId, userId))) throw new CUserNotSubscribeException();
        if (messageClass.equals(MessageClass.meeting)) {
            return meetingMessageRepository.findAllByPinIdOrderByTimeStampDesc(pinId);
        } else if (messageClass.equals(MessageClass.community)) {
            return communityMessageRepository.findAllByPinIdOrderByTimeStampDesc(pinId);
        } else return Collections.emptyList();
    }

    @Transactional
    public void publishMessage(Long senderId, MessageClass messageClass, Long pinId, ChatMessageDto dto) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(KEY + pinId, senderId))) throw new CUserNotSubscribeException();
        User user = userRepository.findById(senderId).orElseThrow(CUserNotFoundException::new);

        String timeStamp = LocalDateTime.now().toString();
        String senderName = user.getName();
        String message;
        if(dto.getType().equals(MessageType.ENTER)) {
            message = user.getName() + "?????? ?????????????????????.";
        } else if(dto.getType().equals(MessageType.QUIT)) {
            message = user.getName() + "?????? ?????????????????????.";
        } else message = dto.getMessage();

        if (messageClass.equals(MessageClass.meeting)) {
            MeetingMessage meetingMessage = MeetingMessage.builder()
                    .type(dto.getType()).pinId(pinId).senderId(senderId)
                    .senderName(senderName).message(message).timeStamp(timeStamp).build();
            redisPubService.publish(MessageClass.meeting, meetingMessage);
            meetingMessageRepository.save(meetingMessage);
        } else if (messageClass.equals(MessageClass.community)) {
            CommunityMessage communityMessage = CommunityMessage.builder()
                    .type(dto.getType()).pinId(pinId).senderId(senderId)
                    .senderName(senderName).message(message).timeStamp(timeStamp).build();
            redisPubService.publish(MessageClass.community, communityMessage);
            communityMessageRepository.save(communityMessage);
        }
    }

    @Transactional(readOnly = true)
    public void testRedis() {
        log.info(redisTemplate.opsForValue().get("PING"));
    }
}
