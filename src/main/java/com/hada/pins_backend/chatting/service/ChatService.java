package com.hada.pins_backend.chatting.service;

import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.dto.ChatMessageDto;
import com.hada.pins_backend.chatting.model.enumerate.MessageClass;

import java.util.List;

/*
 * Created by parksuho on 2022/03/26.
 */
public interface ChatService {
    Long numOfSubscribers(MessageClass messageClass, Long pinId);
    List<ChatMessage> loadMessages(Long userId, MessageClass messageClass, Long pinId);
    void publishMessage(Long senderId, MessageClass messageClass, Long pinId, ChatMessageDto chatMessageDto);
}
