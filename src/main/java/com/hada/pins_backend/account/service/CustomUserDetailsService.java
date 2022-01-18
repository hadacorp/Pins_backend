package com.hada.pins_backend.account.service;

import com.hada.pins_backend.account.exception.CUserNotFoundException;
import com.hada.pins_backend.account.model.entity.UserPrincipal;
import com.hada.pins_backend.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Created by parksuho on 2022/01/19.
 */
@RequiredArgsConstructor
@Service
// 토큰에 세팅된 유저 정보로 회원정보 조회하는 인터페이스(UserDetailService) 재정의
// DB에서 유저 정보 직접 가져오는 인터페이스 구현
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNum) throws UsernameNotFoundException {
        return new UserPrincipal(userRepository.findByPhoneNum(phoneNum).orElseThrow(CUserNotFoundException::new));
    }
}
