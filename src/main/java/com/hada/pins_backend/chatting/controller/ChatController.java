package com.hada.pins_backend.chatting.controller;

import com.hada.pins_backend.advice.ValidationSequence;
import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.service.RedisPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/*
 * Created by parksuho on 2022/03/15.
 * Modified by parksuho on 2022/03/25.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final RedisPubService redisPubService;

    @MessageMapping("/chat/meeting")
    @ResponseStatus(HttpStatus.OK)
    public void publishMeeting(@RequestBody @Validated(ValidationSequence.class) ChatMessage message) {
        message.setTimeStamp(LocalDateTime.now().toString());
        if(ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        else if(ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }
        redisPubService.publish("meeting", message);
    }

    @MessageMapping("/chat/community")
    @ResponseStatus(HttpStatus.OK)
    public void publishCommunity(@RequestBody @Validated(ValidationSequence.class) ChatMessage message) {
        message.setTimeStamp(LocalDateTime.now().toString());
        if(ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        else if(ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }
        redisPubService.publish("community", message);
    }
}
