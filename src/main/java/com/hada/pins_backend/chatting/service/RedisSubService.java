package com.hada.pins_backend.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.chatting.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/*
 * Created by parksuho on 2022/03/25.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubService implements MessageListener {
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            String topic = new String(message.getChannel());
            ChatMessage chatMessage = objectMapper.readValue(message.getBody(), ChatMessage.class);
            log.info("[onMessage] topic: {}, pin ID : {}, message: {}", topic, chatMessage.getPinId(), chatMessage.getMessage());
            messagingTemplate.convertAndSend("/sub/" + topic + "/" + chatMessage.getPinId(), chatMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}