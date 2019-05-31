package adinnet.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wangren
 * @Description: 系统用户
 * @create 2018-09-14 13:32
 **/
@Controller
@RequestMapping("/api/member")
public class UserController {

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "member/index";
    }
}
