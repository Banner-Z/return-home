package com.lxw.dto;

import lombok.Data;


/**
 * 添加志愿者的请求表单
 */
@Data
public class AddVolunteerReq {

    /**
     * 必填项
     */

    private String account; //志愿者账号

    private String password;

    private String volunteerName;

    private String IDCard;

    private String phoneNumber;

    private String address;

    private Integer sex;


    /**
     * 非必填项
     */
    private String email;

}
