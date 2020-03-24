package com.kxj.jpa.dao;

import com.kxj.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author kxj
 * @date 2020/3/24 23:33
 * @desc
 */
public interface UserDao extends JpaSpecificationExecutor<User>,
        JpaRepository<User, Integer> {

    Optional<User> findByName(String name);

    <T> Optional<T> findByName(String name, Class<T> type);
}
