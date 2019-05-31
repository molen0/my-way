package adinnet.service;

import com.adinnet.repository.Menu;
import com.adinnet.repository.StudyExamNeeds;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by wangx on 2018/9/18.
 */
public interface StudyExamNeedsService {

    /**
     * @return
     */
    public JSONObject queryAllNeeds(Integer type, Integer status);

    /**
     * @param studyExamNeeds 分页查询
     * @return
     */
    public Page<StudyExamNeeds> pageList(StudyExamNeeds studyExamNeeds);

    /**
     * @param studyExamNeeds
     * @return
     */
    public Integer save(StudyExamNeeds studyExamNeeds);

    /**
     *
     * @param studyExamNeeds
     * @return
     */
    public Integer update(StudyExamNeeds studyExamNeeds);

    /**
     *
     * @param id
     * @return
     */
    public Integer deleteById(Integer id);

    /**
     *
     * @param id
     * @return
     */
    public StudyExamNeeds getOne(Integer id);
}
