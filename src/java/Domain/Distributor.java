/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Jado
 */
@Entity
public class Distributor implements Serializable{
    @Id
    private String national_id;
    private String names;
    private String email;
    private String phone;
    private String location;
    
    @OneToOne(mappedBy = "distributor")
    private User user;
    
    @OneToMany(mappedBy = "distributor", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<CustomerOrder> delivery;
    
    @OneToMany(mappedBy = "distributor", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<DistributorStock> distributorStock;
    
    @OneToMany(mappedBy = "distributor", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Cart> cart;
    

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<CustomerOrder> getDelivery() {
        return delivery;
    }

    public void setDelivery(List<CustomerOrder> delivery) {
        this.delivery = delivery;
    }

    public List<DistributorStock> getDistributorStock() {
        return distributorStock;
    }

    public void setDistributorStock(List<DistributorStock> distributorStock) {
        this.distributorStock = distributorStock;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

}
