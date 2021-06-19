package com.lxw.converter;

import com.lxw.dto.VolunteerClueTaskDTO;
import com.lxw.entity.*;
import com.lxw.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 志愿者端转换器
 */
public class VolunteerConverter {


    /**
     * 志愿者信息转vo
     * @param volunteer
     * @return
     */
    public static VolunteerInfoVO converterToVolunteerVO(Volunteer volunteer) {
        VolunteerInfoVO volunteerInfoVO = new VolunteerInfoVO();
        BeanUtils.copyProperties(volunteer,volunteerInfoVO);
        return volunteerInfoVO;
    }


    /**
     * 志愿者信息转voList
     * @param volunteers
     * @return
     */
    public static List<VolunteerInfoVO> converterToVolunteerInfoVOList(List<Volunteer> volunteers){
        List<VolunteerInfoVO> volunteerInfoVOS = new ArrayList<>();
        VolunteerInfoVO volunteerInfoVO;
        if(!CollectionUtils.isEmpty(volunteers)){
            for (Volunteer volunteer : volunteers) {
                volunteerInfoVO = converterToVolunteerVO(volunteer);
                volunteerInfoVOS.add(volunteerInfoVO);
            }
        }
        return volunteerInfoVOS;
    }


    /**
     * 志愿者任务列表展示VO
     */
    public static VolunteerClueTaskVO converterToVolunteerTaskListVO(Volunteer volunteer, List<VolunteerClueTaskDTO> dtos){
        VolunteerClueTaskVO vo = new VolunteerClueTaskVO();
        BeanUtils.copyProperties(volunteer,vo);
        vo.setTaskList(dtos);
        return vo;
    }


    /**
     * 志愿者任务详情展示VO
     */
    public static VolunteerTaskDetailsVO converterToVolunteerTaskDetailsVO(LostmanInfo lostman, Family family,Task task) {
        VolunteerTaskDetailsVO vo = new VolunteerTaskDetailsVO();
        BeanUtils.copyProperties(lostman,vo);
        BeanUtils.copyProperties(family,vo);
        BeanUtils.copyProperties(task,vo);
        return vo;
    }

    /**
     * 某个任务下的志愿者简单信息展示VOList
     */
    public static List<VolunteerEasyInfoVO> converterToVolunteerEasyInfoVOList(List<Volunteer> volunteerList) {
        List<VolunteerEasyInfoVO> vos = new ArrayList<>();
        VolunteerEasyInfoVO vo;
        for (Volunteer volunteer : volunteerList) {
            vo = new VolunteerEasyInfoVO();
            BeanUtils.copyProperties(volunteer,vo);
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 查看任务列表 (当前任务和历史任务）VO
     */
    public static VolunteerCheckTaskVO converterToVolunteerCheckTaskVO(Task task,LostmanInfo lostman,LostmanImg img){
        VolunteerCheckTaskVO vo = new VolunteerCheckTaskVO();
        BeanUtils.copyProperties(task,vo);
        BeanUtils.copyProperties(lostman,vo);
        //对照片处理下
        ImgVO imgVO = new ImgVO();
        if(img != null){
            BeanUtils.copyProperties(img,imgVO);
        }
        vo.setImg(imgVO);
        return vo;
    }


    /**
     * 查看某个历史任务的详细信息VO
     */
    public static PastTaskDetailsVO converterToPastTaskDetailsVO(Task task,LostmanInfo lostman,Family family){
        PastTaskDetailsVO vo = new PastTaskDetailsVO();
        BeanUtils.copyProperties(task,vo);
        BeanUtils.copyProperties(lostman,vo);
        BeanUtils.copyProperties(family,vo);
        return vo;
    }



}
