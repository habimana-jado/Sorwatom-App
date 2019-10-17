/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Domain.ItemImage;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jado
 */
public class ItemImageDao extends GenericDao<ItemImage>{
    public ItemImage imageSample(String itemId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {

            Query qry = s.createQuery("select a from ItemImage a where a.item.id=?");
            qry.setString(0, itemId);
            qry.setMaxResults(1);
            ItemImage mti = (ItemImage) qry.uniqueResult();
            return mti;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            s.close();
        }
    }
    public List<ItemImage> findByItem(long iq){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("select a from ItemImage a where a.item.id=:v");
        q.setParameter("v", iq);
        List<ItemImage> list = q.list();
        s.close();
        return list;
     }
}
