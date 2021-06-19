package com.lxw.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.entity.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxw
 * @since 2021-03-11
 */
public interface MenuService extends IService<Menu> {

    //获取菜单栏
    public List<Menu> getMenuList();

//    //根据菜单栏的id集合 拿到菜单栏对象集合
//    public List<Menu> getMenuListByIds(List<Long> menuIds);

}
