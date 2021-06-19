package com.lxw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 家属上报的任务记录
 */
@Data
public class ReportTaskRecordsVO {

    private String lostmanName;

    private Long taskId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lostTime;

    private String lostmanPos;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String rescuePos;

}
