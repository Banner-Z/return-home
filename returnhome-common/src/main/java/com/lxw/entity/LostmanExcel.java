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
public class LostmanExcel implements Serializable {
    private static final long serialVersionUID = 485320569669815023L;

    /**
     * 老人编号
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "老人编号", index = 0) // 定义表头名称和位置,0代表第一列
    private Long lostmanId;

    /**
     * 老人姓名
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "老人姓名", index = 1)
    private String lostmanName;

    /**
     * 老人年龄
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "老人年龄", index = 2)
    private Integer lostmanAge;

    /**
     * 老人备注
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "老人备注", index = 3)
    private String lostmanRemark;

    /**
     * 走失大致位置
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "走失大致位置", index = 4)
    private String lostmanPos;

    /**
     * 走失时间
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "走失时间", index = 5)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;


}

