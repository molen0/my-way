package com.adinnet.utils;


import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import com.sun.mail.util.MimeUtil;
/**
 * @author wangren
 * @Description: 111
 * @create 2018-11-01 15:19
 **/
public class sendMail {
    /**
     * 创建邮件信息
     * @param session
     * @param fromAccount
     * @param toAccount
     * @param sourcePath xml文件目录   e.g. xml
     * @param zipPath   zip文件目录  e.g. zip/person.zip
     */
    public static void CreateMessage(final Session session, final String fromAccount, final String toAccount,final String sourcePath,final String zipPath){
        try{
            final String subjectStr="圣诞节快乐";//主题
            final StringBuffer contentStr=new StringBuffer();//内容
            contentStr.append("<h2>Dear Friends,</h2><br/>");
            contentStr.append("Christmas is coming up soon. <br/> Wish you lots of love, joy &happiness. happy christmas.");
            contentStr.append("<h3>Regards,</h3>").append("<h3>ZHBIT College</h3>");

            //创建默认的 MimeMessage 对象
            final MimeMessage message = new MimeMessage(session);
            //Set From: 头部头字段
            message.setFrom(new InternetAddress(fromAccount));
            //Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toAccount));
            //Set Subject: 头部头字段
            message.setSubject(subjectStr);
            //创建消息部分
            final BodyPart messageBodyPart = new MimeBodyPart();
            //消息
            messageBodyPart.setContent(contentStr.toString(),"text/html;charset=UTF-8");
            //创建多重消息
            final Multipart multipart = new MimeMultipart();
            //设置文本消息部分
            multipart.addBodyPart(messageBodyPart);
            //为邮件添加多个附件
            MimeBodyPart attachment = null;
//            final File source = new File(sourcePath);
//            if (!source.exists()) {
//                System.out.println(sourcePath + " not exists");
//                return;
//            }
            //final File[] files = source.listFiles();
           // for (final File f : files) {
                attachment = new MimeBodyPart();
              //  final String filePath =f.getPath();
                //根据附件文件创建文件数据源
                final DataSource ds1 = new FileDataSource("D:/workspace/yunque/03_Code/hibernate-jpa/target/classes/templates/2/2_1.png");
                attachment.setDataHandler(new DataHandler(ds1));
                //为附件设置文件名
                attachment.setFileName(ds1.getName());
                multipart.addBodyPart(attachment);
           // }

            //添加zip附件
            attachment = new MimeBodyPart();
            //根据附件文件创建文件数据源
            final DataSource ds = new FileDataSource(zipPath);
            attachment.setDataHandler(new DataHandler(ds));
            //为附件设置文件名
            attachment.setFileName(ds.getName());
            multipart.addBodyPart(attachment);

            // 发送完整消息
            message.setContent(multipart);
            // 发送消息
            Transport.send(message);

        }catch (final MessagingException mex) {
            mex.printStackTrace();
        }
    }


    /**
     * 将源文件目录下的所有文件打包成zip文件
     * @param sourceFilePath  e.g. xml
     * @param zipFilePath   e.g. zip
     * @param fileName   e.g. person
     * @return 返回生成的zip文件目录  e.g. zip/person.zip
     */
    public static String tozip(final String sourceFilePath, final String zipFilePath,
                               final String fileName) {
        final File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        final String createZipPath=zipFilePath+ "/" + fileName+ ".zip";

        if(!sourceFile.exists()){
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在");
        } else {
            try {
                final File zipFile = new File(createZipPath);
                final File[] sourceFiles = sourceFile.listFiles();
                if(null == sourceFiles || sourceFiles.length < 1) {
                    System.out.println("待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩");
                }else{
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    final byte[] bufs = new byte[1024*10];
                    for(int i=0;i<sourceFiles.length;i++) {
                        // 创建ZIP实体,并添加进压缩包
                        final ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        // 读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(sourceFiles[i]);
                        bis = new BufferedInputStream(fis,1024*10);
                        int read = 0;
                        while((read=bis.read(bufs, 0, 1024*10)) != -1) {
                            zos.write(bufs, 0, read);
                        }
                    }
                }

            } catch (final FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (final IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != zos) {
                        zos.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return createZipPath;
    }


    public static void main(final String[] args) {
        //收件人电子邮箱
        final String toAccount = "2756751661@qq.com";
        //发件人的 邮箱 和 密码
        final String fromAccount = "Dr_study@163.com";
        final String fromPassword = "Jky888";
        //指定发送邮件的主机
        final String host = "smtp.163.com";

        //创建参数配置, 获取系统属性
        final Properties properties = System.getProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");

        //根据配置创建会话对象,获取默认session对象
        final Session session = Session.getDefaultInstance(properties,new Authenticator(){
            @Override
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(fromAccount, fromPassword); //发件人邮件用户名、密码
            }
        });
        session.setDebug(true);

        //final String xmlPath="xml";
        //final String zipPath=tozip(xmlPath,"zip","person");
        CreateMessage(session,fromAccount,toAccount,"","");
    }
}
