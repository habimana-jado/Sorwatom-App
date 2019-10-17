/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.Distributor;
import Domain.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jado
 */
public class UserDao extends GenericDao<User>{
    public List<User>login(String u,String password){
        Session s=HibernateUtil.getSessionFactory().openSession();
        Query q=s.createQuery("select a from User a where a.username=:v and a.password=:p");
        q.setParameter("v", u);
        q.setParameter("p", password);
        List<User> l=q.list();
        return l;
    } 
    public List<User>findByDistributor(Distributor d){
        Session s=HibernateUtil.getSessionFactory().openSession();
        Query q=s.createQuery("select a from User a where a.distributor=:p");
        q.setParameter("p", d);
        List<User> l=q.list();
        return l;
    } 
}
