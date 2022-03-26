package com.hada.pins_backend.chatting.repository;

import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.MeetingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Created by parksuho on 2022/03/26.
 */
@Repository
public interface MeetingMessageRepository extends JpaRepository<MeetingMessage, Long> {
    List<ChatMessage> findAllByPinIdOrderByTimeStampDesc(Long pinId);
    void deleteAllByPinIdInBatch(Long pinId);
}
