package com.lxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 指挥系统上报走失老人的表单请求对象
 */
@Data
public class SystemReportLostmanReq {

    private String lostmanName;

    private String lostmanPos;

    private Integer lostmanSex;

    private Integer lostmanAge;

    private String lostmanIDCard;

    private String lostmanRemark;

    //这里是没有经纬度的
//    private String lostmanPosLongitude;
//
//    private String lostmanPosLatitude;

    //会把接收到的String转成 Date 但是要注意yy/mm 和yy-mm的区别 否者会报异常
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    private String familyName;

    private String familyPhoneNumber;




}
