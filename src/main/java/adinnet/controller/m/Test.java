package adinnet.controller.m;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by adinnet on 2018/11/15.
 */
public class Test {
    public static void main(String[] args) {
        JSONObject info = new JSONObject();
        info.put("doctype","2");
        Integer a= 1;
        if(null==info.getInteger("doctype")){
            a= 1;
        }else if(info.getInteger("doctype")==2){
            a= 2;
        }
        System.out.println(a);
    }
}
