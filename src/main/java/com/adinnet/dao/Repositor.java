package com.adinnet.dao;

import com.adinnet.repository.Page1;
import com.adinnet.repository.PageBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 分页工具类
 * @create 2018-09-26 15:37
 **/

@NoRepositoryBean
public interface Repositor<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * 非原生分页查询
     * @param page
     * @return
     */
    public PageBean<T> findPage(PageBean<T> page, Object... params);

    public List<T> qryNamedQuery(String nativeName, Object... params);

    /**
     * 原生分页查询
     * @param page
     * @return
     */
    public PageBean<T> findNatPage(PageBean<T> page, Object... params);

    public PageBean<T> findNatPage(PageBean<T> page, String mapping, Object... params) ;

    public List<Map<String,Object>> findNatPage1(PageBean<T> page, Object... params);

    public Page1<List<Map<String,Object>>> findNatPage2(Page1<T> page, Object... params);
    /**
     * 修改
     * @param hql
     * @return
     */
    public Integer updateBean(String hql, Object... params);

    public Integer delBean(String hql, Object... params);

    public List<T> findNatList(String hql, Object... params);

    public Long countHql(String hql, Object... params) ;

    public PageBean<T> pageHql(String hql, Integer pageNum, Integer pageSize, Object... params);
}
