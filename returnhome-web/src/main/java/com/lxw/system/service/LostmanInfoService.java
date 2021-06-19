package com.lxw.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.LostmanInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxw
 * @since 2021-03-14
 */
public interface LostmanInfoService extends IService<LostmanInfo> {

    IPage<LostmanInfo> findLostmanPage(Page<LostmanInfo> page, QueryWrapper<LostmanInfo> wrapper);

}
