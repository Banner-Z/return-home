package com.lxw.converter;

import com.lxw.entity.Family;
import com.lxw.entity.LostmanInfo;
import com.lxw.vo.FamilyTaskDetailsVO;
import org.springframework.beans.BeanUtils;

import java.util.List;


/**
 * 老人信息转换器
 */
public class LostmanConverter {


    /**
     * 老人信息转vo
     */
   public static FamilyTaskDetailsVO converterToVO(LostmanInfo lostman, Family family){
       FamilyTaskDetailsVO vo = new FamilyTaskDetailsVO();
       BeanUtils.copyProperties(lostman,vo);
       BeanUtils.copyProperties(family,vo);
       return vo;
   }



}
