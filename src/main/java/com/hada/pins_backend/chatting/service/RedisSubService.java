package com.hada.pins_backend.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.CommunityMessage;
import com.hada.pins_backend.chatting.model.MeetingMessage;
import com.hada.pins_backend.chatting.model.enumerate.MessageClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/*
 * Created by parksuho on 2022/03/25.
 * Modified by parksuho on 2022/03/26.
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
            if(topic.equals(String.valueOf(MessageClass.meeting))) {
                MeetingMessage chatMessage = objectMapper.readValue(message.getBody(), MeetingMessage.class);
                log.info("[onMessage/meeting] sender ID : {}, pin ID : {}, message: {}",
                        chatMessage.getSenderId(), chatMessage.getPinId(), chatMessage.getMessage());
                messagingTemplate.convertAndSend("/sub/" + topic + "/" + chatMessage.getPinId(), chatMessage);
            } else if (topic.equals(String.valueOf(MessageClass.community))) {
                CommunityMessage chatMessage = objectMapper.readValue(message.getBody(), CommunityMessage.class);
                log.info("[onMessage/community] sender ID : {}, pin ID : {}, message: {}",
                        chatMessage.getSenderId(), chatMessage.getPinId(), chatMessage.getMessage());
                messagingTemplate.convertAndSend("/sub/" + topic + "/" + chatMessage.getPinId(), chatMessage);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}