package com.lxw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.LostmanInfo;
import com.lxw.system.mapper.LostmanInfoMapper;
import com.lxw.system.service.LostmanInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxw
 * @since 2021-03-14
 */
@Service
public class LostmanInfoServiceImpl extends ServiceImpl<LostmanInfoMapper, LostmanInfo> implements LostmanInfoService {

    @Autowired
    private LostmanInfoMapper lostmanInfoMapper;


    /**
     * 根据条件分页查询志愿者
     */
    @Override
    public IPage<LostmanInfo> findLostmanPage(Page<LostmanInfo> page, QueryWrapper<LostmanInfo> wrapper) {
        return lostmanInfoMapper.findLostmanPage(page,wrapper);
    }


}
