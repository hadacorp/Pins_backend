package com.hada.pins_backend.chatting.model;

import com.hada.pins_backend.advice.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * Created by parksuho on 2022/03/15.
 * Modified by parksuho on 2022/03/17.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    public enum MessageType {
        ENTER, QUIT, TALK
    }
    @NotNull(message = "메시지 타입은 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private MessageType type;
    @NotBlank(message = "핀 번호는 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private String pinId;
    @NotBlank(message = "발신자는 필수 값입니다.", groups = ValidationGroups.NotEmptyGroup.class)
    private String sender;
    private String message;
    private String timeStamp;
    private String rawData;
    private String fileName;
}
