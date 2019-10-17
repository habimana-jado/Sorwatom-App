/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.Cart;
import Domain.Customer;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author student
 */
public class CartDao extends GenericDao<Cart>{
    public List<Cart> findByCustomer(Customer v){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from Cart a where a.customer=:v");
        q.setParameter("v", v);
        List<Cart> list = q.list();
        s.close();
        return list;
    }
}
