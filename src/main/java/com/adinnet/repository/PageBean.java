package com.adinnet.repository;

import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @author wangren
 * @Description: 分页
 * @create 2018-09-14 13:47
 **/
@Data
public class PageBean<T> implements Serializable {

    //总共的数据条数
    private Long totalCount;
    //第几页
    private Integer pageNum;
    //每页显示多少
    private Integer pageSize;
    //查询出来的数据
    private List<T> datas;
    //查询sql
    private String hql;
    //查询数量sql
    private String countHql;
    //排序字段名称
    private String sortName;
    //排序规则
    private String sortOrder = "desc";

}
