package com.lxw.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.Role;
import com.lxw.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxw
 * @since 2021-03-06
 */
public interface UserMapper extends BaseMapper<User> {

    //查询所有用户
    List<User> findAll();

    //根据用户id 查询对应的角色
    List<Role> getRolesByUserId(Long userId);

    IPage<User> getUserPage(Page<User> page,@Param(Constants.WRAPPER) QueryWrapper<User> wrapper);

}
