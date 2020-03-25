package com.kxj.jpa.dto;/**
 * @author xiangjin.kong
 * @date 2020/3/25 15:56
 * @desc
 */

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

/**
 * @ClassName UserInterfaceDTO
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2020/3/25 15:56
 * @Version 1.0
 **/
public interface UserDTO2 {

    String getName();

    /**
     * 管理员数量 当别名与该getXXX名称不一致时，可以使用该注解调整
     * @return
     */
    @Value("#{target.nickName}")
    String getNickname();

    LocalDate getBirthday();

    default String toStringInfo() {
        return getName() + " " + getNickname() + " " + getBirthday();
    }


}
