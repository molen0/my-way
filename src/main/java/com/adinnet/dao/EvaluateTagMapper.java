package com.adinnet.dao;

import com.adinnet.repository.EvaluateTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hasee on 2018/9/18.
 */
public interface EvaluateTagMapper extends JpaRepository<EvaluateTag, Integer>, JpaSpecificationExecutor<EvaluateTag> {

    /**
     * 查询评价标签
     * @param type
     * @return
     */
    @Query("from EvaluateTag e where e.type = :type ")
    public List<EvaluateTag> queryTagByType(@Param(value = "type") Integer type);
}
