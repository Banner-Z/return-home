package com.lxw.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.Volunteer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxw
 * @since 2021-03-08
 */
public interface VolunteerMapper extends BaseMapper<Volunteer> {

    //根据条件分页查询志愿者
    IPage<Volunteer> findVolunteerPage(Page<Volunteer> page,@Param(Constants.WRAPPER) QueryWrapper<Volunteer> wrapper);


}
