package com.lxw.dto;

import lombok.Data;

/**
 * 任务中每个志愿者的实体对象
 * 只需要知道志愿者的ID,名字,联系方式,以及当前位置
 */
@Data
public class TaskVolunteerDTO {

    private Long volunteerId;

    private String volunteerName;

    private String phoneNumber;

    private String longitude;

    private String latitude;

}
