import com.kxj.utils.JpaUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

/**
 * @author kxj
 * @date 2019/11/2 18:41
 * @Desc jpql的用法  条件查询，分页查询，排序等
 */
public class CustomerTest {

    // 查询所有
    @Test
    public void findAll() {
        // 创建事务管理器
        EntityManager entityMangager = JpaUtils.getEntityMangager();
        // 得到事务
        EntityTransaction transaction = entityMangager.getTransaction();
        // 开启事务
        transaction.begin();

        // 编写jpql语句
        // 注：select 后接的是与数据库表对应的实体名，可省略包名
//        String sql = " from Customer";
        String sql = "from com.kxj.entity.Customer";
        // 查询操作
        // entityMangager是查询对象，query是执行jpql对象
        Query query = entityMangager.createQuery(sql);
        List resultList = query.getResultList();

        for (Object obj : resultList) {
            System.out.println(obj);
        }

        // 提交事务
        transaction.commit();
        //关闭资源
        entityMangager.close();
    }

    // 倒叙查询
    @Test
    public void ordersTest() {
        // 创建事务管理器
        EntityManager entityMangager = JpaUtils.getEntityMangager();
        // 得到事务
        EntityTransaction transaction = entityMangager.getTransaction();
        // 开启事务
        transaction.begin();

        // 编写jpql语句
        // order by 后可以是custId 或 cust_id
        String sql = "from Customer order by custId desc";
        Query query = entityMangager.createQuery(sql);
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }
        // 提交事务
        transaction.commit();
        //关闭资源
        entityMangager.close();
    }

    // 统计查询
    @Test
    public void countTest() {
        // 创建事务管理器
        EntityManager entityMangager = JpaUtils.getEntityMangager();
        // 得到事务
        EntityTransaction transaction = entityMangager.getTransaction();
        // 开启事务
        transaction.begin();

        // 编写jpql语句
        // select count(*)也支持，但是*有时候不识别，例如select *,建议不用
        String sql = "select count(custId) from Customer";
        Query query = entityMangager.createQuery(sql);
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }

        // 提交事务
        transaction.commit();
    }

    // 分页查询
    @Test
    public void pagingTest() {
        // 创建事务管理器
        EntityManager entityMangager = JpaUtils.getEntityMangager();
        // 得到事务
        EntityTransaction transaction = entityMangager.getTransaction();
        // 开启事务
        transaction.begin();

        // 编写jpql语句
        String sql = " from Customer ";
        Query query = entityMangager.createQuery(sql);
        // 起始索引
        query.setFirstResult(0);
        // 每页显示的条数
        query.setMaxResults(3);
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }

        // 提交事务
        transaction.commit();
    }

    // 条件查询
    @Test
    public void whereTest() {
        // 创建事务管理器
        EntityManager entityMangager = JpaUtils.getEntityMangager();
        // 得到事务
        EntityTransaction transaction = entityMangager.getTransaction();
        // 开启事务
        transaction.begin();

        // 编写jpql语句
        String sql = " from Customer where custName like ? ";
        Query query = entityMangager.createQuery(sql);
        // 占位符从 1 开始
        query.setParameter(1, "%Spring%");
        List resultList = query.getResultList();
        for (Object obj : resultList) {
            System.out.println(obj);
        }

        // 提交事务
        transaction.commit();
    }
}


