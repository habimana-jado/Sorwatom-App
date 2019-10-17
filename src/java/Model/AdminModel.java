/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.DistributorDao;
import Dao.UserDao;
import Domain.Distributor;
import Domain.User;
import java.util.ArrayList;
import java.util.List;
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
public class AdminModel {
    
    private List<Distributor> distributors = new ArrayList<>();
    
    private Distributor dist = new Distributor();
    
    @PostConstruct
    public void init(){
        distributors = new DistributorDao().FindAll(Distributor.class);
    }
    
    public String approve(Distributor d){
        dist = d;
        return "approvedistributor.xhtml?faces-redirect=true";
    }

    public void approving(){
        for(User u : new UserDao().findByDistributor(dist)){
            u.setAccess("Distributor");
            new UserDao().Update(u);
        }
        new DistributorDao().Update(dist);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Disributor Approved"));
    }
    public void declining(){
        new DistributorDao().Update(dist);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Disributor Declined"));
    }
    public List<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public Distributor getDist() {
        return dist;
    }

    public void setDist(Distributor dist) {
        this.dist = dist;
    }
    
    
}
