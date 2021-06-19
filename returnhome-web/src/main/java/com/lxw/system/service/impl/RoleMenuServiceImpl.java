package com.lxw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxw.entity.RoleMenu;
import com.lxw.entity.UserRole;
import com.lxw.system.mapper.RoleMenuMapper;
import com.lxw.system.mapper.UserRoleMapper;
import com.lxw.system.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxw
 * @since 2021-03-11
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 根据角色id获取对应的菜单栏
     * @param roleIds
     * @return
     */
    @Override
    public List<Long> getMenuIdsByRid(List<Long> roleIds) {
        List<Long> menuList = new ArrayList<>();
        roleIds.forEach(roleId -> {
            QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper();
            queryWrapper.select("menu_id").eq("role_id",roleId);
            List<RoleMenu> roleMenuList = roleMenuMapper.selectList(queryWrapper);
            roleMenuList.forEach(roleMenu -> menuList.add(roleMenu.getMenuId()));
        });
        return menuList;
    }

}
