package com.adinnet.service;

import com.adinnet.repository.Mail;

import java.util.List;

/**
 * @author wangren
 * @Description: 邮件发送
 * @create 2018-10-08 15:02
 **/
public interface MailService {

    public void saveOrUpdate(Mail mail);

    public List<Mail> selectAll();

}
