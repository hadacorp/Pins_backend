package com.hada.pins_backend.chatting.controller;

import com.hada.pins_backend.chatting.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

/*
 * Created by parksuho on 2022/03/15.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if(ChatMessage.MessageType.ENTER.equals(message.getType()))
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
