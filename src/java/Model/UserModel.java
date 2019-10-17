/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Dao.CustomerDao;
import Dao.UserDao;
import Domain.Customer;
import Domain.User;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jado
 */
@ManagedBean
@SessionScoped
public class UserModel {

    private User user = new User();

    private User userDetails = new User();

    private UserDao userDao = new UserDao();

    private List<User> users;

    private String username = new String();

    private String password = new String();

    private String userdetails = new String();

    private String sid = new String();

    private String sectid = new String();

    private Customer customer = new Customer();

    CustomerDao customerDao = new CustomerDao();

    private User u = new User();

    private List<Customer> customers = new CustomerDao().FindAll(Customer.class);

    @PostConstruct
    public void init() {
//        users = userDao.FindAll(User.class);
    }

    public String login() throws IOException, Exception {
        findUser();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (user != null) {

            switch (user.getAccess()) {
                case "Admin":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/admin/orders.xhtml");
                    return "faces/pages/admin/orders.xhtml?faces-redirect=true";
                case "Distributor":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/distributor/pendingorder.xhtml");
                    return "faces/pages/distributor/pendingorder.xhtml?faces-redirect=true";
                case "Customer":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", user);
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/customer/home.xhtml");
                    return "faces/pages/customer/home.xhtml?faces-redirect=true";
                case "Block":
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your Account is not activated"));
                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/index.xhtml");
                    return "faces/pages/index.xhtml";
                default:
                    user = null;

                    ec.redirect(ec.getRequestContextPath() + "/faces/pages/index.xhtml");

                    return "/SorwatomApp/faces/pages/index.xhtml";
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Wrong Password Or Username"));
            ec.redirect(ec.getRequestContextPath() + "/faces/pages/index.xhtml");
            return "faces/pages/index.xhtml";
        }

    }

    public void findUser() throws Exception {
        List<User> usersLogin = new UserDao().login(username, password);

        if (!usersLogin.isEmpty()) {
            for (User u : usersLogin) {
                user = u;
//                System.out.println(user.getCustomer().getNames());
            }
        } else {
            user = null;
        }
    }

    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        user = null;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");
    }

    public void createCustomer() {

        try {

            customerDao = new CustomerDao();
//                Sector sect = new SectorDao().FindOne(Sector.class, sectid);

//                customer.setSector(sect);
            customerDao.register(customer);

//                String pass = u.getPassword();
//                String encryptedPass = new PassCode().encrypt(pass);
//                u.setPassword(encryptedPass);
            u.setAccess("Customer");
            u.setCustomer(customer);
            new UserDao().register(u);
            customer = new Customer();
            u = new User();

            customers = customerDao.FindAll(Customer.class);
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Account Created, you may Login"));
        } catch (Exception ex) {
            Logger.getLogger(CustomerModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(String userdetails) {
        this.userdetails = userdetails;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSectid() {
        return sectid;
    }

    public void setSectid(String sectid) {
        this.sectid = sectid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}
