package com.hada.pins_backend.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bangjinhyuk on 2021/12/10.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
