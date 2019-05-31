package adinnet.service;

import com.adinnet.repository.EvaluateTag;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by hasee on 2018/9/18.
 */
public interface EvaluateTagService {


    public void save(EvaluateTag evaluateTag);

    public void update(EvaluateTag evaluateTag);

    public void delete(EvaluateTag evaluateTag);

    /**
     * 分页查询
     * @param evaluateTag
     * @return
     */
    public Page<EvaluateTag> pageList(EvaluateTag evaluateTag);

    /**
     * 根据主键id
     * 查询
     * @param id
     * @return
     */
    public EvaluateTag getOne(Integer id);


    public void saveEvaluateTag(EvaluateTag evaluateTag);

    public void updateEvaluateTag(EvaluateTag evaluateTag);

    public void del(Integer id);

    public JSONObject queryAllTagList();

    public List<EvaluateTag> getReportList(EvaluateTag evaluateTag);

    public JSONObject queryAllTagList2();
}
