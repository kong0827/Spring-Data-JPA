package com.kxj.jpa;

import com.kxj.jpa.dao.UserDao;
import com.kxj.jpa.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaApplicationTests {

    @Autowired
    private UserDao dao;

    @Test
    public void test() {
        Optional<UserDTO> userDTO = dao.findByName("tom", UserDTO.class);
        if (userDTO.isPresent()) {
            System.out.println(userDTO);
        }
    }

}
