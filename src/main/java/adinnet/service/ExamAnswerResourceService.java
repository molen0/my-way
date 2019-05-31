package adinnet.service;

import com.adinnet.repository.CourseCourseware;
import com.adinnet.repository.CourseLearningResource;
import com.adinnet.repository.ExamAnswerResource;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.dto.CourseLearningResourceDto;
import com.adinnet.repository.dto.ExamAnswerResourceDto;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/9.
 */
public interface ExamAnswerResourceService {

    /**
     * 分页查询
     * @param examAnswerResourceDto
     * @return
     */
    public Page<List<Map<String, Object>>> pageList(ExamAnswerResourceDto examAnswerResourceDto);

    public PageBean<ExamAnswerResource> pageList3(ExamAnswerResource examAnswerResource);
    /**
     *
     * @param id
     * @return
     */
    public ExamAnswerResource getOne(Integer id);
    /**
     * @param examAnswerResource
     * @return
     */
    public ModelMap save(ExamAnswerResource examAnswerResource, MultipartFile file, MultipartFile videoFile) throws IOException, HttpException;

    /**
     *
     * @param examAnswerResource
     * @return
     */
    public ModelMap update(ExamAnswerResource examAnswerResource, MultipartFile file, MultipartFile videoFile)throws IOException,HttpException;

    /**
     *
     * @param courseId
     * @return
     */
     public JSONObject findByCourseId(Integer courseId);

    /**
     *
     * @param id
     * @return
     */
    public Integer deleteById(Integer id);
}
