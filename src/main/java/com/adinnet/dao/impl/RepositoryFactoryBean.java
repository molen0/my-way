package com.adinnet.dao.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * @author wangren
 * @Description: 工具
 * @create 2018-09-26 15:49
 **/
public class RepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    public RepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);		// TODO Auto-generated constructor stub
    }

    @SuppressWarnings("rawtypes")
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new repositoryFactory(em);
    }
// 创建一个内部类，该类不用在外部访问
   private static class repositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
        private final EntityManager em;
        public repositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }
        // 设置具体的实现类是BaseRepositoryImpl
     	@Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new RepositorImpl<T, I>((Class<T>) information.getDomainType(), em);
        }
        // 设置具体的实现类的class
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return RepositorImpl.class;
        }
    }


}
