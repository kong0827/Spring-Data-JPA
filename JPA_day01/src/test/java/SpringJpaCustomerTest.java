import com.stu.jpa.dao.CustomerDao;
import com.stu.jpa.pojo.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author QFu
 * @date 2019/10/31 13:45
 * @description  整合Spring后的CRUD
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringJpaCustomerTest {

    @Autowired
    private CustomerDao customerDao;
    Logger logger= LoggerFactory.getLogger(SpringJpaCustomerTest.class);

    @Test
    public void addCustomer() {
        //在save方法中如果保存的对象有Id则先根据id查询出对象执行更新操作，如果不存在id就直接执行插入操作
        Customer customer = new Customer();
        customer.setCustId(90);
        customer.setCustName("通联金融");
        customer.setCustAddress("上海市浦东新区");
        Customer c = customerDao.save(customer);
        logger.info(c.toString());
    }


    @Test
    public void delCustomer(){
        //创建对象删除方式
//        Customer customer=new Customer();
//        customer.setCustId(444);
//        customerDao.delete(customer);
        //直接通过Id删除方式
        customerDao.delete(443);
        logger.info("删除成功");
    }


    //查询数据
    @Test
    public void QueryAllCustomer(){
        List<Customer> customers = customerDao.findAll();
        for (Customer customer:customers) {
            logger.info(customers.toString());
        }
    }
}


