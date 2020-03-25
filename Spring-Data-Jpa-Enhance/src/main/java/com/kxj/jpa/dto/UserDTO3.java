package com.kxj.jpa.dto;/**
 * @author xiangjin.kong
 * @date 2020/3/25 16:53
 * @desc
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName UserDTO3
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2020/3/25 16:53
 * @Version 1.0
 **/
@Data
@ToString
@AllArgsConstructor // 必须写，转换时会调用
public class UserDTO3 {

    private String name;

    /**
     * 数量
     * 注意：count（*）查询返回的数据类型是Long类型，如果用其他类型接收。会出错
     */
    private Long count;
}
