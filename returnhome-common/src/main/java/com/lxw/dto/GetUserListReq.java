package com.lxw.dto;

import lombok.Data;

/**
 * 分页查询管理员列表的请求表单
 */
@Data
public class GetUserListReq {

    private Integer pageNum;

    private Integer pageSize;

    private Long volunteerId;

}
