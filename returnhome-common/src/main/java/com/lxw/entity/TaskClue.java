package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxw
 * @since 2021-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_task_clue")
@ApiModel(value="TaskClue对象", description="")
public class TaskClue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线索ID")
    @TableId(value = "clue_id", type = IdType.AUTO)
    private Long clueId;

    @ApiModelProperty(value = "任务编号(一个任务可以有多条线索)")
    private Long taskId;

    @ApiModelProperty(value = "志愿者编号（方便过程记录）0是指挥系统提交")
    private Long volunteerId;

    @ApiModelProperty(value = "时间戳")
    private Date timestamp;

    @ApiModelProperty(value = "线索")
    private String clue;


}
