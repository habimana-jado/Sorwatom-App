/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Jado
 */
@Entity
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String product;
    private double weight;
    private double unitPrice;
    private double quantity;
    private double orderPoint;
    
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<CustomerOrder> delivery;
    
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<ItemImage> itemImage;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<DistributorStock> distributorStock;
    
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Cart> cart;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getOrderPoint() {
        return orderPoint;
    }

    public void setOrderPoint(double orderPoint) {
        this.orderPoint = orderPoint;
    }

    public List<CustomerOrder> getDelivery() {
        return delivery;
    }

    public void setDelivery(List<CustomerOrder> delivery) {
        this.delivery = delivery;
    }

    public List<ItemImage> getItemImage() {
        return itemImage;
    }

    public void setItemImage(List<ItemImage> itemImage) {
        this.itemImage = itemImage;
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

    
    @Override
    public String toString() {
        return product;
    }

    
}
