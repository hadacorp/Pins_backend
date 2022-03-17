package com.hada.pins_backend.chatting.controller;

import com.hada.pins_backend.advice.ValidationSequence;
import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/*
 * Created by parksuho on 2022/03/15.
 * Modified by parksuho on 2022/03/17.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/kafka/chat/{pin_type}")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@PathVariable("pin_type") String topic, @RequestBody @Validated(ValidationSequence.class) ChatMessage message) {
        message.setTimeStamp(LocalDateTime.now().toString());
        if(ChatMessage.MessageType.ENTER.equals(message.getType())) message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        try {
            kafkaProducerService.send(topic, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
