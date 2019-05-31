package adinnet.service;

import com.adinnet.repository.Semester;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by adinnet on 2018/9/20.
 */
public interface SemesterService {
    public void save(Semester semester);

    public void update(Semester semester);

    public void delete(Semester semester);

    /**
     * 分页查询
     * @param semester
     * @return
     */
    public Page<Semester> pageList(Semester semester);

    public List<Semester> selectAllList();

    /**
     * 查询本学期
     * @return
     */
    public Semester queryThisSemester();

    public Semester queryByCourseId(Integer courseId);

    public Semester queryById(Integer id);

    //保存
    void saveSemester(Semester semester);

    //修改
    void updateSemester(Semester semester);

    public Semester isHas();

    void del(Integer id);

    public List<Semester> queryCreaditBookOthers();

    /**
     *
     * @return
     */
    public List<Semester> queryAllSemester();
}
