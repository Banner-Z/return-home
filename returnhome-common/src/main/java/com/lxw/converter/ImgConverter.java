package com.lxw.converter;

import com.lxw.entity.LostmanImg;
import com.lxw.vo.ImgVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 照片转换器
 */
public class ImgConverter {


    /**
     * 转VO
     */
    public static ImgVO converterToImgVO(LostmanImg img){
        ImgVO vo = new ImgVO();
        BeanUtils.copyProperties(img,vo);
        return vo;
    }

    /**
     * 转VOList
     */
    public static List<ImgVO>  converterToImgVOList(List<LostmanImg> imgList){
        List<ImgVO> vos = new ArrayList<>();
        imgList.forEach(img -> {
            vos.add(converterToImgVO(img));
        });
        return vos;
    }





}
