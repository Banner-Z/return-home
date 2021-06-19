package com.lxw.system.mapper;

import com.lxw.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxw
 * @since 2021-03-11
 */
public interface MenuMapper extends BaseMapper<Menu> {

    //根据菜单栏id查找到所有的子节点
    List<Menu> getAllChildById(Long parentId);

}
