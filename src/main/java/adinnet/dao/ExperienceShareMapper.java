package adinnet.dao;

import com.adinnet.repository.ExperienceShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */
public interface ExperienceShareMapper extends JpaRepository<ExperienceShare, Integer>, JpaSpecificationExecutor<ExperienceShare> {

    @Query("from ExperienceShare c where c.id = :id")
    ExperienceShare queryById(@Param(value = "id") Integer id);

    @Query(value = "select c.* from tb_experience_share c where c.type = :type order by c.create_time desc limit 3",nativeQuery = true)
    List<ExperienceShare> findByType(@Param(value = "type") Integer type);

    @Query("from ExperienceShare c where c.type = :type")
    List<ExperienceShare> findMoreByType(@Param(value = "type") Integer type);

    @Query(value = "select c.* from tb_experience_share c order by c.create_time desc limit 6",nativeQuery = true)
    List<ExperienceShare> findSix();
}
