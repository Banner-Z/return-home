package com.lxw.dto;

import lombok.Data;

/**
 * 修改志愿者信息的表单
 */
@Data
public class UpdateVolunteerInfoReq {

    private Long volunteerId;

    private String email;

    private String phoneNumber;

    private String address;

    private Integer status;

}
