package com.adinnet.service;

import com.adinnet.repository.DoctorArea;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.ReportDoctor;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31.
 */
public interface ReportDoctorService {
    /**
     *
     * @param reportDoctor
     *          查询各区医生得分统计
     * @return
     */
    public PageBean<ReportDoctor> pageList(ReportDoctor reportDoctor);

    /**
     *
     * @return
     */
    public List<DoctorArea> findAllArea();

    /**
     *
     * @param reportDoctor
     *          查询各区医生得分统计
     * @return
     */
    public PageBean<ReportDoctor> page(ReportDoctor reportDoctor);
}
