package adinnet.job;

import com.adinnet.dao.*;
import com.adinnet.repository.*;
import com.adinnet.service.*;
import com.adinnet.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wangren
 * @Description: 定时任务
 * @create 2018-10-08 16:45
 **/
@Slf4j
@Component
public class ScheduledService {

    @Autowired
    private MailMapper mailMapper;

    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ExaminUserService examinUserService;

    @Autowired
    private CreaditBookService creaditBookService;

    @Autowired
    private EmailBookService emailBookService;
    @Autowired
    private ExamAnswerResourceService examAnswerResourceService;
    @Autowired
    private SemesterDoctorService semesterDoctorService;
    @Autowired
    private SemesterService semesterService;
    @Scheduled(cron = "0 */1 * * * ?")
    public void scheduled() throws Exception {
        log.info("定时任务：发送邮件");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dat = sdf.format(new Date());
        Mail mail = new Mail();
        mail.setIsDelete(1);
        mail.setSendDate(dat);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("sendDate", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<Mail> ex = Example.of(mail);
        List<Mail> list =  mailMapper.findAll(ex);
        String pdfpath = "";
        for (int i=0;i<list.size();i++){
            String pngUrl = null;
            mail = list.get(i);
            String[] fjs = null;
            if(null != mail.getFj()){
                fjs = mail.getFj().split(",");
            }else{
                fjs = new String[0];
            }
            for(int j=0;j<fjs.length;j++){
                if("0".equals(fjs[j])){
                    //String url = this.getClass().getResource("/").getPath();
                    //url = url.substring(1);
                    String url = HtmlForPdfUtils.MPURL;
                    List<ExaminUser> examinUserList = examinUserService.examinUserList(mail.getUserId(),mail.getSemesterId());
                    Integer type = 0;
                    if(examinUserList.size()>0 && "1".equals(examinUserList.get(0).getDoctor().getProperty().toString())){
                        type =1;
                    }
                    CreaditBook creaditBook = creaditBookService.getBysemesterId(type);
                    Double add = 0.0;
                    for(ExaminUser examinUser : examinUserList){
                        if("1".equals(examinUser.getExamin().getCourse().getCourseType().toString())){
                            add = add + Double.parseDouble(examinUser.getCredit()) ;
                        }
                    }
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd号");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    String photoUrl1 = "";
                    String headlines = "";
                    String semesterTitle = "";
                    String name = mail.getUserName();
                    String content = "";
                    String credit = (add+"").substring(0,3);
                    String hospital = "";
                    String fazhengdate = "2018年10月12号";
                    if(examinUserList.size()>0){
                        Semester semester = semesterService.queryById(mail.getSemesterId());
                        fazhengdate = sdf1.format(sdf2.parse(semester.getTime()));
                    }
                    if(null != creaditBook){
                        if(null != creaditBook.getChapter1() && !"".equals(creaditBook.getChapter1())){
                            log.info("邮件印章文件1是否存在------------");
                           // String time = creaditBook.getChapter1().substring(creaditBook.getChapter1().lastIndexOf("/")+1);
//                            photoUrl1 = "<div class=\"seal_num\">\n" +
//                                        "<img class=\"img-responsive\" src=\".http://study.wdjky.com/yinzhang/2e5718f6-9e90-434a-932d-5a6fc77f19e3.png\" />\n" +
//                                        "</div>";
                            photoUrl1 = "<div class=\"seal_num\">\n" +
                                        "<img class=\"img-responsive\" src=\"" + creaditBook.getChapter1() + "\" />\n" +
                                        "</div>";
//                            File file = new File(url+"/static/xfzs/images/"+time);
//                            if(!file.exists()){
//                                log.info("邮件印章文件1开始下载------------");
//                                ImageDownload.downloadPicture(creaditBook.getChapter1(),url+"/static/xfzs/images/"+time);
//                                log.info("邮件印章文件1下载完成--------------");
//                            }
                        }
                        if(null != creaditBook.getChapter2() && !"".equals(creaditBook.getChapter2())){
                            log.info("邮件印章文件2是否存在------------");
                            //String time = creaditBook.getChapter2().substring(creaditBook.getChapter2().lastIndexOf("/")+1);
//                            photoUrl1 += "<div class=\"seal_num adjust\">\n" +
//                                    "<img class=\"img-responsive\" src=\"./images/"+time+"\" />\n" +
//                                    "</div>";
                            photoUrl1 += "<div class=\"seal_num adjust\">\n" +
                                    "<img class=\"img-responsive\" src=\""+creaditBook.getChapter2()+"\" />\n" +
                                    "</div>";
//                            File file = new File(url+"/static/xfzs/images/"+time);
//                            if(!file.exists()){
//                                log.info("邮件印章文件2开始下载------------");
//                                ImageDownload.downloadPicture(creaditBook.getChapter2(),url+"/static/xfzs/images/"+time);
//                                log.info("邮件印章文件2下载完成--------------");
//                            }
                        }
//                        if(null != creaditBook.getHospital1() && !"".equals(creaditBook.getHospital1())){
//                            hospital = "<p>"+creaditBook.getHospital1()+"</p>";
//                        }
//                        if(null != creaditBook.getHospital2() && !"".equals(creaditBook.getHospital2())){
//                            hospital += "<p>"+creaditBook.getHospital2()+"</p>";
//                        }
                        if(null != creaditBook.getHeadlines() && !"".equals(creaditBook.getHeadlines())){
                            headlines = creaditBook.getHeadlines();
                        }
                        if(null != creaditBook.getSemesterTitle() && !"".equals(creaditBook.getSemesterTitle())){
                            semesterTitle = creaditBook.getSemesterTitle();
                        }
                        if(null != creaditBook.getProveCon() && !"".equals(creaditBook.getProveCon())){
                            content = creaditBook.getProveCon();
                        }
                    }
                    String htmlUrl = craditRepace(content,url,headlines,semesterTitle,name,credit,hospital,photoUrl1,fazhengdate);
                    Thread.sleep(5000);
                    String pdfUrl =  craditPdf(url,htmlUrl);
                    Thread.sleep(5000);
                    pngUrl = PdftoimgUtils.pdf2Image(pdfUrl,url,300);
                    Thread.sleep(5000);
//                    String fileName = System.currentTimeMillis()+"";
//                    String zipUrl = pdfUrl.substring(0,pdfUrl.lastIndexOf("/"))+fileName+".zip";
//                    ZIPUtil.compress(pngUrl,zipUrl);
                    pdfpath = pngUrl;
                   // map.put("学分证书.png",pngUrl);
                }else if("2".equals(fjs[j])){
                    mail.setContent(mail.getContent()+"<br/>点击查看：<a href='"+mail.getHandBookUrl()+"'> 电子手册</a>");
                }
            }
            try {
                qqEmails.send(mail.getUserMail(),mail.getTittle(),pdfpath,"certificate.png",mail.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Scheduled(cron = "0 0 2 * * ?")
    //@Scheduled(cron = "0 0 15 * * ?")
   // @Scheduled(cron = "0 */1 * * * ?")
    public void emailBook()throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd号");
        String dateStr = sdf.format(new Date());
        List<EmailBook> emailBooks = emailBookService.select(dateStr);
        for(EmailBook emailBook : emailBooks){
            List<SemesterDoctor> semesterDoctorList = semesterDoctorService.querysemesterId(emailBook.getSemester().getId());
            for(SemesterDoctor semesterDoctor : semesterDoctorList){
                Doctor doctor =doctorService.queryById(semesterDoctor.getDoctorId());
                String pdfpath = null;
                List<ExaminUser> examinUserList = examinUserService.examinUserList(semesterDoctor.getDoctorId(),semesterDoctor.getSemesterId());

//                    String url = this.getClass().getResource("/").getPath();
//                    url = url.substring(1);
                    String url = HtmlForPdfUtils.MPURL;
                    Integer type = 0;
                    if(examinUserList.size()>0 && "1".equals(doctor.getProperty())){
                        type =1;
                    }
                    CreaditBook creaditBook = creaditBookService.getBysemesterId(type);
                    Double add = 0.0;
                    for(ExaminUser examinUser : examinUserList){
                        if("1".equals(examinUser.getExamin().getCourse().getCourseType().toString())){
                            add = add + Double.parseDouble(examinUser.getCredit()) ;
                        }
                    }
                if(add>=0.5){
                    String photoUrl1 = "";
                    String headlines = "";
                    String semesterTitle = "";
                    String name = doctor.getName();
                    String content = "";
                    String credit = (add+"").substring(0,3);
                    String hospital = "";
                    String fazhengdate = sdf1.format(sdf.parse(dateStr));
                    if(null != creaditBook){
                        if(null != creaditBook.getChapter1() && !"".equals(creaditBook.getChapter1())){
                            log.info("邮件印章文件1是否存在------------");
                            photoUrl1 = "<div class=\"seal_num\">\n" +
                                    "<img class=\"img-responsive\" src=\"" + creaditBook.getChapter1() + "\" />\n" +
                                    "</div>";
                        }
                        if(null != creaditBook.getChapter2() && !"".equals(creaditBook.getChapter2())){
                            log.info("邮件印章文件2是否存在------------");
                            photoUrl1 += "<div class=\"seal_num adjust\">\n" +
                                    "<img class=\"img-responsive\" src=\""+creaditBook.getChapter2()+"\" />\n" +
                                    "</div>";
                        }
                        if(null != creaditBook.getHospital1() && !"".equals(creaditBook.getHospital1())){
                            hospital = "<p>"+creaditBook.getHospital1()+"</p>";
                        }
                        if(null != creaditBook.getHospital2() && !"".equals(creaditBook.getHospital2())){
                            hospital += "<p>"+creaditBook.getHospital2()+"</p>";
                        }
                        if(null != creaditBook.getHeadlines() && !"".equals(creaditBook.getHeadlines())){
                            headlines = creaditBook.getHeadlines();
                        }
                        if(null != creaditBook.getSemesterTitle() && !"".equals(creaditBook.getSemesterTitle())){
                            semesterTitle = creaditBook.getSemesterTitle();
                        }
                        if(null != creaditBook.getProveCon() && !"".equals(creaditBook.getProveCon())){
                            content = creaditBook.getProveCon();
                        }
                    }
                    String htmlUrl = craditRepace(content,url,headlines,semesterTitle,name,credit,hospital,photoUrl1,fazhengdate);
                    Thread.sleep(5000);
                    String pdfUrl =  craditPdf(url,htmlUrl);
                    Thread.sleep(5000);
                    String pngUrl = PdftoimgUtils.pdf2Image(pdfUrl,url,300);
                    Thread.sleep(5000);
                    pdfpath = pngUrl;
                }
               // EmailUtils.send(doctor.getEmail(),emailBook.getTittle(), emailBook.getContent()+"<br/>点击查看：<a href='"+emailBook.getSemester().getHandBookUrl()+"'> 电子手册</a>",pdfpath,null,pdfpath);
                qqEmails.send(doctor.getEmail(),emailBook.getTittle(),pdfpath,"certificate.png",emailBook.getContent()+"<br/>点击查看：<a href='"+emailBook.getSemester().getHandBookUrl()+"'> 电子手册</a>");
            }
        }
    }

    public static String craditPdf(String baseUrl,String htmlUrl){
        String url2 = "";
        try {
            log.info("开始pdf生成------------");
             url2 = baseUrl+"/xf.pdf";
             File file = new File(url2);
             if(!file.exists()){
                 file.createNewFile();
             }
             HtmlForPdfUtils.pdfTest(htmlUrl,url2);
            log.info("完成pdf生成------------");
            return url2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url2;
    }

    public static String craditRepace(String content,String basePath,String headlines,String semesterTitle,String name,String credit,String hospital,String img,String fazhengdate){
        try {
            log.info("开始生成模板view_credits------------");
            String filePath = basePath+ "/view_credits.html";
        //下面这个方法来获取文件流我还没弄明白
        //InputStream is= Temp.class.getClassLoader().getResourceAsStream("whtml");
        String templateContent = "";
        FileInputStream fileinputstream = new FileInputStream(filePath);// 读取模板文件
        //下面四行：获得输入流的长度，然后建一个该长度的数组，然后把输入流中的数据以字节的形式读入到数组中，然后关闭流
        int lenght = fileinputstream.available();
        byte bytes[] = new byte[lenght];
        fileinputstream.read(bytes);
        fileinputstream.close();
        //通过使用平台的默认字符集解码指定的 byte 数组，构造一个新的 String。然后利用字符串的replaceAll()方法进行指定字符的替换
        //此处除了这种方法之外，应该还可以使用表达式语言${}的方法来进行。
        templateContent = new String(bytes);
        //System.out.print(templateContent);
       // templateContent = templateContent.replaceAll("###title###", title);
        templateContent = templateContent.replaceAll("###headlines###", headlines);
            templateContent = templateContent.replaceAll("###semesterTitle###", semesterTitle);
            templateContent = templateContent.replaceAll("###name###", name);
            templateContent = templateContent.replaceAll("###content###", content);
            templateContent = templateContent.replaceAll("###credit###", credit);
            templateContent = templateContent.replaceAll("###hospital###", hospital);
            templateContent = templateContent.replaceAll("###img###", img);
            templateContent = templateContent.replaceAll("###fazhengdate###", fazhengdate);
      //  templateContent = templateContent.replaceAll("###author###", editer);
        //使用平台的默认字符集将此 String 编码为 byte 序列，并将结果存储到一个新的 byte 数组中。
        byte tag_bytes[] = templateContent.getBytes();
        // System.out.print(templateContent);
        // 根据时间得文件名。Calendar 的 getInstance 方法返回一个 Calendar 对象，其日历字段已由当前日期和时间初始化
        Calendar calendar = Calendar.getInstance();
        //valueOf()方法进行类型的转换，转换成String型
        String fileame = String.valueOf(123) + ".html";
        fileame = basePath+"/" + fileame;// 生成的html文件保存路径。
        // FileOutputStream fileoutputstream = new FileOutputStream(fileame);// 建立文件输出流
        OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(fileame), "utf-8");
        oStreamWriter.write(templateContent);
        oStreamWriter.close();
            log.info("结束生成模板view_credits------------");
        return fileame;
    } catch (Exception e) {
        System.out.print(e.toString());
    }
        return "";
    }
}
