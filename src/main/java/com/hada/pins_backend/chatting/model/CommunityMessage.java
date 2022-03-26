package com.hada.pins_backend.chatting.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/*
 * Created by parksuho on 2022/03/26.
 */
@Entity
@SuperBuilder
@NoArgsConstructor
public class CommunityMessage extends ChatMessage{
}
