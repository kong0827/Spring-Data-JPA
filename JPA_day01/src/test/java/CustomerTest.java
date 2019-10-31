import com.stu.jpa.pojo.Customer;
import com.stu.jpa.util.JPAUtil;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class CustomerTest {


    @Test
    public void addCustomer() {
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
        customer.setCustName("传智学院");
        customer.setCustLevel("VIP客户");
        customer.setCustSource("网络");
        customer.setCustIndustry("IT教育");
        customer.setCustAddress("昌平区北七家镇");
        customer.setCustPhone("010-84389340");
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

    //删除对象
    @Test
    public void delCustomer(){
        //获得管理实体类工厂
        EntityManagerFactory factory=Persistence.createEntityManagerFactory("myJpa");
        //获得管理实体类对象
        EntityManager manager=factory.createEntityManager();
        //获得事务对象
        EntityTransaction transaction=manager.getTransaction();
        //开启事务
        transaction.begin();
        Customer customer=manager.find(Customer.class,4);
        //customer.setCustId(1);
        manager.remove(customer);
        //提交事务
        transaction.commit();
        //释放资源
        factory.close();
        manager.close();
    }

    @Test
    public  void queryCustomer(){

        // 定义对象
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            // 获取实体管理对象
            em = JPAUtil.getEntityManager();
            // 获取事务对象
            tx = em.getTransaction();
            // 开启事务
            tx.begin();
            // 执行操作
            Customer c1 = em.find(Customer.class, 428);
            // 提交事务
            tx.commit();
            System.out.println(c1); // 输出查询对象
        } catch (Exception e) {
            // 回滚事务
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 释放资源
            em.close();
        }

    }

    @Test
    public  void updateCustomer(){
        EntityManager manager=null;
        EntityTransaction transaction=null;
        try {
            //获得实体管理对象
            manager=JPAUtil.getEntityManager();
            //获得事务管理对象
            transaction=manager.getTransaction();
            //开启事务
            transaction.begin();
            //开始查询
            Customer customer=manager.find(Customer.class,428);
            customer.setCustAddress("China");
            //将对象清除出缓存
            manager.clear();
            //开始执行
            manager.merge(customer);
            //提交事务
            transaction.commit();
        } catch (Exception e) {
            //如果出错就事务回滚，抛出异常
            transaction.rollback();
            e.printStackTrace();
        } finally {
            //不管发生过什么最后都要关闭资源
            manager.close();
        }

    }

    @Test
    public void delById (){
        EntityManager manager=null;
        EntityTransaction transaction=null;

         manager = JPAUtil.getEntityManager();
         transaction=manager.getTransaction();
         //start transaction
        transaction.begin();


    }
}
