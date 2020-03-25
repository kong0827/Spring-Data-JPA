package com.kxj.jpa;

import com.kxj.jpa.dao.UserDao;
import com.kxj.jpa.dto.UserDTO;
import com.kxj.jpa.dto.UserDTO2;
import com.kxj.jpa.dto.UserDTO3;
import com.kxj.jpa.dto.UserDTO4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaApplicationTest {

    @Autowired
    private UserDao dao;

    @Test
    public void findByIdTest() {
        Optional<UserDTO> userDTO = dao.findById(1, UserDTO.class);
        if (userDTO.isPresent()) {
            System.out.println(userDTO);
        }
    }

    @Test
    public void findByUserNamesTest() {
        List<UserDTO> userDTOS = dao.findByUserNames("tom");
        for (UserDTO userDTO : userDTOS) {
            System.out.println(userDTO);
        }
    }

    @Test
    public void findByNameTest() {
        List<UserDTO2> list = dao.findByUserName("tom");
        list.forEach(user -> System.out.println(user.toStringInfo()));
    }

    @Test
    public void findByNamePageTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDTO2> page = dao.findByUserName("tom", pageable);
        page.getContent().forEach(user -> System.out.println(user.toStringInfo()));
        System.out.println("条数：" + page.getTotalElements());
    }

    @Test
    public void findByNamePageUseNativeSQLTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDTO2> page = dao.findByUserNameUseNativeSQL("tom", pageable);
        page.getContent().forEach(user -> System.out.println(user.toStringInfo()));
        System.out.println("条数：" + page.getTotalElements());
    }


    @Test
    public void findUserCountTest() {
        List<UserDTO3> users = dao.findUserCount();
        users.stream().forEach(user -> {
            System.out.println(user);
        });
    }

    @Test
    public void findUserNameCountTest() {
        List<UserDTO4> users = dao.findUserNameCount();
        users.forEach(user -> {
            System.out.println(user.toStringInfo());
        });
    }

}
