package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 志愿者信息表
 * </p>
 *
 * @author lxw
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_volunteer_info")
@ApiModel(value="Volunteer对象", description="志愿者信息表")
@AllArgsConstructor
@NoArgsConstructor
public class Volunteer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "志愿者ID  (-1代表家属 0代表指挥系统) ")
    @TableId(value = "volunteer_id", type = IdType.AUTO)
    private Long volunteerId;

    @ApiModelProperty(value = "微信关联的账号")
    private String openid;

    @ApiModelProperty(value = "志愿者真实姓名")
    private String volunteerName;

    @ApiModelProperty(value = "性别 男0 女1")
    private Integer sex;

    @ApiModelProperty(value = "电话号码注册(唯一）")
    private String phoneNumber;

    @ApiModelProperty(value = "常住地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像url")
    private String avatar;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "状态 空闲0 任务中1")
    private Integer status;

    @ApiModelProperty(value = "身份证号")
    @TableField(value = "IDCard")
    private String IDCard;

}
