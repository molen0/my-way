package com.adinnet.dao;

import com.adinnet.repository.Attach;
import com.adinnet.repository.CourseLearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/19.
 */
public interface AttachMapper extends JpaRepository<Attach, Integer>, JpaSpecificationExecutor<Attach> {

    @Query("from Attach a where a.attachId =:attachId")
    public List<Attach> findByAttachId(@Param("attachId") String attachId);

    @Query("from Attach a where  a.attachId =:attachId")
    public List<Map<String, Object>> findByAttachId1(@Param("attachId") String attachId);

    @Query("delete from Attach where  attachId =:attachId")
    @Modifying
    public Integer delByAttachId(@Param("attachId") String attachId);
}
