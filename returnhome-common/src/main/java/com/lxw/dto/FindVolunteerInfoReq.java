package com.lxw.dto;

import lombok.Data;


/**
 * 查询志愿者信息的表单
 */
@Data
public class FindVolunteerInfoReq {

    private Integer pageNum;

    private Integer pageSize;

    private Long volunteerId;

    private String volunteerName;

    private String phoneNumber;

    private Integer sex;

}
