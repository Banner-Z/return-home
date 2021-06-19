package com.lxw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.dto.TaskVolunteerDTO;
import com.lxw.entity.VolunteerPos;
import com.lxw.system.mapper.VolunteerMapper;
import com.lxw.system.mapper.VolunteerPosMapper;
import com.lxw.system.service.VolunteerService;
import com.lxw.entity.Volunteer;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxw
 * @since 2021-03-08
 */
@Service
public class VolunteerServiceImpl extends ServiceImpl<VolunteerMapper, Volunteer> implements VolunteerService {

    @Autowired
    private VolunteerMapper volunteerMapper;

    @Autowired
    private VolunteerPosMapper volunteerPosMapper;

    /**
     * 添加志愿者
     * @param volunteer
     * @return
     */
    @Override
    public int addVolunteer(Volunteer volunteer){
        return volunteerMapper.insert(volunteer);
    }

    /**
     * 查询某个志愿者
     * @param wrapper
     * @return
     */
    @Override
    public Volunteer findOneVolunteer(QueryWrapper<Volunteer> wrapper) {
        return volunteerMapper.selectOne(wrapper);
    }


    /**
     * 根据条件分页查询志愿者
     */
    @Override
    public IPage<Volunteer> findVolunteerPage(Page<Volunteer> page, QueryWrapper<Volunteer> wrapper) {
        return volunteerMapper.findVolunteerPage(page,wrapper);
    }

    /**
     * 根据Id封装任务中的志愿者
     */
    @Override
    public TaskVolunteerDTO findTaskVolunteerById(Long volunteerId) {
        TaskVolunteerDTO dto = new TaskVolunteerDTO();
        //志愿者实体对象
        Volunteer volunteer = volunteerMapper.selectById(volunteerId);
        BeanUtils.copyProperties(volunteer,dto);
        //志愿者-位置实体对象
        VolunteerPos volunteerPos = volunteerPosMapper.selectById(volunteerId);
        if(volunteerPos != null){
            BeanUtils.copyProperties(volunteerPos,dto);
        }
        return dto;
    }


    /**
     * 根据ID查找志愿者名字  ---给线索用
     * @param id
     * @return
     */
    public String findVolNameById(Long id){
        Volunteer volunteer = volunteerMapper.selectById(id);
        return volunteer.getVolunteerName();
    }


}
