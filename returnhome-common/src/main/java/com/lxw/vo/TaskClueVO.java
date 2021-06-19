package com.lxw.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 任务线索展示
 */
@Data
public class TaskClueVO {

    //志愿者编号 （-1代表家属 0代表指挥系统）
    private Long volunteerId;

    //志愿者姓名
    private String volunteerName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    private String clue;

}
