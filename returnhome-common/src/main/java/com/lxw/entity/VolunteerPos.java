package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_volunteer_pos")
@ApiModel(value="VolunteerPos对象", description="")
public class VolunteerPos implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "志愿者ID")
    @TableId(value = "volunteer_id")
    private Long volunteerId;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;


}
