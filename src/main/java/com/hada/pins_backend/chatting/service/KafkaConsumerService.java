package com.hada.pins_backend.chatting.service;

import com.hada.pins_backend.chatting.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/*
 * Created by parksuho on 2022/03/17.
 */
@Slf4j
@Service
public class KafkaConsumerService {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = "meeting", groupId = "meeting-listener", containerFactory = "meetingContainerFactory")  // 카프카로부터 메시지 받아옴
    public void meetingListen(ChatMessage message, @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment ack) {
        log.info("sending via kafka listener..");
        try {
            log.info("receive data='{}' from topic='{}' pinId='{}'", message.getMessage(), "meeting", message.getPinId());
            template.convertAndSend("/meeting/" + message.getPinId(), message);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("error sending message : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "community", groupId = "community-listener", containerFactory = "communityContainerFactory")
    public void communityListen(ChatMessage message, @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment ack) {
        log.info("sending via kafka listener..");
        try {
            log.info("receive data='{}' from topic='{}' pinId='{}'", message.getMessage(), "community", message.getPinId());
            template.convertAndSend("/community/"+message.getPinId(), message);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("error sending message : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}