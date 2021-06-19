package com.lxw.converter;

import com.lxw.dto.TaskVolunteerDTO;
import com.lxw.entity.*;
import com.lxw.vo.TaskClueVO;
import com.lxw.vo.TaskListVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务转换器
 */
public class TaskConverter {

    /**
     * 封装任务视图对象 (转VO)
     * @param task
     * @param lostman
     * @param taskVolunteerDTOS
     * @return
     */
    public static TaskListVO converterToTaskVO(Task task, LostmanInfo lostman,
                                               List<TaskVolunteerDTO> taskVolunteerDTOS, Family family,
                                               LostmanImg img) {
        TaskListVO taskListVO = new TaskListVO();
        BeanUtils.copyProperties(task, taskListVO);
        BeanUtils.copyProperties(lostman, taskListVO);
        BeanUtils.copyProperties(family,taskListVO);
        taskListVO.setVolunteerList(taskVolunteerDTOS);
        if(img != null){
            taskListVO.setImgUrl(img.getPicUrl());
        }
        return taskListVO;
    }


    /**
     * 转线索VO
     * @param taskClue
     * @return
     */
    public static TaskClueVO converterToTaskClueVO(TaskClue taskClue,String volunteerName){
        TaskClueVO taskClueVO = new TaskClueVO();
        BeanUtils.copyProperties(taskClue,taskClueVO);
        //还要设置一个POJO类里面没有的字段（志愿者姓名）
        taskClueVO.setVolunteerName(volunteerName);
        return taskClueVO;
    }


    /**
     * 把线索集合转换成voList
     * @param clueList
     * @return
     */
    public static List<TaskClueVO> converterToTaskClueVO(List<TaskClue> clueList){
        List<TaskClueVO> taskClueVOS = new ArrayList<>();
        for (TaskClue clue : clueList) {
            TaskClueVO clueVO = new TaskClueVO();
            BeanUtils.copyProperties(clue,clueVO);
            //这里还差一个志愿者的名字
            taskClueVOS.add(clueVO);
        }
        return taskClueVOS;
    }




}
