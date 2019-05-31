package adinnet.utils;

import java.io.BufferedReader;
  import java.io.BufferedWriter;
 import java.io.File;
import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.OutputStream;
import java.io.OutputStreamWriter;
 import java.io.UnsupportedEncodingException;
 import java.util.Properties;

 import javax.activation.DataHandler;
 import javax.activation.DataSource;
 import javax.activation.FileDataSource;
 import javax.mail.BodyPart;
  import javax.mail.Message;
 import javax.mail.Multipart;
import javax.mail.Session;
 import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
  import javax.mail.internet.MimeBodyPart;
 import javax.mail.internet.MimeMessage;
 import javax.mail.internet.MimeMultipart;
/**
 * @author wangren
 * @Description: qq
 * @create 2018-12-20 17:48
 **/
public class qqEmails {


        private static String host = "smtp.qq.com"; // smtp服务器
        private static  String from = "3507835370@qq.com"; // 发件人地址
        private String to = "2756751661@qq.com"; // 收件人地址
        private String affix = ""; // 附件地址
        private String affixName = ""; // 附件名称
        private String user = ""; // 用户名
        private static String pwd = "mqxnhrbffdnvcjec"; // 密码
        private String subject = "测试"; // 邮件标题

        public void setAddress(String from, String to, String subject) {
            this.from = from;
            this.to = to;
            this.subject = subject;
        }

        public void setAffix(String affix, String affixName) {
            this.affix = affix;
            this.affixName = affixName;
        }

        public static void send(String to, String subject, String affix,String name,String content) {

            Properties props = new Properties();

            // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
            props.put("mail.smtp.host", host);
            // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", 465);
            props.put("mail.smtp.ssl.enable", true);
            // 用刚刚设置好的props对象构建一个session
            Session session = Session.getDefaultInstance(props);

            // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
            // 用（你可以在控制台（console)上看到发送邮件的过程）
            session.setDebug(true);

            // 用session为参数定义消息对象
            MimeMessage message = new MimeMessage(session);
            try {
                // 加载发件人地址
                message.setFrom(new InternetAddress(from));
                // 加载收件人地址
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                        to));
                // 加载标题
                message.setSubject(subject);

                // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
                Multipart multipart = new MimeMultipart();

                // 设置邮件的文本内容
                BodyPart contentPart = new MimeBodyPart();
                contentPart.setContent(content, "text/html; charset=utf-8");
                multipart.addBodyPart(contentPart);
               // contentPart.setText(content);
               // multipart.addBodyPart(contentPart);
                if(null != affix && !"".equals(affix)) {
                    // 添加附件
                    BodyPart messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(affix);
                    // 添加附件的内容
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    // 添加附件的标题
                    // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                    messageBodyPart.setFileName("=?GBK?B?"
                            + enc.encode(name.getBytes()) + "?=");
                    multipart.addBodyPart(messageBodyPart);
                }
                // 将multipart对象放到message中
                message.setContent(multipart);
                // 保存邮件
                message.saveChanges();
                // 发送邮件
                Transport transport = session.getTransport("smtp");
                // 连接服务器的邮箱
                transport.connect(host, "3507835370@qq.com", pwd);
                // 把邮件发送出去
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {

            qqEmails.send("2756751661@qq.com","测试","C:\\Users\\datang\\Desktop\\TB1wrmqs7voK1RjSZFDXXXY3pXa-209-75.png","certificate.png","<span style='color:red'>》》》这是原来的字体</span>  <strong>》》》这是加粗的字体</strong>  >>>这个是链接 <a href='http://baidu.com'>点击进入</a>");
        }

}
