package com.lxw.dto;

import lombok.Data;

/**
 * 更新志愿者最新位置
 */
@Data
public class VolunteerPosReq {

    private Long volunteerId;

    private String longitude;

    private String latitude;

}
