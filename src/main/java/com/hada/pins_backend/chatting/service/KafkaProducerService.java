package com.hada.pins_backend.chatting.service;

import com.hada.pins_backend.chatting.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/*
 * Created by parksuho on 2022/03/17.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public void send(String topic, ChatMessage message) {
        log.info("sending data='{}' to topic='{}' pinId='{}'", message.getMessage(), topic, message.getPinId());
        kafkaTemplate.send(topic, message);
    }
}
