package com.lxw.vo;

import lombok.Data;

/**
 * 志愿者信息展示(去掉不用的密码等啥）
 */
@Data
public class VolunteerInfoVO {

    private Long volunteerId;

    private String volunteerName;

    private Integer sex;

    private String email;

    private String phoneNumber;

    private String address;

    private Integer status;

}
