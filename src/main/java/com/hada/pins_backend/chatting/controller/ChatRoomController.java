package com.hada.pins_backend.chatting.controller;

import com.hada.pins_backend.chatting.model.ChatRoom;
import com.hada.pins_backend.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Created by parksuho on 2022/03/15.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomRepository chatRoomRepository;

    // 채팅리스트 화면
    @GetMapping("/room")
    public String room(Model model) {
        return "/chat/room";
    }

    // 모든 채팅방 목록 화면
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> rooms() {
        return chatRoomRepository.findAllRoom();
    }

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String roomName) {
        return chatRoomRepository.createChatRoom(roomName);
    }

    // 채팅방 입장
    @GetMapping("/room/enter/{roomId}")
    public String roomEnter(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }
}
