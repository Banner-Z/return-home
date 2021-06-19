package com.lxw.dto;

import lombok.Data;

/**
 * 上报线索的表单
 */
@Data
public class ReportClueReq {

    private Long taskId;

    private Long volunteerId;

    private String clue;

}
