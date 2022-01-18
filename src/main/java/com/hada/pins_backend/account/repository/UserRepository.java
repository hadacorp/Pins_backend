package com.hada.pins_backend.account.repository;

import com.hada.pins_backend.account.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho in 2022/01/18.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByPhoneNum(String phonenum);
    Optional<User> findByNickName(String nickname);
}
