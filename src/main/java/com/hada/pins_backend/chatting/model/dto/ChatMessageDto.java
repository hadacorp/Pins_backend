package com.hada.pins_backend.chatting.model.dto;

import com.hada.pins_backend.advice.ValidationGroups;
import com.hada.pins_backend.chatting.model.enumerate.MessageType;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/*
 * Created by parksuho on 2022/03/26.
 */
@Getter
public class ChatMessageDto {
    @NotNull(message = "메시지 타입은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private MessageType type;
    private String message;
}
