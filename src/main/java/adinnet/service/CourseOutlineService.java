package adinnet.service;

import com.adinnet.repository.Course;
import com.adinnet.repository.CourseOutline;
import com.adinnet.repository.Examin;
import com.adinnet.repository.PageBean;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/18.
 */
public interface CourseOutlineService {

    /**
     * 查询课程大纲
     * @param courseId 课程id
     * @param doctorId 医生id
     * @return
     */
    public Map queryByCourseId(Integer courseId, Integer doctorId);

    /**
     * 分页查询
     * @param courseOutline
     * @return
     */
    Page<CourseOutline> pageList(CourseOutline courseOutline);

    /**
     * 查询单个
     * @param id
     * @return
     */
    CourseOutline getOne(Integer id);

    ModelMap saveCourseOutline(CourseOutline courseOutline, MultipartFile file) throws IOException, HttpException;

    ModelMap updateCourseOutline(CourseOutline courseOutline, MultipartFile file) throws IOException, HttpException;

    void del(Integer id);

    Page<List<Map<String, CourseOutline>>> pageList1(CourseOutline courseOutline);

    PageBean<CourseOutline> pageList3(CourseOutline courseOutline);

    PageBean<CourseOutline> pageList4(CourseOutline courseOutline);

    /**
     * 判断是否可以开始考试
     * @param courseId
     * @param doctorId
     * @return
     */
    boolean startExam(Integer courseId, Integer doctorId);
}
