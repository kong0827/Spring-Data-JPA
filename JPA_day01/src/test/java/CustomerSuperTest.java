import com.stu.jpa.pojo.Customer;
import com.stu.jpa.util.JPAUtil;
import org.junit.Test;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class CustomerSuperTest {

    @Test
    public void queryAll() {
        EntityManager manager = null;
        EntityTransaction transaction = null;

        try {
            //获得管理实体类对象
            manager = JPAUtil.getEntityManager();
            transaction = manager.getTransaction();
            //开启事务
            transaction.begin();
            //获得Query对象
            String sql = "from cst_customer";
            Query query = manager.createQuery(sql);
            List<Customer> list = query.getResultList();
            for (Customer customer:list) {
                System.out.println(customer.toString());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    @Test
    public void pageQuery(){
        EntityManager manager=null;
        EntityTransaction transaction=null;

        try {
            manager=JPAUtil.getEntityManager();
            transaction=manager.getTransaction();
            transaction.begin();
            String sql ="from cst_customer";
            Query query=manager.createQuery(sql);
            //设置开始索引
            query.setFirstResult(2);
            //设置每页显示条数
            query.setMaxResults(5);
            //开始查询
            List<Customer> list = query.getResultList();
            for (Customer customer:list) {
                System.out.println(customer.toString());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    @Test
    public  void CondtionQuery(){

        EntityManager manager=null;
        EntityTransaction transaction=null;

        try {
            manager=JPAUtil.getEntityManager();
            transaction=manager.getTransaction();
            //start transaction
            transaction.begin();
            //"1"要注意添加
            String sql ="from cst_customer where custName like ?1 ";
            Query query=manager.createQuery(sql);
            //在上面添加1的情况下这里就可以直接写1，必须要添加任何的符号
            query.setParameter(1,"B%");
            Object result = query.getSingleResult();
            System.out.println(result);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }

    }

    @Test
    public  void orderQuery(){

        EntityManager  manager=null;
        EntityTransaction transaction=null;

        try {
            manager=JPAUtil.getEntityManager();
            transaction=manager.getTransaction();
            //start transaction
            transaction.begin();
            String sql ="from cst_customer order by custId desc";
            Query query=manager.createQuery(sql);
            List<Customer> list = query.getResultList();
            for (Customer customer:list){
                System.out.println(customer.toString());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    @Test
    public void countQuery(){
        EntityManager manager=null;
        EntityTransaction transaction=null;

        try {
            manager=JPAUtil.getEntityManager();
            transaction=manager.getTransaction();
            transaction.begin();
            String sql ="select count(custId) from cst_customer ";
            Query query=manager.createQuery(sql);
            Object count = query.getSingleResult();
            System.out.println("总条数是："+count);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            //添加一点注释
            manager.close();
        }

    }
}
