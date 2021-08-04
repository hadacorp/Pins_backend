package com.hada.pins_backend.domain.user;

import com.hada.pins_backend.domain.BaseTimeEntity;
import com.hada.pins_backend.domain.Gender;
import lombok.*;

import javax.persistence.*;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper=false)
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickName;

    private int resRedNumber;

    private String phoneNum;

    private int age;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String image;








}
