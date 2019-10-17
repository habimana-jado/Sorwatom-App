package Dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Nkusi Isaac
 */
public class GenericDao<X> {

    public void register(X s) {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        ss.beginTransaction();
        ss.save(s);
        ss.getTransaction().commit();
        ss.close();
    }

    public void Update(X s) {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        ss.beginTransaction();
        ss.update(s);
        ss.getTransaction().commit();
        ss.close();
    }

    public void Delete(X s) {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        ss.beginTransaction();
        ss.delete(s);
        ss.getTransaction().commit();
        ss.close();
    }

    public List<X> FindAll(Class c) {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Query q = ss.createQuery("from " + c.getName() + "");
        List<X> list = q.list();
        ss.close();
        return list;
    }

    public X FindOne(Class c, Serializable id) {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        X s = (X) ss.get(c, id);
        ss.close();
        return s;
    }

}
