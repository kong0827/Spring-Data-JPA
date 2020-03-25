package com.kxj.jpa.dto;/**
 * @author xiangjin.kong
 * @date 2020/3/25 17:35
 * @desc
 */

/**
 * @ClassName UserDTO4
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2020/3/25 17:35
 * @Version 1.0
 **/
public interface UserDTO4 {

    String getName();

    Long getCount();

    default String toStringInfo() {
        return getName() + " " + getCount();
    }
}
