package com.adinnet.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Transient;

/**
 * @author wangren
 * @Description: 基础实体
 * @create 2018-09-17 15:01
 **/
@Data
public class BaseEntity {

    @Transient
    @JsonIgnore
    private Integer offset = 0;

    @Transient
    @JsonIgnore
    private Integer limit = 10;

    public Integer page(){
        if(0 ==this.offset.intValue() || 1 ==this.offset.intValue()){
            return 0;
        }else{
            return offset/limit;
        }
    }
}
