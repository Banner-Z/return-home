package com.lxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 家属报案走失老人的表单
 */
@Data
public class ReportLostmanReq {

    private String lostmanName;

    private String lostmanPos;

    private String lostmanPosLongitude;

    private String lostmanPosLatitude;

    //会把接收到的String转成 Date 但是要注意yy/mm 和yy-mm的区别 否者会报异常
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    private String familyName;

    private String familyPhoneNumber;

    //家属端关联的微信账号
    private String openid;




}
