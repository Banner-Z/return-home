package com.lxw.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * 实现UserDetails类型接口  防止返回的是默认User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser implements UserDetails {

    // 用户名
    private String username;
    // 密码
    private String password;
    //志愿者ID 为0说明是指挥系统
    private Long vounteerId;

    // 权限信息
    private Set<? extends GrantedAuthority> authorities;

    // 账号是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号是否未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 账号凭证是否未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //账号是否可以使用
    @Override
    public boolean isEnabled() {
        return true;
    }

}
