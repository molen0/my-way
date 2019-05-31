package adinnet.service;

import com.adinnet.repository.CourseEvaluate;
import com.adinnet.repository.Page1;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by hasee on 2018/9/18.
 */
public interface CourseEvaluateService {

    public void save(CourseEvaluate courseEvaluate);

    public void update(CourseEvaluate courseEvaluate);

    public void delete(CourseEvaluate courseEvaluate);

    public void del(Integer id);

    /**
     * 分页查询
     * @param courseEvaluate
     * @return
     */
    public Page<CourseEvaluate> pageList(CourseEvaluate courseEvaluate);

    public Page1<List<Map<String, Object>>> pageList1(CourseEvaluateDto courseEvaluateDto);


    /**
     * 添加评价
     * @param body
     */
    @Transactional
    public void saveCourseEvaluate(String body);

    public JSONObject queryAllEva(String body);


    public CourseEvaluate getOne(Integer id);
}
