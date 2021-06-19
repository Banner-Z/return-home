package com.lxw.vo;

import com.lxw.dto.VolunteerClueTaskDTO;
import lombok.Data;

import java.util.List;

/**
 * 提交线索时 查看某个志愿者的任务列表展示
 * 选择任务提交线索
 */
@Data
public class VolunteerClueTaskVO {

    private Long volunteerId;

    private String volunteerName;

    private List<VolunteerClueTaskDTO> taskList;

}
