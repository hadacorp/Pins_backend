package com.hada.pins_backend.chatting.repository;

import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.model.MeetingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Created by parksuho on 2022/03/26.
 */
@Repository
public interface MeetingMessageRepository extends JpaRepository<MeetingMessage, Long> {
    List<ChatMessage> findAllByPinIdOrderByTimeStampDesc(Long pinId);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM MeetingMessage mm WHERE mm.pinId = :pinId")
    void deleteAllByPinIdInBatch(Long pinId);
}
