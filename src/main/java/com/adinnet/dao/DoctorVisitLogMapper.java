package com.adinnet.dao;

import com.adinnet.repository.DoctorVisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by adinnet on 2018/11/20.
 */
public interface DoctorVisitLogMapper extends JpaRepository<DoctorVisitLog, Integer>, JpaSpecificationExecutor<DoctorVisitLog> {
}
