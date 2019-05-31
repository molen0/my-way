package adinnet.service;

import com.adinnet.repository.CourseFeedback;
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
public interface CourseFeedbackService {

    public void save(CourseFeedback courseFeedback);

    public void update(CourseFeedback courseFeedback);

    public void delete(CourseFeedback courseFeedback);

    public void del(Integer id);

    /**
     * 分页查询
     * @param courseFeedback
     * @return
     */
    public Page<CourseFeedback> pageList(CourseFeedback courseFeedback);

   public Page<List<Map<String, Object>>> pageList2(CourseFeedback courseFeedback);

    public Page1<List<Map<String, Object>>> pageList1(CourseEvaluateDto courseEvaluateDto);

    /**
     * 添加反馈
     * @param body
     */
    @Transactional
    public void saveFeedBack(String body);

    /**
     * 查询反馈
     * @param body
     * @return
     */
    public JSONObject queryAllFeed(String body);

    public CourseFeedback getOne(Integer id);


    public void updateCourseFeedback(CourseFeedback courseFeedback);

    public void saveFeedback(CourseFeedback courseFeedback);

}
