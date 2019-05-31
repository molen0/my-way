package com.adinnet.service;

import com.adinnet.repository.vo.DoctorAreaVo;

import java.util.List;

/**
 * 区域医生数
 * Created by RuanXiang on 2018/11/9.
 */
public interface DoctorAreaService {
    public List<DoctorAreaVo> getDoctorAreaInfo(Integer property);
}
