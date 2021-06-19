package com.lxw.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.entity.RoleMenu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxw
 * @since 2021-03-11
 */
public interface RoleMenuService extends IService<RoleMenu> {

    //根据角色id获取对应的菜单栏
    List<Long> getMenuIdsByRid(List<Long> roleIds);

}
