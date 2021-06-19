package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lxw.dto.TaskVolunteerDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *  任务列表展示
 */
@Data
public class TaskListVO {

    private Long taskId;

    private int level;

    private Long lostmanId;

    //还需要老人的姓名、位置以及坐标,方便地图定位
    private String lostmanName;

    private String lostmanPos;

    private String lostmanPosLongitude;

    private String lostmanPosLatitude;

    private String lostmanRemark;

    //家属的姓名和联系方式
    private String familyName;

    private String familyPhoneNumber;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private List<TaskVolunteerDTO> volunteerList;

    //老人照片
    private String imgUrl;

}
