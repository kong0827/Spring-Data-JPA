import com.stu.jpa.pojo.Customer;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CustomerTest {


    @Test
    public void CustomerTest() {
        /**
         * 创建实体管理类工厂，借助Persistence的静态方法获取
         * 其中传递的参数为持久化单元名称，需要jpa配置文件中指定
         */

        EntityManagerFactory factory= Persistence.createEntityManagerFactory("myJpa");

        //创建管理实体类
        EntityManager manager=factory.createEntityManager();
        //获取事物对象
        EntityTransaction transaction=manager.getTransaction();
        //开启事物
        transaction.begin();
        Customer customer=new Customer();
        customer.setCustName("Bob");
        /*
           getTransaction : 获取事务对象
        	persist ： 保存操作
	        merge ： 更新操作
	        remove ： 删除操作
        	find/getReference ： 根据id查询

        */
        manager.persist(customer);
        //提交事物
        transaction.commit();
        //释放资源
        manager.close();
        factory.close();
    }
}
