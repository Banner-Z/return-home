package com.lxw.system.service;

import com.lxw.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {

    //根据用户id获取对应角色的id集合
    List<Long> getRoleIdsByUid(Long userId);

}
