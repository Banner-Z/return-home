package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 1.家属端展示某个任务的详情
 * 2.指挥系统展示老人的页面对象
 */
@Data
public class FamilyTaskDetailsVO {

    //家属信息
    private String familyName;

    private String familyPhoneNumber;

    //老人信息
    private Long lostmanId;

    private String lostmanName;

    private String lostmanPos;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    private Integer status;

    private String lostmanIDCard;

    private Integer lostmanAge;

    private Integer lostmanSex;

    private String lostmanRemark;

    private List<ImgVO> imgList;

}
