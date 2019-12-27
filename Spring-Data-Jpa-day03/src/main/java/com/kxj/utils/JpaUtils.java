package com.kxj.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author kxj
 * @date 2019/10/30 21:34
 * @Desc EntityManagerFactory的创建浪费资源费时，因为线程安全，抽取出来
 * 获得EntityManager的方法
 */
public class JpaUtils {

    private static EntityManagerFactory entityManagerFactory = null;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("myJpa");
    }

    public static EntityManager getEntityMangager() {
        return entityManagerFactory.createEntityManager();
    }
}
