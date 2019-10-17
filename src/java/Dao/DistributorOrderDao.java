/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.Distributor;
import Domain.DistributorOrder;
import Domain.Item;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author student
 */
public class DistributorOrderDao extends GenericDao<DistributorOrder>{
    public List<DistributorOrder> findByDistributor(Distributor d){
        Session s=HibernateUtil.getSessionFactory().openSession();
        Query q=s.createQuery("select a from DistributorOrder a where a.distributor=:p");
        q.setParameter("p", d);
        List<DistributorOrder> l=q.list();
        return l;
    } 
//    public List<DistributorOrder>findByItem(Item d){
//        Session s=HibernateUtil.getSessionFactory().openSession();
//        Query q=s.createQuery("select a from DistributorOrder a where a.distributor=:p");
//        q.setParameter("p", d);
//        List<DistributorOrder> l=q.list();
//        return l;
//    } 
}
