package adinnet.service;

import com.adinnet.repository.Course;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by RuanXiang on 2018/9/19.
 */
public interface CourseService {
    public void save(Course course);

    public void update(Course course);

    public void delete(Course course);

    public Course queryById(Integer id);

    /**
     * 分页查询
     * @param course
     * @return
     */
    public Page<Course> pageList(Course course);

    public List<Course> selectAllList();

    public Integer updateCourseNum(Integer id);

    public JSONObject queryListByParam(Integer doctorId, Integer courseType);

    public JSONObject queryIntroduceById(Integer courseId);

    public List<Course> queryByExam();

    //保存
    void saveCourse(Course course, MultipartFile file, MultipartFile imageFile) throws IOException, HttpException;

    //修改
    void updateCourse(Course course, MultipartFile file, MultipartFile imageFile) throws IOException, HttpException;

    void del(Integer id);

    public Course getOne(Integer id);

    public JSONObject queryIndexInfo(Integer doctorId);

    /**
     *
     * @return
     */
    public List<Course> queryListAllExists(Integer courseId);

    void doSpecialist();
}
