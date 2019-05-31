package com.adinnet.service;/**
 * @Description: 群发邮件模板
 * @author wangren
 * @create 2018-12-13 11:57
 **/

import com.adinnet.repository.EmailBook;

import java.util.List;

/**
 * @Description: 群发邮件模板
 * @author wangren
 * @create 2018-12-13 11:57
 **/
public interface EmailBookService {

    public List<EmailBook> selectBysemesterId(Integer semesterId);

    public void save(EmailBook emailBook);

    public void update(EmailBook emailBook);

    public List<EmailBook> select(String nowdate);
}
