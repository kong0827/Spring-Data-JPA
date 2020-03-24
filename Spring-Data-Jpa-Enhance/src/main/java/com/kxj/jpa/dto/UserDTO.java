package com.kxj.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author kxj
 * @date 2020/3/24 23:39
 * @desc
 */
@Data
@ToString
//@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private String name;

    private String nickname;

    private LocalDate birthday;

}
