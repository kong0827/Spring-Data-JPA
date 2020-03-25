package com.kxj.jpa.dao;/**
 * @author xiangjin.kong
 * @date 2020/3/25 17:55
 * @desc
 */

import com.kxj.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName UserParamDao
 * @Description 处理传入参数为空的问题
 * @Author kxj
 * @Date 2020/3/25 17:55
 **/
public interface UserParamDao extends JpaSpecificationExecutor<User>,
        JpaRepository<User, Integer> {


}
