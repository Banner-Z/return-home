package com.lxw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.User;
import com.lxw.system.mapper.UserMapper;
import com.lxw.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxw
 * @since 2021-03-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    //通过用户姓名查找用户
    @Override
    public User findByUsername(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        //数据库里查询的对象封装为tb_user
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public IPage<User> getUserPage(Page<User> page, QueryWrapper<User> wrapper) {
        return userMapper.getUserPage(page,wrapper);
    }


}
