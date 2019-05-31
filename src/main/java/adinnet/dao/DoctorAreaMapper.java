
package adinnet.dao;

import com.adinnet.repository.DoctorArea;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by Administrator on 2018/11/1.
 */
public interface DoctorAreaMapper extends Repositor<DoctorArea, Integer>, JpaSpecificationExecutor<DoctorArea> {

    @Query( value = "select * from tb_doctor_area  da  where  da.area_code = :areaCode ",nativeQuery = true)
    public DoctorArea findByareaCode(@Param("areaCode") String areaCode);


    @Query(value = "SELECT COUNT(1) ,tda.area_name,tda.area_code FROM tb_semester_doctor tsd \n" +
            "LEFT JOIN tb_doctor td ON td.id=tsd.doctor_id \n" +
            "LEFT JOIN tb_doctor_area tda ON tda.area_code=td.area\n" +
            "where tsd.semester_id = :semesterId and td.property = :property GROUP BY td.area",nativeQuery = true)
    List findAllArea(@Param(value = "semesterId") Integer semesterId, @Param(value = "property") Integer property);

    @Query( value = "select * from tb_doctor_area  ",nativeQuery = true)
    public List<DoctorArea>findAllArea();

    @Query(value = "SELECT  tda.*,(case when m.count is null then 0 else m.count end) as count FROM tb_doctor_area tda \n" +
            "LEFT JOIN ( SELECT COUNT(1) count,tda.area_name,tda.area_code FROM tb_semester_doctor tsd \n" +
            "LEFT JOIN tb_doctor td ON td.id = tsd.doctor_id \n" +
            "LEFT JOIN tb_doctor_area tda ON tda.area_code = td.area WHERE tsd.semester_id = :semesterId AND td.property = :property GROUP BY td.area ) m ON tda.area_code = m.area_code",nativeQuery = true)
    List findAllAreas(@Param(value = "semesterId") Integer semesterId, @Param(value = "property") Integer property);
}

