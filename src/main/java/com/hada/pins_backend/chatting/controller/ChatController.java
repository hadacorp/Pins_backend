package com.hada.pins_backend.chatting.controller;

import com.hada.pins_backend.account.model.entity.CurrentUser;
import com.hada.pins_backend.account.model.entity.User;
import com.hada.pins_backend.advice.ValidationSequence;
import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.dto.ChatMessageDto;
import com.hada.pins_backend.chatting.model.enumerate.MessageClass;
import com.hada.pins_backend.chatting.service.ChatService;
import com.hada.pins_backend.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Created by parksuho on 2022/03/15.
 * Modified by parksuho on 2022/03/25.
 * Modified by parksuho on 2022/03/26.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/chat/{messageClass}/{pinId}/num")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Long> numOfSubscribers(@PathVariable MessageClass messageClass, @PathVariable Long pinId) {
        return new ApiResponse<>(chatService.numOfSubscribers(messageClass, pinId));
    }

    @GetMapping("/chat/{messageClass}/{pinId}/messages")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ChatMessage>> loadMessages(
            @CurrentUser User user, @PathVariable MessageClass messageClass, @PathVariable Long pinId) {
        return new ApiResponse<>(chatService.loadMessages(user.getId(), messageClass, pinId));
    }

    @MessageMapping("/chat/{messageClass}/{pinId}")
    @ResponseStatus(HttpStatus.OK)
    public void publishMessage(
            @CurrentUser User user, @PathVariable MessageClass messageClass,
            @PathVariable Long pinId, @RequestBody @Validated(ValidationSequence.class) ChatMessageDto chatMessageDto) {
        chatService.publishMessage(user.getId(), messageClass, pinId, chatMessageDto);
    }
}
