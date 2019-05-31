package com.adinnet.service.Impl;

import com.adinnet.ex.BuzEx;
import com.adinnet.repository.DoctorArea;
import com.adinnet.repository.vo.DoctorAreaVo;
import com.adinnet.response.code.CCode;
import com.adinnet.service.DoctorAreaService;
import com.adinnet.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区域医生
 * Created by RuanXiang on 2018/11/9.
 */
@Service
public class DoctorAreaServiceImpl implements DoctorAreaService {

    private static final String WANDA_authKey = "771a832a-0a38-4403-9a55-3aa773e19a61";
    //测试
    //private static final String WANDA_url = "http://218.80.250.99/healthCloud-open-gateway-te/gateway/7DF3237C-2237-4668-812A-6855C2B204A5";

    //生产
    private static final String WANDA_url = "http://172.17.13.31/healthCloud-esb/esb/C2492964-77F4-45CA-B2C5-8A9F9540671B";

    @Override
    public List<DoctorAreaVo> getDoctorAreaInfo(Integer property){
        Map<String, String> params = new HashMap<>();
        params.put("doctype",property==0?"2":"1");
        try {
            JSONObject jsonObject = HttpClientUtil.httpsGet(WANDA_url,params,WANDA_authKey);
            Integer code = jsonObject.getInteger("code");
            List<DoctorAreaVo> list = new ArrayList<DoctorAreaVo>();
            if(code == 0){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                String str = JSONObject.toJSONString(jsonArray);
                list = JSONObject.parseArray(str, DoctorAreaVo.class);
                return list;
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
