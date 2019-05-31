package com.adinnet.dao;

import com.adinnet.repository.CreaditBook;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @author wangren
 * @Description: 证书模板
 * @create 2018-10-09 15:16
 **/
public interface CreaditBookMapper extends Repositor<CreaditBook, Integer>,JpaSpecificationExecutor<CreaditBook> {

}
