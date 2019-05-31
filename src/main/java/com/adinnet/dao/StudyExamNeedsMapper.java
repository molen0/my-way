package com.adinnet.dao;

import com.adinnet.repository.StudyExamNeeds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by wangx on 2018/9/18.
 */
public interface StudyExamNeedsMapper extends JpaRepository<StudyExamNeeds, Integer>, JpaSpecificationExecutor<StudyExamNeeds> {

    @Query("update StudyExamNeeds se set se.title=:title,se.textArea=:textArea,se.isDeleted=:isDeleted,  se.status=:status,se.type=:type ,se.updateTime = now() where se.id =:id")
    @Modifying
    public Integer updateById(@Param(value = "id") Integer id, @Param(value = "title") String title, @Param(value = "type") Integer type, @Param(value = "textArea") String textArea,
                              @Param(value = "isDeleted") Integer isDeleted, @Param(value = "status") Integer status);


    @Query("update StudyExamNeeds se set se.isDeleted=0 where se.id =:id")
    @Modifying
    public Integer delById(@Param(value = "id") Integer id);

    @Query("from StudyExamNeeds se  where se.isDeleted =1 and se.type =:type and se.status =:status")
    public List<StudyExamNeeds> findAllList(@Param("type") Integer type, @Param("status") Integer status);
    @Query("from StudyExamNeeds se  where se.isDeleted =1 and se.type =:type")
    public List<StudyExamNeeds> findList(@Param("type") Integer type);
}





