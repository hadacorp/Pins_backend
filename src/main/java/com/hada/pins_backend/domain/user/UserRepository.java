package com.hada.pins_backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
public interface UserRepository extends JpaRepository<User,Long>{
}
