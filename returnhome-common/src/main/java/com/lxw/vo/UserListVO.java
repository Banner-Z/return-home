package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 分页查询管理员的页面展示信息
 */
@Data
public class UserListVO {

    private Long id;

    private String username;

    private String roleName;

    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
