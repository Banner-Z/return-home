package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author test
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_quartz")
@ApiModel(value="JobTask对象", description="系统定时任务表")
public class Quartz implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统任务ID")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "系统任务名称")
    private String jobName;

    @ApiModelProperty(value = "系统任务执行类")
    private String jobClass;

    @ApiModelProperty(value = "系统任务调度时间表达式")
    private String cronExpression;

    @ApiModelProperty(value = "系统任务状态，0：启动，1：暂停，2：停用")
    private Integer status;


}
