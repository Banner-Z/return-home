package com.lxw.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 每个任务的实体对象
 */
@Data
public class TaskDTO {

    private Long taskId;

    private int level;

    private Long lostmanId;

    private Date startTime;

    private Date endTime;

    //志愿者列表
    private List<TaskVolunteerDTO> volunteerList;

    //老人的位置和坐标
    private String lostmanPos;

    private String lostmanPosLongitude;

    private String lostmanPosLatitude;

}
