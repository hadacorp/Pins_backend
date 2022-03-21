package com.hada.pins_backend.chatting.repository;

import com.hada.pins_backend.chatting.model.ChatRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

/*
 * Created by parksuho on 2022/03/15.
 */
@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    // 모든 채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        // 채팅방 최근 생성 순
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    // 채팅방 하나 불러오기
    public ChatRoom findByRoomId(String roomId) {
        return chatRoomMap.get(roomId);
    }

    // 채팅방 생성
    public ChatRoom createChatRoom(String roomName) {
        ChatRoom chatRoom = ChatRoom.create(roomName);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
