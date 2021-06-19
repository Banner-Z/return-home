package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 某个历史任务的详细信息
 */
@Data
public class PastTaskDetailsVO {

    //老人信息
    private String lostmanName;

    private Integer lostmanAge;

    private String lostmanPos;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    //任务信息
    private Integer level;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String rescuePos;

    //家属信息
    private String familyName;

    private String familyPhoneNumber;




}
