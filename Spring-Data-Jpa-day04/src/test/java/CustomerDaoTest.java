import com.kxj.dao.CustomerDao;
import com.kxj.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author kxj
 * @date 2019/11/3 0:35
 * @Desc 测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerDaoTest {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void findOneTest() {
        Customer customer = customerDao.findOne(1l);
        System.out.println(customer);
    }

    @Test
    public void find() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // 3、获取事务对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.find(Customer.class, 1l);

        // 5、提交事务
        transaction.commit();
        // 6、释放资源
        entityManager.close();
        entityManagerFactory.close();
    }
}
