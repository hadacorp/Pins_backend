package com.hada.pins_backend.account.repository;

import com.hada.pins_backend.account.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by bangjinhyuk on 2022/01/15.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByPhoneNum(String phonenum);
    Optional<User> findByNickName(String nickname);
}
