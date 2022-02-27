package com.hada.pins_backend.account.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/02/27.
 */
public class UserPrincipal extends org.springframework.security.core.userdetails.User {
    private User user;

    public UserPrincipal(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
    }

    public UserPrincipal(User user) {
        super(user.getPhoneNum(), "", List.of(new SimpleGrantedAuthority(user.getRole())));
        this.user = user;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(List<String> role) {
        return role.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return user.getPhoneNum();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() { // 계정 만료 여부
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() { // 계정 잠금 여부
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() { // 계정 패스워드 만료 여부
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() { // 계정 사용가능 여부
        return true;
    }

    public User getUser() {
        return user;
    }
}
