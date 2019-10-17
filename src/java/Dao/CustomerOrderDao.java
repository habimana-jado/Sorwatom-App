/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.Customer;
import Domain.CustomerOrder;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jado
 */
public class CustomerOrderDao extends GenericDao<CustomerOrder>{
    public List<CustomerOrder> findByCustomer(Customer v){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from CustomerOrder a where a.customer=:v");
        q.setParameter("v", v);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
    public List<CustomerOrder> findByEmployee(String v){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from Delivery a where a.employee.national_id=:v");
        q.setParameter("v", v);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
    public List<CustomerOrder> findByStatus(String v){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from Delivery a where a.status=:v");
        q.setParameter("v", v);
        List<CustomerOrder> list = q.list();
        s.close();
        return list;
    }
}
