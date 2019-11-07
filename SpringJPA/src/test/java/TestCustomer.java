import com.jpa.dao.CustomerDao;
import com.jpa.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestCustomer {
    Logger logger= LoggerFactory.getLogger(TestCustomer.class);

    @Autowired
    private CustomerDao customerDao;

    Customer customer=new Customer();

    @Test
    public void TestPage(){

        //设置查询条件,当存在外键约束时，jpa的查询会将与之相关的所有的文件都查询出来
        PageRequest pageable=new PageRequest(0,5, Sort.Direction.ASC,"custId");
        Page<Customer> customers = customerDao.findAll(pageable);
        for (Customer customer:customers) {
            logger.info(customer.toString());
        }

    }

    @Test
    public void TestCondition(){
        //创建一个实体类

        customer.setCustName("Bob");
        customer.setCustAddress("China");
        customer.setCustPhone("123564894564");
        //使用匿名内部类的方式，创建一个Specification的实现类，并实现toPredicate方法
        Specification<Customer> spec = new Specification<Customer>() {
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //创建一个集合重要的作用就是保存设置的查询条件的
                List<Predicate> predicates = new ArrayList();
                //创建一个数组，以集合的初始长度作为数组的初始化长度（具体作用是什么不清楚？？）
                Predicate[] ps = new Predicate[predicates.size()];
                //开始对需要的条件进行非空检验
                if(!StringUtils.isEmpty(customer.getCustName())) {
                    //将条件记录下来
                    predicates.add(criteriaBuilder
                            .like(root.get("custName"), "%" + customer.getCustName() + "%"));
                }
                if(!StringUtils.isEmpty(customer.getCustAddress())){
                    predicates.add(criteriaBuilder
                            .like(root.get("custAddress"), "%" + customer.getCustAddress() + "%"));
                }
                if(!StringUtils.isEmpty(customer.getCustPhone())){
                    predicates.add(criteriaBuilder.equal(root.get("custPhone"),""));
                }
                criteriaQuery.where(predicates.toArray(ps));
                return null;
            }
        };
        List<Customer> customers = customerDao.findAll(spec);
        for (Customer cust:customers) {
            logger.info(cust.toString());
        }

    }

    @Test
    public void TestDelete(){
        customerDao.deleteById(435);

    }


    //当删除具有主外键关系的表数据时，删除主键，会将附带的以该主键作为外键的数据删除，但是这是需要通过注解设置的
    @Test
    public void TestSave(){
        customer.setCustId(999);
        Customer save = customerDao.save(customer);
        logger.info(save.toString());
    }

}
