package adinnet.dao;

import com.adinnet.repository.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hasee on 2018/9/19.
 */
public interface DoctorMapper extends JpaRepository<Doctor, Integer>, JpaSpecificationExecutor<Doctor> {
    /**
     * 查询医生信息是否已存在
     * @param uid 万达信息医生id
     * @return
     */
    @Query("from Doctor d where d.uid = :uid")
    public Doctor queryByUid(@Param(value = "uid") String uid);

    /**
     * 查询医生信息
     * @param id 医生id
     * @return
     */
    @Query("from Doctor d where d.id = :id")
    public Doctor queryById(@Param(value = "id") Integer id);

    @Query("from Doctor d where d.name = :name")
    public Doctor queryByName(@Param(value = "name") String name);


    @Query(value="select * from tb_doctor c where c.name like CONCAT('%',:name,'%')  order by c.update_time desc limit 1",nativeQuery = true)
    public Doctor queryByName1(@Param(value = "name") String name);

    @Query("from Doctor d where  d.name like CONCAT('%',:name,'%')")
    public List<Doctor> queryByNames(@Param(value = "name") String name);

    /**
     * 执行更新医生头像等信息
     */
    @Query(value="update tb_doctor d set d.property =:property,d.area=:area,d.photo=:photo,d.company=:company,d.update_time=now() where d.id=:id",nativeQuery = true)
    @Modifying
    public void update(@Param("property") Integer property, @Param("area") String area, @Param("photo") String photo, @Param("company") String company, @Param("id") Integer id);
}
