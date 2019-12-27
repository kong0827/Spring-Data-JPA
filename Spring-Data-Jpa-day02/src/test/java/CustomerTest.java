import com.kxj.entity.Customer;
import com.kxj.utils.JpaUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;

/**
 * @author kxj
 * @date 2019/10/30 22:38
 * @Desc 客户的增删改查
 */
public class CustomerTest {

    @Test
    public void addCustomer() {
        EntityManager entityMangager = JpaUtils.getEntityMangager();
        EntityTransaction transaction = entityMangager.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.setCustName("测试数据1");
        customer.setCustPhone("666666666666");

        entityMangager.persist(customer);

        transaction.commit();
        entityMangager.close();

    }

    @Test
    public void findCustomerTest() {
        EntityManager entityManager = JpaUtils.getEntityMangager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = entityManager.find(Customer.class, 1L);
        System.out.println(customer);

        transaction.commit();
        entityManager.close();
    }


    @Test
    public void getReferenceCustomerTest() {
        EntityManager entityManager = JpaUtils.getEntityMangager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = entityManager.getReference(Customer.class, 1L);
        System.out.println(customer);

        transaction.commit();
        entityManager.close();
    }

    @Test
    public void updateCustomerTest() {
        EntityManager entityManager = JpaUtils.getEntityMangager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = entityManager.find(Customer.class, 1L);
        customer.setCustName("Learning Spring Data Jpa");
        entityManager.merge(customer);
        customer = entityManager.find(Customer.class, 1L);
        System.out.println(customer);

        transaction.commit();
        entityManager.close();
    }

    @Test
    public void deleteCustomerTest() {
        EntityManager entityManager = JpaUtils.getEntityMangager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Customer customer = entityManager.find(Customer.class, 4L);
        entityManager.remove(customer);
        transaction.commit();
        entityManager.close();
    }
}
