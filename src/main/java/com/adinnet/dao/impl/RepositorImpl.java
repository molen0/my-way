package com.adinnet.dao.impl;

import com.adinnet.dao.Repositor;
import com.adinnet.repository.Page1;
import com.adinnet.repository.PageBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 分页工具类实现
 * @create 2018-09-26 15:40
 **/
public class RepositorImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, Serializable>
        implements Repositor<T, Serializable> {

    private final EntityManager entityManager;
    private final Class<T> clas;

    public RepositorImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
        this.clas=domainClass;
    }
    @Override
    public List<T> qryNamedQuery(String nativeName,Object... params) {
        Query q = entityManager.createNamedQuery(nativeName);
        setQueryParams(q, params);
        List<T> t = q.getResultList();
        return t;
    }


    public void setQueryParams(Query query, Object... params) {
        try {
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    query.setParameter(i + 1, params[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PageBean<T> findPage(PageBean<T> page,Object... params) {
        StringBuffer stb = new StringBuffer();
        stb.append(page.getHql());
        if (null != page.getSortName() && !"".equals(page.getSortName())){
            stb.append("order by "+ page.getSortName());
        }
        Query query = entityManager.createQuery(stb.toString(),clas);
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        if(null != page.getPageNum() && null != page.getPageSize()){
            limitDatas(page.getPageNum(),page.getPageSize(),query);
        }
        page.setDatas(query.getResultList());
        if(null != page.getCountHql() && !"".equals(page.getCountHql())){
            page.setTotalCount(countHql(page.getCountHql(),params));
        }else {
            page.setTotalCount(getHCount(page.getHql(),params));
        }
        return page;
    }
    public PageBean<T> findNatPage( PageBean<T> page,Object... params) {
        StringBuffer stb = new StringBuffer();
        stb.append(page.getHql());

        if (null != page.getSortName() && !"".equals(page.getSortName())){
            stb.append(" order by "+ page.getSortName());
        }
        Query query = entityManager.createNativeQuery(stb.toString(), clas);
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        Long totalCount = (long) -1;
        if(null != page.getPageNum() && null != page.getPageSize()){
            limitDatas(page.getPageNum(),page.getPageSize(),query);
        }
        if(null != page.getCountHql() && !"".equals(page.getCountHql())){
            page.setTotalCount(countHql(page.getCountHql(),params));
        }else {
            page.setTotalCount(getHCount(page.getHql(),params));
        }
        page.setDatas(query.getResultList());
        return page;
    }
    public List<Map<String,Object>> findNatPage1( PageBean<T> page,Object... params) {
        StringBuffer stb = new StringBuffer();
        stb.append(page.getHql());

        if (null != page.getSortName() && !"".equals(page.getSortName())){
            stb.append(" order by "+ page.getSortName());
        }
        Query query = entityManager.createNativeQuery(stb.toString());
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        Long totalCount = (long) -1;
        if(null != page.getPageNum() && null != page.getPageSize()){
            limitDatas(page.getPageNum(),page.getPageSize(),query);
        }
        if(null != page.getCountHql() && !"".equals(page.getCountHql())){
            page.setTotalCount(countHql(page.getCountHql(),params));
        }else {
            page.setTotalCount(getHCount(page.getHql(),params));
        }
        return query.getResultList();
    }

    public Page1<List<Map<String,Object>>> findNatPage2( Page1<T> page,Object... params) {
        StringBuffer stb = new StringBuffer();
        stb.append(page.getHql());

        if (null != page.getSortName() && !"".equals(page.getSortName())){
            stb.append(" order by "+ page.getSortName());
        }
        Query query = entityManager.createNativeQuery(stb.toString());
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        Long totalCount = (long) -1;
        if(null != page.getPageNum() && null != page.getPageSize()){
            limitDatas(page.getPageNum(),page.getPageSize(),query);
        }
        if(null != page.getCountHql() && !"".equals(page.getCountHql())){
            page.setTotalCount(countHql(page.getCountHql(),params));
        }else {
            page.setTotalCount(getHCount(page.getHql(),params));
        }
        Page1<List<Map<String,Object>>> page1 = new  Page1<List<Map<String,Object>>>();
        page1.setTotalCount(page.getTotalCount());
        page1.setDatas(query.getResultList());
        return page1;
    }

    public PageBean<T> findNatPage( PageBean<T> page,String mapping,Object... params) {
        StringBuffer stb = new StringBuffer();
        stb.append(page.getHql());

        if (null != page.getSortName() && !"".equals(page.getSortName())){
            stb.append(" order by "+ page.getSortName());
        }
        Query query = entityManager.createNativeQuery(stb.toString(), "customerAndCreditCardMapping");
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        Long totalCount = (long) -1;
        if(null != page.getPageNum() && null != page.getPageSize()){
            limitDatas(page.getPageNum(),page.getPageSize(),query);
        }
        if(null != page.getCountHql() && !"".equals(page.getCountHql())){
            page.setTotalCount(countHql(page.getCountHql(),params));
        }else {
            page.setTotalCount(getHCount(page.getHql(),params));
        }
        page.setDatas(query.getResultList());
        return page;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateBean(String hql, Object... params) {
        Query query = entityManager.createNativeQuery(hql);
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        int result = query.executeUpdate(); //影响的记录数
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer delBean( String hql, Object... params){
        Query query = entityManager.createNativeQuery(hql);
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        int result = query.executeUpdate(); //影响的记录数
        return result;
    }

    @Override
    public List<T> findNatList(String hql, Object... params) {
        Query query = entityManager.createNativeQuery(hql);
        for(int i=0;i<params.length;i++){
            query.setParameter(i+1,params[i]);
        }
        return query.getResultList();
    }

    @Override
    public PageBean<T> pageHql(String hql, Integer pageNum, Integer pageSize, Object... params) {

        PageBean<T> pageBean = new PageBean<>();
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        Map<String, Object> map = getHSql(hql);
        String jpaSql = (String) map.get("sql");
        Integer count = (Integer) map.get("count");
        Long totalCount = getHCount(jpaSql, count, params);
        if(totalCount==null||totalCount==0){
            pageBean.setTotalCount(0L);
            return pageBean;
        }
        pageBean.setTotalCount(totalCount);

        //获取数据
        Query query = entityManager.createNativeQuery(jpaSql,clas);
        if (params != null && params.length > 0) {
            int length = params.length;
            for (int i = 0; i < length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        limitDatas(pageNum, pageSize, query);
        List<T> records =  query.getResultList();
        pageBean.setDatas(records);
        return pageBean;
    }
    private void limitDatas(Integer pageNum, Integer pageSize, Query query) {
        pageNum = (pageNum/pageSize)+1;
        if(pageNum!=null&&pageSize!=null){
            if(pageNum<=0)
                pageNum=1;
            query.setFirstResult((pageNum-1)*pageSize);
            query.setMaxResults(pageSize);
        }
    }

    private Long getHCount(String hql, Integer count, Object... params) {
        //执行count操作
        boolean flag = hql.startsWith("select");
        int end = hql.lastIndexOf("order by");
        if(end<=0){
            end = hql.lastIndexOf("ORDER BY");
        }
        String tempHql =hql;
        if(flag) {
            if (end > 0) {
                tempHql = tempHql.substring(0, end);
            }
            tempHql= "select count(1) from ("+tempHql+") as temp";
        }else{
            tempHql="select count(1) "+tempHql;
        }
        return countHql(tempHql,params);
    }

    private Long getHCount(String hql, Object... params) {
        //执行count操作
        boolean flag = hql.startsWith("select");
        int end = hql.lastIndexOf("order by");
        if(end<=0){
            end = hql.lastIndexOf("ORDER BY");
        }
        String tempHql =hql;
        if(flag) {
            if (end > 0) {
                tempHql = tempHql.substring(0, end);
            }
            tempHql= "select count(1) from ("+tempHql+") as temp";
        }else{
            tempHql="select count(1) "+tempHql;
        }
        return countHql(tempHql,params);
    }

    @Override
    public Long countHql(String hql, Object... params) {
        Query query = createHqlQuery(hql,params);
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    /**
     * 生成Hql可执行的query对象
     * @param hql
     * @param params
     * @return
     */
    private Query createHqlQuery(String hql, Object[] params) {
        Map<String, Object> map = getHSql(hql);
        String jpaHql = (String) map.get("sql");
        int count = (int) map.get("count");
        Query query = entityManager.createNativeQuery(jpaHql);
        setQueryParameters(count,query,params);
        return query;
    }

    private Map<String, Object> getHSql(String sql) {
        sql += " ";
        int count = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("sql", sql);
        String[] datas = sql.split("\\?");
        int length = datas.length;
        if (length == 1)
            return map;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - 1; i++) {
            count++;
            sb.append(datas[i]).append("?" + count).append(" ");
        }
        sb.append(datas[length-1]);
        map.put("count", count);
        map.put("sql", sb.toString());
        return map;
    }

    /**
     * 为Query设置参数
     * @param count
     * @param query
     * @param params
     */
    private void setQueryParameters(Integer count, Query query, Object[] params) {
        if (params != null) {
            if (params.length != count) {
                throw new RuntimeException("sql 语句和参数数量不一致");
            }
            int length = params.length;
            for (int i = 0; i < length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
    }
}
