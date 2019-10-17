/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.CustomerOrderDao;
import Dao.DistributorDao;
import Dao.GenericDao;
import Dao.UserDao;
import Domain.CustomerOrder;
import Domain.Distributor;
import Domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jado
 */
@ManagedBean
@SessionScoped
public class DistributorModel {

    private Distributor distributor = new Distributor();

    private Distributor distributorDetails = new Distributor();

    DistributorDao distributorDao = new DistributorDao();

    private String searchKey = new String();

    private List<Distributor> distributors;

    private List<Distributor> distributorelle;

    private String cid = new String();

    private long sectid;

    private User u = new User();
    
    private List<CustomerOrder> owndelivery = new ArrayList<>();
    
    @PostConstruct
    public void init() {
//        distributors = new DistributorDao().FindAll(Distributor.class);
//        ownDelivery();
    }

    public List<CustomerOrder> ownDelivery() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Distributor e = x.getDistributor();

//        String id = e.getNational_id();
//        System.out.println(loggedInUser.getId());
//        if (e == null || e.toString().isEmpty()) {
//            owndelivery = new ArrayList<>();
//        } else {

            owndelivery =new CustomerOrderDao().findByEmployee(e.getNational_id());
//        }
        return owndelivery;
    }
    public void createDistributor() {
        if (checkUserExistence(distributor)) {

            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage("The Username is Reserved"));

        } else {
            try {
                
                distributorDao = new DistributorDao();
                
                distributorDao = new DistributorDao();
                distributorDao.register(distributor);
                
                u.setAccess("Block");
                u.setDistributor(distributor);
                new UserDao().register(u);
                u = new User();                
                distributor = new Distributor();

                distributors = distributorDao.FindAll(Distributor.class);
                FacesContext ct = FacesContext.getCurrentInstance();
                ct.addMessage(null, new FacesMessage("Distributor Registered"));
            } catch (Exception ex) {
                Logger.getLogger(DistributorModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Boolean checkUserExistence(Distributor c) {

        Distributor s = (Distributor) new GenericDao().FindOne(Distributor.class, c.getNational_id());
        if (s == null) {
            return false;
        } else {
            return true;
        }

    }

    public void updateEmployee() {
        distributorDao.Update(distributorDetails);
        distributorDetails = new Distributor();

        distributors = distributorDao.FindAll(Distributor.class);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Employee updated"));
    }

    public void deleteEmployee() {
        distributorDao.Delete(distributor);
        distributors = distributorDao.FindAll(Distributor.class);

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Employee Deleted", ""));

    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Distributor getDistributorDetails() {
        return distributorDetails;
    }

    public void setDistributorDetails(Distributor distributorDetails) {
        this.distributorDetails = distributorDetails;
    }

    public DistributorDao getDistributorDao() {
        return distributorDao;
    }

    public void setDistributorDao(DistributorDao distributorDao) {
        this.distributorDao = distributorDao;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public List<Distributor> getDistributorelle() {
        return distributorelle;
    }

    public void setDistributorelle(List<Distributor> distributorelle) {
        this.distributorelle = distributorelle;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public long getSectid() {
        return sectid;
    }

    public void setSectid(long sectid) {
        this.sectid = sectid;
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public List<CustomerOrder> getOwndelivery() {
        return owndelivery;
    }

    public void setOwndelivery(List<CustomerOrder> owndelivery) {
        this.owndelivery = owndelivery;
    }

}
