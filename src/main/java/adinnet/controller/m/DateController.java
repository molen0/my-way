package adinnet.controller.m;

import com.adinnet.response.code.CCode;
import com.adinnet.response.json.JsonResult;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author wangren
 * @Description: 获取服务器时间
 * @create 2018-11-09 16:45
 **/
@RestController
@RequestMapping("m/date")
public class DateController {

    @RequestMapping(value = "/getDate", method = {RequestMethod.GET})
    public JsonResult checkDate(){
        try {
            Map<String,String> map = new HashedMap();
            Date date = new Date();
            map.put("date",String.valueOf(date.getTime()/1000));
                return JsonResult.OK(map);
        } catch (Exception e) {
            return JsonResult.build(CCode.C_Buz_Ex);
        }
    }
}
