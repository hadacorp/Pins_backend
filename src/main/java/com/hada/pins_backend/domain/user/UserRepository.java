package com.hada.pins_backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByPhoneNum(String phonenum);
}
