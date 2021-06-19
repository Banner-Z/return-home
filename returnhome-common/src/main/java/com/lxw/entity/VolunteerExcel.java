package com.lxw.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor//无参构造方法
@AllArgsConstructor//全参构造方法
@ContentRowHeight(30) //设置内容体列高
//@HeadRowHeight(20)  //设置表头高度
public class VolunteerExcel implements Serializable {
    private static final long serialVersionUID = 485320569669815013L;

    /**
     * 志愿者编号
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "志愿者编号", index = 0) // 定义表头名称和位置,0代表第一列
    private Long volunteerId;

    /**
     * 志愿者姓名
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "志愿者姓名", index = 1)
    private String volunteerName;

    /**
     * 性别 (0 男 1 女）
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "性别(0 男 1 女)", index = 2)
    private Integer sex;

    /**
     * 志愿者联系方式
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "联系方式", index = 3)
    private String phoneNumber;

    /**
     * 常住地址
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "常住地址", index = 4)
    private String address;

    /**
     * 创建时间
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "创建时间", index = 5)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
