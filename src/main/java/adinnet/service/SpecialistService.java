package adinnet.service;

import com.adinnet.repository.Specialist;
import org.apache.http.HttpException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by RuanXiang on 2018/9/20.
 */
public interface SpecialistService {

    /**
     * 查询本学期
     * @return
     */
    public List<Specialist> querySpecialistByCourse(Integer courseId);

    public String querySpecialistName(Integer courseId);

    /**
     * 分页查询
     * @param specialist
     * @return
     */
    public Page<Specialist> pageList(Specialist specialist);

    //根据id查询
    public Specialist queryById(Integer id);

    //保存
    void saveSpecialist(Specialist specialist, MultipartFile file) throws IOException, HttpException;

    //修改
    void updateSpecialist(Specialist Specialist, MultipartFile file) throws IOException, HttpException;

    void del(Integer id);

    public List<Specialist> getList(Integer id);
}
