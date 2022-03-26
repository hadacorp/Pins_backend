package com.hada.pins_backend.chatting.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hada.pins_backend.advice.ValidationGroups;
import com.hada.pins_backend.chatting.model.enumerate.MessageType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * Created by parksuho on 2022/03/15.
 * Modified by parksuho on 2022/03/25.
 * Modified by parksuho on 2022/03/26.
 */
@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private MessageType type;
    @Column(nullable = false)
    private Long pinId;
    @Column(nullable = false)
    private Long senderId;
    @Column(nullable = false)
    private String senderName;

    private String message;
    private String timeStamp;
}
