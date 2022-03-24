package com.hada.pins_backend.chatting.service;

import com.hada.pins_backend.chatting.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/*
 * Created by parksuho on 2022/03/25.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubService {
    private final RedisTemplate<String, ChatMessage> chatMessageRedisTemplate;

    public void publish(String topic, ChatMessage message) {
        log.info("[sendMessage] topic: {}, pin ID : {}, message: {}", topic, message.getPinId(), message.getMessage());
        chatMessageRedisTemplate.convertAndSend(topic, message);
    }
}
