package adinnet.service;

import com.adinnet.repository.Doctor;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by RuanXiang on 2018/9/26.
 */
public interface OnlineSignUpService {

    public JSONObject saveSemesterAndDoctor(Doctor doctor);
}
