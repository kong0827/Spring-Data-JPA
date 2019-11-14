package com.kxj.test;

import com.kxj.dao.RoleDao;
import com.kxj.dao.UserDao;
import com.kxj.entity.Role;
import com.kxj.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * @author kxj
 * @date 2019/11/14 22:53
 * @Desc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Test
    public void saveTest() {
        User user = new User();
        user.setUserName("马化腾人");

        Role role = new Role();
        role.setRoleName("马云");

        roleDao.save(role);
        userDao.save(user);
    }

    @Test
    public void cascadeAddTest() {
        User user = new User();
        user.setUserName("马化腾");

        Role role = new Role();
        role.setRoleName("马云");

        user.getRoles().add(role);

        userDao.save(user);

    }

    @Test
    public void cascadeDelTest() {
        User user = new User();
        Optional<User> optionalUser = userDao.findById(1L);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        userDao.delete(user);

    }
}
