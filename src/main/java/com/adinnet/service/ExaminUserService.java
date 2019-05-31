package com.adinnet.service;

import com.adinnet.repository.ExaminUser;
import com.adinnet.repository.ExaminUserRecord;
import com.adinnet.repository.PageBean;
import com.adinnet.repository.UserInfo;
import com.adinnet.repository.vo.ExaminUserVo;
import com.adinnet.repository.vo.ExaminVo;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 用户考试记录
 * @create 2018-09-29 16:19
 **/
public interface ExaminUserService {

    public List<ExaminUser> selectUserId(Integer courseId, String userId);

    public List<ExaminUser> selectUser(Integer courseId, String userId);

    public ExaminUser save(Integer examinId, String userId);

    public ExaminVo selectExamin(Integer courseId);

    public Map saveExaminUserVo(ExaminUserVo examinUserVo, JSONArray jsonArray);

    public Map checkDate(Integer courseId, String userId);

    public void updateExaminUser(Integer semesterId, Integer id, Integer right, Integer error, Integer courseId);

    public ExaminUser wrongset(Integer courseId, String userId);

    public List<ExaminUserRecord> selectExaminUserRecord(Integer examinId, String userId);

    /**
     * 分页查询
     * @param examinUser
     * @return
     */
    public PageBean<ExaminUser> pageList(ExaminUser examinUser);

    public List<ExaminUserRecord> selectRecordId(Integer recordId, Integer examinTopicId);

    public List<ExaminUser> examinUserList(Integer userId, Integer semesterId);

    public Map selectCradit(Integer courseId, Integer userId);
}
