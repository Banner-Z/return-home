package com.lxw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxw.entity.JwtUser;
import com.lxw.entity.Role;
import com.lxw.entity.User;
import com.lxw.system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 供权限框架调用
 */
@Service(value = "userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null||"".equals(username)){
            throw new RuntimeException("账号不能为空");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = userMapper.selectOne(wrapper);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        // 设置权限
        Set authoritiesSet = new HashSet();
        //取出用户权限
        List<Role> roleList = userMapper.getRolesByUserId(user.getId());
        System.out.println(roleList);
        // 注意角色权限需要加 ROLE_前缀，否则报403
        roleList.forEach(role -> {
            String roleStr = role.getRoleName();
            authoritiesSet.add(new SimpleGrantedAuthority("ROLE_"+roleStr));
        });
//        GrantedAuthority userPower = new SimpleGrantedAuthority("ROLE_USER");
//        GrantedAuthority adminPower = new SimpleGrantedAuthority("ROLE_ADMIN");
//        authoritiesSet.add(userPower);
//        authoritiesSet.add(adminPower);
        return new JwtUser(user.getUsername(),user.getPassword(),user.getVolunteerId(),authoritiesSet);

    }

}
