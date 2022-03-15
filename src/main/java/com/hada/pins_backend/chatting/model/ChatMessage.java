package com.hada.pins_backend.chatting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Created by parksuho on 2022/03/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    public enum MessageType {
        ENTER, QUIT, TALK
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
