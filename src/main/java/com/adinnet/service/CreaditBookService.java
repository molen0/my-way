package com.adinnet.service;

import com.adinnet.repository.CreaditBook;
import com.adinnet.repository.Semester;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * @author wangren
 * @Description: 证书模板
 * @create 2018-10-09 15:19
 **/
public interface CreaditBookService {

    public Page<CreaditBook> pageList(CreaditBook creaditBook);

    public void saveOrUpdate(CreaditBook creaditBook);

    public CreaditBook getOne(Integer id);

    public void  del(Integer id);

    public CreaditBook getBysemesterId(Integer semesterId);
}
