package com.adinnet.dao;

import com.adinnet.repository.Mail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangren
 * @Description: 邮件
 * @create 2018-10-08 15:01
 **/
public interface MailMapper extends Repositor<Mail, Integer>,JpaSpecificationExecutor<Mail> {
}
