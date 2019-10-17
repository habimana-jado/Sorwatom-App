/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Dao.CustomerOrderDao;
import Dao.ItemDao;
import Dao.ItemImageDao;
import Domain.CustomerOrder;
import Domain.ItemImage;

/**
 *
 * @author hp
 */
public class DeleteUnneded {
    public static void main(String[] args) {
//        for(ItemImage i : new ItemImageDao().FindAll(ItemImage.class)){
//            
//        new ItemImageDao().Delete(i);
//        }
        
        for(CustomerOrder d: new CustomerOrderDao().FindAll(CustomerOrder.class)){
            new CustomerOrderDao().Delete(d);
        }
    }
}
