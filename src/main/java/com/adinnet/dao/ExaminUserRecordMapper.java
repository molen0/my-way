package com.adinnet.dao;

import com.adinnet.repository.ExaminUserRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangren
 * @Description: 考试记录
 * @create 2018-09-30 9:36
 **/
public interface ExaminUserRecordMapper extends Repositor<ExaminUserRecord, Integer>,JpaSpecificationExecutor<ExaminUserRecord> {
}
