package com.lxw.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.LostmanInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lxw
 * @since 2021-03-14
 */
public interface LostmanInfoMapper extends BaseMapper<LostmanInfo> {

    IPage<LostmanInfo> findLostmanPage(Page<LostmanInfo> page,@Param(Constants.WRAPPER) QueryWrapper<LostmanInfo> wrapper);

}
