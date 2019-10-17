/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Dao.DistributorDao;
import Dao.UserDao;
import Domain.Distributor;
import Domain.User;

/**
 *
 * @author Jado
 */
public class Create_Tables {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        HibernateUtil.getSessionFactory().openSession();

        Distributor e = new Distributor();
        e.setEmail("frank@gmail.com");
        e.setNames("Frank");
        e.setNational_id("11994876869239");
        e.setPhone("0788984736");
        new DistributorDao().register(e);
        
        User u = new User();
        u.setUsername("frank");
        u.setPassword("frank");
        u.setAccess("Admin");
        u.setDistributor(e);;
        new UserDao().register(u);
    }
    
}
