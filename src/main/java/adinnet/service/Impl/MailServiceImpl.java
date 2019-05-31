package adinnet.service.Impl;

import com.adinnet.dao.CourseMapper;
import com.adinnet.dao.DoctorMapper;
import com.adinnet.dao.MailMapper;
import com.adinnet.dao.SemesterMapper;
import com.adinnet.repository.*;
import com.adinnet.service.MailService;
import com.adinnet.utils.EmailUtils;
import com.adinnet.utils.GetFileUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 邮件发送
 * @create 2018-10-08 15:03
 **/
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private MailMapper mailMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private SemesterMapper semesterMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Override
    @Transactional
    public void saveOrUpdate(Mail mail) {
        Doctor doctor = doctorMapper.getOne(mail.getUserId());
        Semester semester = semesterMapper.queryById(mail.getSemesterId());
        if(null  != mail.getId()){
            mail.setIsDelete(1);
            mail.setUpdateTime(new Date());
            mail.setUserMail(doctor.getEmail());
            mail.setUserName(doctor.getName());
            mailMapper.saveAndFlush(mail);
        }else{
            mail.setIsDelete(1);
            mail.setUpdateTime(new Date());
            mail.setCreateTime(new Date());
            mail.setUserMail(doctor.getEmail());
            mail.setUserName(doctor.getName());
            mail.setHandBookUrl(semester.getHandBookUrl());
            mailMapper.save(mail);
            //sendMail(mail);
        }
    }

    @Override
    public List<Mail> selectAll() {
        Mail mail = new Mail();
        mail.setIsDelete(1);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<Mail> ex = Example.of(mail, matcher);
        return mailMapper.findAll(ex);
    }

//    @Async
//    public void sendMail(Mail mail){
//       String url = this.getClass().getResource("/").getPath();
//       String[] fjs = mail.getFj().split(",");
//        Map<String,String> map = new HashedMap();
//        Doctor doctor = doctorMapper.getOne(mail.getUserId());
//        String xfpdf = "http://pfapsoyog.bkt.clouddn.com/pdfPath/eb485123-9755-4beb-8b84-73aaece9a0dc.pdf";
//        String dapdf = "http://pfapsoyog.bkt.clouddn.com/pdfPath/eb485123-9755-4beb-8b84-73aaece9a0dc.pdf";
//        String sp = "http://pfapsoyog.bkt.clouddn.com/study/bc705fa4-d8e2-49d0-a4f9-539b4087bac9.mp4";
//        String dz = "http://pfapsoyog.bkt.clouddn.com/pdfPath/eb485123-9755-4beb-8b84-73aaece9a0dc.pdf";
//        for(int i=0;i<fjs.length;i++){
//           if("0".equals(fjs[i])){
//               String xfpdfs = xfpdf.substring(xfpdf.lastIndexOf("."));
//               File file = new File(url+"/xfpdf"+xfpdfs);
//               GetFileUtils.downloadFile(xfpdf,url+"/xfpdf"+xfpdfs);
//               map.put("学分证书",url+"/xfpdf"+xfpdfs);
//           }else if("1".equals(fjs[i])){
//               String dapdfs = xfpdf.substring(dapdf.lastIndexOf("."));
//               File file = new File(url+"/dapdf"+dapdfs);
//               GetFileUtils.downloadFile(dapdf,url+"/dapdf"+dapdfs);
//
//               String sps = xfpdf.substring(sp.lastIndexOf("."));
//               File file1 = new File(url+"/sp"+sps);
//               GetFileUtils.downloadFile(sp,url+"/sp"+sps);
//               map.put("参考答案ppt",url+"/dapdf"+dapdfs);
//               map.put("参考答案视频",url+"/sp"+sps);
//           }else if("2".equals(fjs[i])){
//               String dzs = xfpdf.substring(dz.lastIndexOf("."));
//               File file = new File(url+"/xfpdf"+dzs);
//               GetFileUtils.downloadFile(dz,url+"/dz"+dzs);
//               map.put("电子手册",url+"/xfpdf"+dzs);
//           }
//       }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//        try {
//            EmailUtils.send(doctor.getEmail(),mail.getTittle(),mail.getContent(),map,sdf.parse(mail.getSendDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
