package adinnet.service;

import com.adinnet.repository.ExperienceShare;
import com.adinnet.repository.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/10/9.
 */
public interface ExperienceShareService {

    public Page<ExperienceShare> pageList(ExperienceShare experienceShare);

    ExperienceShare getOne(Integer id);

    void del(Integer id);

    /**
     * 查询经验分享
     * @param doctorId
     * @return
     */
    Map queryExperienceShare(Integer doctorId);

    Map getListByType(Integer doctorId, Integer type);

    Map getDetail(Integer doctorId, Integer id);

    ModelMap saveExperienceShare(ExperienceShare experienceShare, MultipartFile file, MultipartFile photo) throws IOException;

    ModelMap updateExperienceShare(ExperienceShare experienceShare, MultipartFile file, MultipartFile photo) throws IOException;
}
