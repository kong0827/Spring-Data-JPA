package com.stu.jpa.util;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    // JPA的实体管理器工厂：相当于Hibernate的SessionFactory
    @Autowired
    private static EntityManagerFactory em;
    /**
     * 使用管理器工厂生产一个管理器对象
     *
     * @return
     */
    public static EntityManager getEntityManager() {
        return em.createEntityManager();
    }

}
