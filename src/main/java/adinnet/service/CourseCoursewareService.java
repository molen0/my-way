package adinnet.service;

import com.adinnet.repository.CourseCourseware;
import com.adinnet.repository.CourseLearningResource;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.dto.CourseCoursewareDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by wangx on 2018/9/21.
 */
public interface CourseCoursewareService {
    /**
     * @param courseCoursewareDto 分页查询
     * @return
     */
    public Page<List<Map<String, Object>>> pageList(CourseCoursewareDto courseCoursewareDto);

    public PageBean<CourseCourseware> pageList3(CourseCourseware courseCourseware);

    /**
     * @param courseId 查询图片接口
     * @return
     */
    public JSONObject findByCourseId(Integer courseId);
    /**
     *
     * @param id
     * @return
     */
    public CourseCourseware getOne(Integer id);
    /**
     * @param courseCourseware
     * @return
     */
    public ModelMap save(CourseCourseware courseCourseware, MultipartFile file) throws IOException;

    /**
     *
     * @param courseCourseware
     * @return
     */
    public ModelMap update(CourseCourseware courseCourseware, MultipartFile file)throws IOException;
    /**
     *
     * @param ids
     * @return
     */
    public Integer deleteById(Integer ids);


}
