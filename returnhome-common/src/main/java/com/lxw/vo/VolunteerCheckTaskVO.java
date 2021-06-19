package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 志愿者查看任务列表 (当前任务和历史任务）
 */
@Data
public class VolunteerCheckTaskVO {

    private String lostmanName;

    private Integer lostmanAge;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    private Long taskId;

    private Integer level;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String rescuePos;

    //照片
    private ImgVO img;

}
