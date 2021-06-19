package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2021-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_lostman_info")
@ApiModel(value="LostmanInfo对象", description="")
public class LostmanInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "走失老人ID")
    @TableId(value = "lostman_id", type = IdType.AUTO)
    private Long lostmanId;

    @ApiModelProperty(value = "走失老人姓名(必填)")
    private String lostmanName;

    @ApiModelProperty(value = "走失老人性别（0:男 1:女)")
    private Integer lostmanSex;

    @ApiModelProperty(value = "走失老人年龄")
    private Integer lostmanAge;

    @ApiModelProperty(value = "走失老人身份证号")
    @TableField("lostman_IDCard")
    private String lostmanIDCard;

    @ApiModelProperty(value = "备注信息(面部、衣着特征,身体状况)")
    private String lostmanRemark;

    @ApiModelProperty(value = "大致走失地(必填项)")
    private String lostmanPos;

    @ApiModelProperty(value = "走失地-经度(必填项)")
    private String lostmanPosLongitude;

    @ApiModelProperty(value = "走失地-纬度(必填项)")
    private String lostmanPosLatitude;

    @ApiModelProperty(value = "走失时间(必填项)")
    private Date lostTime;

    @ApiModelProperty(value = "家属ID")
    private Long familyId;

    @ApiModelProperty(value = "老人状态(0:搜寻中 1:已找到)")
    private Integer status;

}
