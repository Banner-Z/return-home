package com.lxw.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxw
 * @since 2021-03-06
 */
public interface UserService extends IService<User> {

//    //管理员登入
//    Boolean login(User user);

    //通过姓名查找用户
    User findByUsername(String username);

    IPage<User> getUserPage(Page<User> page, QueryWrapper<User> wrapper);
}
