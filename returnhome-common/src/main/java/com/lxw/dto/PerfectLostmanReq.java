package com.lxw.dto;

import lombok.Data;

/**
 * 走失老人信息完善的表单
 */
@Data
public class PerfectLostmanReq {

    private Long lostmanId;

    private Integer lostmanSex;

    private Integer lostmanAge;

    private String lostmanIDCard;

    private String lostmanRemark;

}
