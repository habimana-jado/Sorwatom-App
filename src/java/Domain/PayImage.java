/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author hp
 */
@Entity
public class PayImage implements Serializable {
    @Id
    private String id = UUID.randomUUID().toString();
    private String image;
    
    @ManyToOne
    private CustomerOrder delivery;

    @ManyToOne
    private DistributorOrder distributorOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CustomerOrder getDelivery() {
        return delivery;
    }

    public void setDelivery(CustomerOrder delivery) {
        this.delivery = delivery;
    }

    public DistributorOrder getDistributorOrder() {
        return distributorOrder;
    }

    public void setDistributorOrder(DistributorOrder distributorOrder) {
        this.distributorOrder = distributorOrder;
    }
    
    
}
