package adinnet.controller.api;

import com.adinnet.repository.*;
import com.adinnet.service.*;
import com.adinnet.utils.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 学分管理
 * @create 2018-10-08 10:38
 **/
@Controller
@RequestMapping("/api/credit")
public class CreditController {

    @Autowired
    private ExaminUserService examinUserService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private EmailBookService emailBookService;
    @Autowired
    private SemesterService semesterService;
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String creditIndex(Model model) {
        return "credit/index";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(ExaminUser examinUser) {
        try{
            ModelMap map = new ModelMap();
            map.put("queryParam",examinUser);
            PageBean<ExaminUser> page = examinUserService.pageList(examinUser);
            map.put("pageInfo",page);
            return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }
    @RequestMapping(value = "/examinUserList", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap examinUserList(Integer userId, Integer semesterId) {
        try{
            ModelMap map = new ModelMap();
            List<ExaminUser> list = examinUserService.examinUserList(userId,semesterId);
            map.put("pageInfo",list);
            return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(Integer userId, Integer semesterId, Model model,Mail mail ) {
        List<ExaminUser> list = examinUserService.examinUserList(userId,semesterId);
            Double totle = 0.0;
            for(ExaminUser examinUser : list){
                if("1".equals(examinUser.getExamin().getCourse().getCourseType().toString())){
                    totle = totle + Double.parseDouble(examinUser.getCredit());
                }
            }
            if(totle>=0.5){
                mail.setDat(1);
            }else{
                mail.setDat(0);
            }
        model.addAttribute("mail",mail);
        return "credit/from";
    }
    @RequestMapping(value = "/email", method = {RequestMethod.GET})
    public String email( Model model,Integer id) {
        EmailBook emailBook = new EmailBook();
        List<EmailBook> emailBooks = emailBookService.selectBysemesterId(id);
        Semester semester = semesterService.queryById(id);
        if(emailBooks.size()>0){
            emailBook =emailBooks.get(0);
        }
            emailBook.setSemester(semester);
        model.addAttribute("emailBook",emailBook);
        return "credit/emailfrom";
    }

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save( Mail mail, BindingResult result) {
        try {
            if (result.hasErrors()) {
                for (ObjectError er : result.getAllErrors())
                    return ReturnUtil.Error(er.getDefaultMessage(), null, null);
            }
            mailService.saveOrUpdate(mail);
            return ReturnUtil.Success("操作成功", null, "/api/credit/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
    @RequestMapping(value = "/emailBooksave", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap emailBooksave( EmailBook emailBook, BindingResult result) {
        try {
            Semester semester = new Semester();
            semester.setId(emailBook.getSemesterId());
            emailBook.setSemester(semester);
            if(null != emailBook.getId()){
                emailBook.setUpdateTime(new Date());
                emailBook.setIsDelete(1);
                emailBookService.update(emailBook);
            }else{
                emailBook.setCreateTime(new Date());
                emailBook.setUpdateTime(new Date());
                emailBook.setIsDelete(1);
                emailBookService.update(emailBook);
            }
            return ReturnUtil.Success("操作成功", null, "/api/semester/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
