package adinnet.repository.vo;

import lombok.Data;

/**
 * 区域医生数
 * Created by RuanXiang on 2018/11/9.
 */
@Data
public class DoctorAreaVo {
    //医生数
    private Integer doctorNumer;

    //区域编码
    private String areaCode;

    //区域名称
    private String areaName;
}
