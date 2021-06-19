package com.lxw.system.service.impl;

import com.lxw.entity.Menu;
import com.lxw.entity.User;
import com.lxw.system.mapper.MenuMapper;
import com.lxw.system.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxw.system.service.RoleMenuService;
import com.lxw.system.service.UserRoleService;
import com.lxw.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 获取菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenuList(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        //获得当前用户对应的所有角色的id
        List<Long> roleIds = userRoleService.getRoleIdsByUid(user.getId());
        // 查询出这些角色对应的所有菜单项
        List<Long> menuIds = roleMenuService.getMenuIdsByRid(roleIds);
        //根据菜单ID查询所有菜单对象
        List<Menu> menuList = menuMapper.selectBatchIds(menuIds);
        //处理菜单项
        handleMenus(menuList);
        return menuList;
    }


    /**
     * 处理菜单项的结构
     * @param menuList
     */
    public void handleMenus(List<Menu> menuList) {
        //查找菜单子节点并加入children集合
        for (Menu menu : menuList) {
            List<Menu> children = menuMapper.getAllChildById(menu.getId());
            menu.setChildren(children);
        }
        //迭代菜单 只保留一级菜单项
        Iterator<Menu> iterator = menuList.iterator();
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            if (menu.getParentId() != 0) {
                iterator.remove();
            }
        }

    }


}
