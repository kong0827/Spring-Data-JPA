package com.kxj.jpa.dao;

import com.kxj.jpa.dto.UserDTO;
import com.kxj.jpa.dto.UserDTO2;
import com.kxj.jpa.dto.UserDTO3;
import com.kxj.jpa.dto.UserDTO4;
import com.kxj.jpa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author kxj
 * @date 2020/3/24 23:33
 * @desc 自定义对象接收
 */
public interface UserDao extends JpaSpecificationExecutor<User>,
        JpaRepository<User, Integer> {

    /**
     * 根据id查询
     * @param id
     * @param type
     * @param <T>
     * @return
     */
    <T> Optional<T> findById(int id, Class<T> type);

    @Query(value = "select new com.kxj.jpa.dto.UserDTO(u.name, u.nickname, u.birthday) from User u where u.name = :name")
    List<UserDTO> findByUserNames(@Param("name") String name);

    /**
     * 根据用户名查找
     * 别名必须写 在HQL方式中 否则无法转换 为null，在原生SQL中别名可写可不写
     * @param name
     * @return
     */
    @Query(value = "select u.name as name, u.nickname as nickName, u.birthday as birthday from User u where u.name = :name")
    List<UserDTO2> findByUserName(@Param("name") String name);

    /**
     * 分页 根据用户名查找
     * @param name
     * @param pageable
     * @return
     */
    @Query(value = "select u.name as name, u.nickname as nickName, u.birthday as birthday from User u where u.name = :name")
    Page<UserDTO2> findByUserName(@Param("name") String name, Pageable pageable);


    /**
     * 分页 根据用户名查找 原生SQL
     * @param name
     * @param pageable
     * @return
     */
    @Query(value = "select name, nickName, birthday from user  where name = :name", nativeQuery = true)
    Page<UserDTO2> findByUserNameUseNativeSQL(@Param("name") String name, Pageable pageable);


    /**
     * 查找名字相同的数量 类
     * @return
     */
    @Query(value = "select new com.kxj.jpa.dto.UserDTO3(u.name, count(u.id)) from User u group by u.name")
    List<UserDTO3> findUserCount();


    /**
     * 查找名字相同的数量 接口
     * @return
     */
//    @Query(value = "select u.name as name, count(u.id) as count from User u group by u.name")
    @Query(value = "select name, count(id) as count from user group by name", nativeQuery = true)
    List<UserDTO4> findUserNameCount();






}
