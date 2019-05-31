package adinnet.dao;

import com.adinnet.repository.DoctorInfoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by adinnet on 2018/11/13.
 */
public interface DoctorInfoLogMapper extends JpaRepository<DoctorInfoLog, Integer>, JpaSpecificationExecutor<DoctorInfoLog> {

}
