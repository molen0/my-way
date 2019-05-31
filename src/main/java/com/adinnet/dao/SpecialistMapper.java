package com.adinnet.dao;

import com.adinnet.repository.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by RuanXiang on 2018/9/20.
 */
public interface SpecialistMapper  extends JpaRepository<Specialist, Integer>, JpaSpecificationExecutor<Specialist> {
    /**
     * 专家列表
     * @return
     */
    @Query(value = "select s.* from (select * from tb_course_specialist where course_id = :courseId) c left join tb_specialist s on c.specialist_id = s.id",nativeQuery = true)
    public List<Specialist> querySpecialistByCourse(@Param(value = "courseId") Integer courseId);

    /**
     * 查询专家
     * @return
     */
    @Query("from Specialist s where s.id = :id")
    public Specialist queryById(@Param("id") Integer id);

    /**
     * 查询课程
     * @param id
     * @return
     */
    @Query("update Specialist set isDelete = 0 where id = :id")
    @Modifying
    public void updateById(@Param(value = "id") Integer id);

    /**
     * 专家列表
     * @return
     */
    @Query("from Specialist where is_delete=1")
    public List<Specialist> querySpecialistAll();
}
