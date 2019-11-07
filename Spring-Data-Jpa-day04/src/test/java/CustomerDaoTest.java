import com.kxj.dao.CustomerDao;
import com.kxj.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

/**
 * @author kxj
 * @date 2019/11/3 0:35
 * @Desc 测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerDaoTest {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    public void findOneTest() {
        Optional<Customer> customer = customerDao.findById(1L);
        Customer customer1 = null;
        if (customer.isPresent()) {
            customer1 = customer.get();
            System.out.println(customer1);
        } else {
            System.out.println("数据为empty");
        }
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


    @Test
    public void save() {
        Customer customer = new Customer();
        customer.setCustName("奥巴马");
        customer.setCustAddress("美国");
        customer.setCustPhone("111111");
        customerDao.save(customer);
    }

    // 经过测试无法自己设置主键
    // 会根据主键生成策略自己生成
    @Test
    public void save2() {
        Customer customer = new Customer();
        customer.setCustId(20l);
        customer.setCustName("奥巴马");
        customer.setCustAddress("美国");
        customer.setCustPhone("111111");
        customerDao.save(customer);
    }

    @Test
    public void update() {
        Optional<Customer> customerOptional = customerDao.findById(10L);
        Customer customer = null;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
            System.out.println(customer);
        }
        customer.setCustName("特朗普");
        customerDao.save(customer);
    }

    /**
     * getOne的用法
     *   需要在实体类上加@Proxy(lazy = false)注解
     */
    @Test
    public void findTest() {
        Customer one = customerDao.getOne(1L);
        System.out.println(one);
    }

    /**
     * 删除操作 根据id 删除
     */
    @Test
    public void deleteTest() {
        customerDao.deleteById(9L);
    }

    /**
     * 根据对象删除 本质也是根据对象id删除
     */
    @Test
    public void deleteTest2() {
        Customer customer = customerDao.getOne(10L);
        customerDao.delete(customer);
    }

    /**
     * 查找所有
     */
    @Test
    public void findAllTest() {
        List<Customer> customers = customerDao.findAll();
        for (Customer customer : customers) {
            System.out.println(customer);
        }


    }

    /**
     * 查找所有并按照规则排序
     */
    @Test
    public void fingAllSortTest() {
        Sort sort = new Sort(Sort.Direction.DESC, "custSource");
        List<Customer> customerList = customerDao.findAll();
        for (Customer customer : customerList) {
            System.out.println(customer);
        }
    }

}
