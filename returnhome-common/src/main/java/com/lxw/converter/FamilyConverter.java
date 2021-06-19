package com.lxw.converter;


import com.lxw.entity.*;
import com.lxw.vo.FamilyTaskDetailsVO;
import com.lxw.vo.ReportTaskRecordsVO;
import org.springframework.beans.BeanUtils;


/**
 * 家属端的转换器
 */
public class FamilyConverter {

    /**
     * 转过往任务上报记录的VO
     */
    public static ReportTaskRecordsVO converterToReportTaskRecordsVO(LostmanInfo lostman, Task task) {
        ReportTaskRecordsVO vo = new ReportTaskRecordsVO();
        BeanUtils.copyProperties(lostman,vo);
        BeanUtils.copyProperties(task,vo);
        return vo;
    }

    /**
     * 转某个任务详情的VO
     */
    public static FamilyTaskDetailsVO converterToFamilyTaskVO(LostmanInfo lostman, Family family) {
        FamilyTaskDetailsVO vo = new FamilyTaskDetailsVO();
        BeanUtils.copyProperties(lostman,vo);
        BeanUtils.copyProperties(family,vo);
        return vo;
    }






}
