package com.kxj.test;

import com.kxj.dao.CustomerDao;
import com.kxj.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

/**
 * @author kxj
 * @date 2019/11/11 22:33
 * @Desc 动态查询
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerTest {

    @Autowired
    private CustomerDao customerDao;

    /**
     * 根据条件 查询对象
     */
    @Test
    public void testSpec() {
        /**
         * 匿名内部类
         *      1、实现Specification接口(提供泛型，查询的对象类型)
         *      2、实现toPredicate方法
         *      3、需要借助方法参数中的两个参数
         *          root：获取需要查询的对象属性
         *          CriteraBuilder:构造查询条件的，内部封账了很多的查询条件(模糊查询，精准匹配)
         *
         *
         * 案例：根据客户名称查询
         *      1、查询方式
         *          criteriaBuilder对象
         *      2、比较的属性名称
         *          root对象
         */
        Specification<Customer> specification = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //1、获取比较的属性
                Path<Object> custName = root.get("custName");
                //2、构造查询条件
                /**
                 * 第一个参数： 需要比较的比象(path对象)
                 * 第二个参数：当前需要比较的取值
                 */
                Predicate predicate = criteriaBuilder.equal(custName, "奥巴马");
                return predicate;
            }
        };
        Optional<Customer> customerOptional = customerDao.findOne(specification);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            System.out.println(customer);
        }
    }

    /**
     * 多条件 查询对象
     */
    @Test
    public void testSpec2() {
        Specification<Customer> specification = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //1、获取比较的属性
                Path<Object> custName = root.get("custName");
                Path<Object> custIndustry = root.get("custIndustry");
                //2、构造查询条件
                /**
                 * 第一个参数： 需要比较的比象(path对象)
                 * 第二个参数：当前需要比较的取值
                 */
                Predicate p1 = criteriaBuilder.equal(custName, "奥巴马");
                System.out.println(p1);
                Predicate p2 = criteriaBuilder.equal(custIndustry, "服务业");

                // 将多个查询条件组合在一起(and,or)
                Predicate predicate = criteriaBuilder.and(p1, p2);
                return predicate;
            }
        };
        Optional<Customer> customerOptional = customerDao.findOne(specification);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            System.out.println(customer);
        }
    }

    /**
     * 模糊匹配 查询对象
     * equal:直接得到path对象（属性），然后进行比较即可
     * gt,lt,ge,,le,like:得到path对象，根据path指定的参数类型，在去比较即可
     * 指定参数类型：path.as(类型的字节码对象)
     */
    @Test
    public void testSpec3() {
        Specification<Customer> specification = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //1、获取比较的属性
                Path<Object> custName = root.get("custName");
                //2、构造查询条件
                /**
                 * 第一个参数： 需要比较的比象(path对象)
                 * 第二个参数：当前需要比较的取值
                 */
                Predicate predicate = criteriaBuilder.like(custName.as(String.class), "%奥巴马%");

                criteriaBuilder.function()
                // 将多个查询条件组合在一起(and,or)
                return predicate;
            }
        };
        List<Customer> customers = customerDao.findAll(specification);
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    @Test
    public void testSpec4() {
        Specification<Customer> specification = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> custName = root.get("custName");
                Predicate predicate = criteriaBuilder.f(custName.as(String.class), "%奥巴马%");
                return predicate;
            }
        };

        Sort sort = new Sort(Sort.Direction.DESC, "custLevel");
        List<Customer> customers = customerDao.findAll(specification, sort);
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    /**
     * 分页查询
     * <p>
     * Specification: 查询条件
     * Pageable:分页参数
     * 分页参数： 查询的页码， ，每页查询的条件
     * findAll(Specification, Pageable) 带有条件的分页
     * findAll(Pageable)：没有条件的分页
     */
    @Test
    public void testSpec5() {

        Specification specification = null;
        /**
         * PageReuqest对象是Pageable接口的实现类
         *
         * 创建PageRequest的过程中，需要调用它的构造方法传入两个参数
         *  第一个参数：当前查询的页数（从0开始）
         *  第二个参数：每页查询的数量
         *
         *  直接new PageRequest已经过时，PageRequest.of取代
         */
        Pageable pageRequest = PageRequest.of(0, 2);

        // 分页查询
        Page<Customer> customers = customerDao.findAll((Specification<Customer>) null, pageRequest);
        for (Customer customer : customers) {
            System.out.println(customer);
        }

        System.out.println("总条数 " + customers.getTotalElements());
        System.out.println("集合列表 " + customers.getContent());
        System.out.println("总页数 " + customers.getTotalPages());

    }

}
