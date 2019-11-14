package com.kxj.dao;

import com.kxj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author kxj
 * @date 2019/11/14 22:43
 * @Desc
 */
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
