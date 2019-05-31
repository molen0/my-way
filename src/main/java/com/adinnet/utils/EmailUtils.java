package com.adinnet.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.*;

/**
 * @author wangren
 * @Description: 邮箱带附件
 * @create 2018-10-09 11:28
 **/
@Slf4j
public class EmailUtils {

    private static String user = "Dr_study@163.com";  //用户名
    private static String pwd = "Jky888";   //密码
    private static String host = "smtp.163.com";  //smtp服务器

    public static void main(String[] agrs){
        send("2756751661@qq.com","ceshi","ss","D:\\workspace\\yunque\\03_Code\\hibernate-jpa\\target\\classes\\templates\\2\\xfzs.jpg",new Date(),"");
    }
    public static void send(String fromUser, String subject, String content, String filepath, Date sendDate,String pngUrl) {
        Properties props = new Properties();
        //设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        //需要经过授权，也就是有户名和密码的校验，这样才能通过验证
        props.put("mail.smtp.auth", "true");
        //用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);

        //有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        //用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);

        //用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try{
            //加载发件人地址
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.CC,new InternetAddress(user));
            //加载收件人地址
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(fromUser));
            //加载标题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();


            //设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            //contentPart.setText(content);
            contentPart.setContent(content, "text/html; charset=utf-8");
            multipart.addBodyPart(contentPart);
            //添加附件
            // BodyPart messageBodyPart= new MimeBodyPart();
            // DataSource source = new FileDataSource(affix);
            //添加附件的内容
            //  messageBodyPart.setDataHandler(new DataHandler(source));
            //添加附件的标题
            //这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            // messageBodyPart.setFileName("=?GBK?B?"+enc.encode(affixName.getBytes())+"?=");
            // multipart.addBodyPart(messageBodyPart);
            // multipart.addBodyPart(messageBodyPart);
            if(null != filepath && !"".equals(filepath)) {
                BodyPart messageBodyPart = new MimeBodyPart();
                File file = new File(filepath);
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
                multipart.addBodyPart(messageBodyPart);
            }
            //将multipart对象放到message中
            message.setContent(multipart);
            message.setSentDate(new Date());
            //保存邮件
            message.saveChanges();
            //   发送邮件
            Transport transport = session.getTransport("smtp");
            //连接服务器的邮箱
            transport.connect(host, user, pwd);
            //把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            if(null != filepath && !"".equals(filepath)) {
                File file = new File(filepath);
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        System.out.println("删除单个文件" + filepath + "成功！");
                    }
                }
            }
            if(null != pngUrl && !"".equals(pngUrl)) {
                File file = new File(pngUrl);
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        System.out.println("删除单个文件" + pngUrl + "成功！");
                    }
                }
            }
//            for(String fileName : stringList){
//                File file = new File(fileName);
//                if (file.exists() && file.isFile()) {
//                    if (file.delete()) {
//                        System.out.println("删除单个文件" + fileName + "成功！");
//                    }
//                }
//            }
//            Iterator its = map.entrySet().iterator();
//            while (its.hasNext()) {
//                Map.Entry entry = (Map.Entry) it.next();
//                String value = (String)entry.getValue();
//                File file = new File(value);
//                if (file.exists()) {
//                    if (file.isFile())
//                         deleteFile(value);
//                    else {
//                         deleteDirectory(value);
//                    }
//                }
//            }
        }catch(Exception e){
            log.error("163邮箱发送失败，邮件被拦截了！");
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

}
