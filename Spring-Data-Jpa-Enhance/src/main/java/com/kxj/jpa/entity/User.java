package com.kxj.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author kxj
 * @date 2020/3/24 23:29
 * @desc
 */
@Entity
@Table(name = "user")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "sex")
    private Boolean sex;

    @Column(name = "password")
    private String password;
}
