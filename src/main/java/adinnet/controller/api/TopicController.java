package adinnet.controller.api;

import com.adinnet.common.MenuTree;
import com.adinnet.repository.*;
import com.adinnet.service.CourseService;
import com.adinnet.service.ExaminTopicService;
import com.adinnet.utils.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 题目管理
 * @create 2018-09-26 10:23
 **/
@Controller
@RequestMapping("/api/topic")
public class TopicController {

    @Autowired
    private ExaminTopicService examinTopicService;
    @Autowired
    private CourseService courseService;
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "topic/index";
    }

    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(Integer id, Model model,Integer mode,Integer parentId) {
        String view = "";
        switch (mode){
            case 1 :
                ExaminTopic examinTopic = new ExaminTopic();
                if(null != id){
                    examinTopic = examinTopicService.getOneExaminTopic(id);
                }else {
                    examinTopic.setExaminId(parentId);
                }
                view = "topicfrom";
                model.addAttribute("examinTopic",examinTopic);
                break;
            case 2 :
                ExaminTopicAnswer examinTopicAnswer = new ExaminTopicAnswer();
                if(null != id){
                    examinTopicAnswer = examinTopicService.getOneExaminTopicAnswer(id);
                }else {
                    examinTopicAnswer.setTopicId(parentId);
                }
                view = "answerfrom";
                model.addAttribute("examinTopicAnswer",examinTopicAnswer);
                break;
        }

        return "topic/"+view;
    }
    @RequestMapping(value = "/coursefrom", method = {RequestMethod.GET})
    public String coursefrom( Model model) {
        return "topic/from";
    }

    @RequestMapping(value = "/menutree", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelMap comboTree(Integer id) {
        List<Course> courseList = courseService.queryByExam();
        List<Map<String, Object>> mapList = MenuTree.buildTree(courseList);
        return ReturnUtil.Success(null, mapList, null);
    }
    @RequestMapping(value = "/grant", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap grant(Integer[] courseIds) {
        try {
            examinTopicService.saveExamin(courseIds);
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Examin examin) {
        try{
        ModelMap map = new ModelMap();
        map.put("queryParam",examin);
            PageBean<Examin> pageBean = examinTopicService.pageList(examin);
            map.put("pageInfo",pageBean);
         return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }

    @RequestMapping(value = "/listByTopic", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap listByTopic(Integer courseId, Model model) {
        try{
        ModelMap map = new ModelMap();
        map.put("pageInfo",examinTopicService.listExaminTopic(courseId));
        return ReturnUtil.Success("加载成功", map, null);
      } catch (Exception e) {
        e.printStackTrace();
        return ReturnUtil.Error("加载失败", null, null);
      }
    }
    @RequestMapping(value = "/listByAnswer", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap listByAnswer(Integer topicId, Model model) {
        try{
        ModelMap map = new ModelMap();
        map.put("pageInfo",examinTopicService.listByAnswer(topicId));
        return ReturnUtil.Success("加载成功", map, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("加载失败", null, null);
        }
    }

    @RequestMapping(value = "/saveTopic", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap saveTopic(ExaminTopic examinTopic, Model model) {
        try{
            if(null == examinTopic.getId()){
                examinTopicService.saveExaminTopic(examinTopic);
            }else {
                examinTopicService.updateExaminTopic(examinTopic);
            }
        model.addAttribute("examinTopic",examinTopic);
        return ReturnUtil.Success("操作成功", null, "/api/topic/index");
       } catch (Exception e) {
        e.printStackTrace();
        return ReturnUtil.Error("操作失败", null, null);
       }
    }

    @RequestMapping(value = "/saveAnswer", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap saveAnswer(ExaminTopicAnswer examinTopicAnswer, Model model) {
        try{
        if(null == examinTopicAnswer.getId()){
            examinTopicService.saveExaminTopicAnswer(examinTopicAnswer);
        }else {
            examinTopicService.updateExaminTopicAnswer(examinTopicAnswer);
        }
            model.addAttribute("examinTopicAnswer",examinTopicAnswer);
        return ReturnUtil.Success("操作成功", null, "/api/topic/index");
    } catch (Exception e) {
        e.printStackTrace();
        return ReturnUtil.Error("操作失败", null, null);
    }
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(Integer id,Integer mode, Model model) {
        try{
            switch (mode){
                case 1 :
                    examinTopicService.delExamin(id);
                    break;
                case 2:
                    examinTopicService.delExaminTopic(id);
                    break;
                case 3:
                    examinTopicService.delExaminTopicAnswer(id);
                    break;
            }
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }
}
