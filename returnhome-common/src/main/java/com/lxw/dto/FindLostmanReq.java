package com.lxw.dto;

import lombok.Data;

/**
 * 查询老人信息的表单
 */
@Data
public class FindLostmanReq {

    private Integer pageNum;

    private Integer pageSize;

    private String address;

    private String lostmanName;

    private Integer status;

}
