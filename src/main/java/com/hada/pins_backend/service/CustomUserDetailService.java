package com.hada.pins_backend.service;

import com.hada.pins_backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by bangjinhyuk on 2021/08/12.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phonenum) throws UsernameNotFoundException {
        return userRepository.findByPhoneNum(phonenum)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));    }
}
