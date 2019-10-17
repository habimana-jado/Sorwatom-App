/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Common.FileUpload;
import Dao.CustomerDao;
import Dao.ItemDao;
import Dao.ItemImageDao;
import Domain.Customer;
import Domain.Item;
import Domain.ItemImage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Jado
 */
@ManagedBean
@SessionScoped
public class ItemModel {

    private Item item = new Item();

    private Item itemDetails = new Item();

    ItemDao itemDao = new ItemDao();

    private String searchKey = new String();

    private List<Item> items;

    private List<Item> itemelle;

    private String cid = new String();

    private List<String> choosenImage = new ArrayList();

    private List<ItemImage> allImage = new ArrayList();
    
    private ItemImage oneImage;
    
    private String itemX;
    
    private List<Customer> customers = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        items = new ItemDao().FindAll(Item.class);
        allImage = new ItemImageDao().FindAll(ItemImage.class);
        customers = new CustomerDao().FindAll(Customer.class);
    }

    public void createItem() {
        if (this.choosenImage.isEmpty()) {
            FacesContext ct = FacesContext.getCurrentInstance();

            ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Your must supply Item Image", null));
        } else {
            itemDao = new ItemDao();
            itemDao.register(item);
            
            for (String x : this.choosenImage) {
                ItemImage ite = new ItemImage();
                ite.setImage(x);
                ite.setItem(item);
                new ItemImageDao().register(ite);
//                System.out.println(x);
            }

            item = new Item();
            allImage = new ItemImageDao().FindAll(ItemImage.class);
            items = itemDao.FindAll(Item.class);
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Item Registered"));
        }
    }

    public void updateItem() {
        itemDao.Update(itemDetails);
        itemDetails = new Item();

        items = itemDao.FindAll(Item.class);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Item updated"));
    }

    public void QuickLook(String itemX){
        oneImage = new ItemImageDao().FindOne(ItemImage.class, itemX);
    }
            
    public void deleteItem() {
        itemDao.Delete(item);
        items = itemDao.FindAll(Item.class);

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Deleted", ""));

    }
    public void Upload(FileUploadEvent event) {
        choosenImage.add(new FileUpload().Upload(event, "C:\\Users\\student\\Documents\\NetBeansProjects\\SorwatomApp\\web\\uploads\\"));
    }
    public ItemImage Itemimage(String itemId) {
        return new ItemImageDao().imageSample(itemId);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(Item itemDetails) {
        this.itemDetails = itemDetails;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItemelle() {
        return itemelle;
    }

    public void setItemelle(List<Item> itemelle) {
        this.itemelle = itemelle;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<String> getChoosenImage() {
        return choosenImage;
    }

    public void setChoosenImage(List<String> choosenImage) {
        this.choosenImage = choosenImage;
    }

    public List<ItemImage> getAllImage() {
        return allImage;
    }

    public void setAllImage(List<ItemImage> allImage) {
        this.allImage = allImage;
    }

    public ItemImage getOneImage() {
        return oneImage;
    }

    public void setOneImage(ItemImage oneImage) {
        this.oneImage = oneImage;
    }

    public String getItemX() {
        return itemX;
    }

    public void setItemX(String itemX) {
        this.itemX = itemX;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

        
}
