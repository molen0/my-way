package com.adinnet.service.Impl;

import com.adinnet.dao.EmailBookMapper;
import com.adinnet.repository.EmailBook;
import com.adinnet.repository.Semester;
import com.adinnet.service.EmailBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangren
 * @Description: 群发邮件模板
 * @create 2018-12-13 11:58
 **/
@Service
public class EmailBookServicelmpl implements EmailBookService{

    @Autowired
    private EmailBookMapper emailBookMapper;
    @Override
    public List<EmailBook> selectBysemesterId(Integer semesterId) {
        EmailBook emailBook = new EmailBook();
        emailBook.setIsDelete(1);
        Semester semester = new Semester();
        semester.setId(semesterId);
        emailBook.setSemester(semester);
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("semester.id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
        //创建实例
        Example<EmailBook> ex = Example.of(emailBook);
        return emailBookMapper.findAll(ex);
    }

    @Override
    @Transactional
    public void save(EmailBook emailBook) {
        emailBookMapper.save(emailBook);
    }

    @Override
    @Transactional
    public void update(EmailBook emailBook) {
        emailBookMapper.saveAndFlush(emailBook);
    }

    @Override
    public List<EmailBook> select(String nowdate) {
//        EmailBook emailBook = new EmailBook();
//        emailBook.setIsDelete(1);
//        Semester semester = new Semester();
//        semester.setTime(nowdate);
//        semester.setIsDelete(1);
//        emailBook.setSemester(semester);
//        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
//                .withMatcher("semester.time", ExampleMatcher.GenericPropertyMatchers.contains())
//                .withMatcher("semester.isDelete", ExampleMatcher.GenericPropertyMatchers.exact())
//                .withMatcher("isDelete", ExampleMatcher.GenericPropertyMatchers.exact());
//        //创建实例
//        Example<EmailBook> ex = Example.of(emailBook);
        return emailBookMapper.emailBookList(nowdate);
    }
}
