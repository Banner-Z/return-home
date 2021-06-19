package com.lxw.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxw
 * @since 2021-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_family")
@ApiModel(value="Family对象", description="")
@NoArgsConstructor
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "家属ID")
    @TableId(value = "family_id", type = IdType.AUTO)
    private Long familyId;

    @ApiModelProperty(value = "方便小程序端操作")
    private String openid;

    @ApiModelProperty(value = "家属姓名")
    private String familyName;

    @ApiModelProperty(value = "家属联系方式")
    @TableField("family_phoneNumber")
    private String familyPhoneNumber;

    public Family(String openid,String familyName,String familyPhoneNumber){
        this.openid = openid;
        this.familyName = familyName;
        this.familyPhoneNumber = familyPhoneNumber;
    }

}
