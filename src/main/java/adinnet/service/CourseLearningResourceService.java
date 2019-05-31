package adinnet.service;

import com.adinnet.repository.Attach;
import com.adinnet.repository.Course;
import com.adinnet.repository.CourseLearningResource;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.dto.CourseEvaluateDto;
import com.adinnet.repository.dto.CourseLearningResourceDto;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wangx on 2018/9/20.
 */
public interface CourseLearningResourceService {


    /**
     * 分页查询
     * @param courseLearningResourceDto
     * @return
     */
    public  Page<List<Map<String, Object>>> pageList1(CourseLearningResourceDto courseLearningResourceDto);

    /**
     * @return
     */
    public JSONObject queryResourceByCourseId(Integer courseId);

    /**
     *
     * @param attachId 查询pdf文件转换成图片
     * @return
     */
    public JSONObject findPdfByphoto(String attachId);

    /**
     *
     * @param id
     * @return
     */
    public CourseLearningResource getOne(Integer id);


    /**
     * @param courseLearningResource
     * @return
     */
    public ModelMap save(CourseLearningResource courseLearningResource, MultipartFile file) throws IOException, HttpException;

    /**
     *
     * @param courseLearningResource
     * @return
     */
    public ModelMap update(CourseLearningResource courseLearningResource, MultipartFile file)throws IOException;
    /**
     *
     * @param ids
     * @return
     */
    public Integer deleteById(Integer ids);

    /**
     *
     * @param
     * @return
     */
    public List<Course> queryListAllExists();

    /**
     *
     * @param courseLearningResource
     * @return
     */
    public PageBean<CourseLearningResource> pageList3(CourseLearningResource courseLearningResource);



}
