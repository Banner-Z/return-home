package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxw
 * @since 2021-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_lostman_img")
@ApiModel(value="LostmanImg对象", description="")
public class LostmanImg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pic_id", type = IdType.AUTO)
    private Long picId;

    @ApiModelProperty(value = "脸部照片")
    private String picUrl;

    @ApiModelProperty(value = "老人照片类型(0:脸部 1:身体)")
    private Integer type;

    @ApiModelProperty(value = "走失老人编号")
    private Long lostmanId;


}
