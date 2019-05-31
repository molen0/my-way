package com.adinnet.dao;

import com.adinnet.repository.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by adinnet on 2018/9/20.
 */
public interface SemesterMapper extends JpaRepository<Semester, Integer>, JpaSpecificationExecutor<Semester> {
    /**
     * 查询本学期
     * @return
     */
    @Query("from Semester s where s.status = 1")
    public Semester queryThisSemester();

    /**
     * 查询学期
     * @return
     */
    @Query("from Semester s where s.id = :id")
    public Semester queryById(@Param("id") Integer id);

    /**
     * 查询课程
     * @param
     * @return
     */
    @Query("update Semester set status = 0")
    @Modifying
    public void updateOthers();

    /**
     * 删除学期
     * @param id
     * @return
     */
    @Query("update Semester set isDelete = 0 where id = :id")
    @Modifying
    public void updateById(@Param(value = "id") Integer id);

    /**
     * 查询出没有模板的学期
     * @return
     */
    @Query(value = "select s.* from tb_semester s left join tb_cradit_model c on c.semester_id = s.id and c.is_delete = 1 where c.id is null and s.is_delete = 1",nativeQuery = true)
    public List<Semester> queryCreaditBookOthers();
    /**
     * 查询本学期
     * @return
     */
    @Query("from Semester s where 1 = 1")
    public List<Semester> queryAllSemester();

}
