package com.lxw.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.dto.TaskVolunteerDTO;
import com.lxw.entity.Volunteer;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lxw
 * @since 2021-03-08
 */
public interface VolunteerService extends IService<Volunteer> {

    //添加志愿者
    int addVolunteer(Volunteer volunteer);

    //查询某个志愿者
    Volunteer findOneVolunteer(QueryWrapper<Volunteer> wrapper);

    //根据条件分页查找志愿者
    IPage<Volunteer> findVolunteerPage(Page<Volunteer> page, QueryWrapper<Volunteer> wrapper);

    //根据ID封装任务中的志愿者信息
    TaskVolunteerDTO findTaskVolunteerById(Long volunteerId);

    //根据ID查找志愿者姓名
    String findVolNameById(Long id);

}
