package adinnet.service;

import com.adinnet.repository.Examin;
import com.adinnet.repository.ExaminTopic;
import com.adinnet.repository.ExaminTopicAnswer;
import com.adinnet.repository.PageBean;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 考试课程
 * @create 2018-09-26 14:01
 **/
public interface ExaminTopicService {

    public PageBean<Examin> pageList(Examin examin);

    public Integer getExaminId(Integer courseId);

    public List<ExaminTopic> listExaminTopic(Integer id);

    public List<ExaminTopicAnswer> listByAnswer(Integer id);

    public void saveExamin(Integer[] courseIds);

    public void saveExaminTopic(ExaminTopic examinTopic);

    public void saveExaminTopicAnswer(ExaminTopicAnswer examinTopicAnswer);

    public void updateExamin(Integer id, Integer courseId);

    public void updateExaminTopic(ExaminTopic examinTopic);

    public void updateExaminTopicAnswer(ExaminTopicAnswer examinTopicAnswer);

    public ExaminTopic getOneExaminTopic(Integer id);

    public ExaminTopicAnswer getOneExaminTopicAnswer(Integer id);

    public void delExaminTopic(Integer id);

    public void delExaminTopicAnswer(Integer id);

    public void delExamin(Integer id);


}
