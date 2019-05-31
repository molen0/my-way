package adinnet.dao;

import com.adinnet.repository.Examin;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



/**
 * @author wangren
 * @Description: 考试课程主类
 * @create 2018-09-26 14:15
 **/

public interface ExaminMapper extends Repositor<Examin, Integer>,JpaSpecificationExecutor<Examin> {

}
