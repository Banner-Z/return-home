package com.lxw.dto;

import lombok.Data;

/**
 * 体态识别的结果 封装对象
 */
@Data
public class BodyDetResDTO implements Comparable<BodyDetResDTO>{

    private String PersonId;

    private String TraceId;

    private Double Score;

    //对象比较
    @Override
    public int compareTo(BodyDetResDTO o) {
        if(this.getScore() < o.getScore())
            return -1;
        else
            return 0;
    }


}
