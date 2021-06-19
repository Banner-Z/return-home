package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 志愿者端查看某个任务的详情信息页面
 */
@Data
public class VolunteerTaskDetailsVO {

    //家属信息
    private String familyName;

    private String familyPhoneNumber;

    //老人信息
    private Long lostmanId;

    private String lostmanName;

    private String lostmanPos;

    private String lostmanPosLongitude;

    private String lostmanPosLatitude;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    private Integer lostmanAge;

    private Integer lostmanSex;

    private String lostmanRemark;


    //任务信息
    private Integer level;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String rescuePos;


}
