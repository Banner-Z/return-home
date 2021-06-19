package com.lxw.system.service.impl;

import com.lxw.entity.UserRole;
import com.lxw.system.mapper.UserRoleMapper;
import com.lxw.system.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxw
 * @since 2021-03-06
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 根据用户id获取对应角色的id集合
     */
    @Override
    public List<Long> getRoleIdsByUid(Long userId) {
        //方法一：根据columnMap查询
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("user_id",userId);
        List<UserRole> userRoleList = userRoleMapper.selectByMap(columnMap);
        List<Long> roleList = new ArrayList<>();
        userRoleList.forEach(userRole -> roleList.add(userRole.getRoleId()));
        return roleList;
    }



}
