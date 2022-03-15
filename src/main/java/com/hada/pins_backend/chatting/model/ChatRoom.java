package com.hada.pins_backend.chatting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/*
 * Created by parksuho on 2022/03/15.
 */
@Getter
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private String roomName;

    public static ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }
}
