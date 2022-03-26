package com.hada.pins_backend.chatting.service;

import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.enumerate.MessageClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/*
 * Created by parksuho on 2022/03/25.
 * Modified by parksuho on 2022/03/26.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubService {
    private final RedisTemplate<String, ChatMessage> chatMessageRedisTemplate;

    public void publish(MessageClass messageClass, ChatMessage message) {
        String topic = String.valueOf(messageClass);
        log.info("[sendMessage/{}] sender ID : {}, pin ID : {}, message: {}",
                topic, message.getSenderId(), message.getPinId(), message.getMessage());
        chatMessageRedisTemplate.convertAndSend(topic, message);
    }
}
