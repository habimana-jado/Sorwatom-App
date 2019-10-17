/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Common.FileUpload;
import Dao.CartDao;
import Dao.CustomerDao;
import Dao.CustomerOrderDao;
import Dao.DistributorDao;
import Dao.ItemImageDao;
import Dao.PayImageDao;
import Dao.UserDao;
import Domain.Cart;
import Domain.Customer;
import Domain.CustomerOrder;
import Domain.Distributor;
import Domain.Item;
import Domain.ItemImage;
import Domain.PayImage;
import Domain.User;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Jado
 */
@ManagedBean
@SessionScoped
public class CustomerModel {

    private Customer customer = new Customer();

    private Customer customerDetails = new Customer();

    CustomerDao customerDao = new CustomerDao();

    private String searchKey = new String();

    private List<Customer> customers = new CustomerDao().FindAll(Customer.class);

    private List<Customer> customerelle;

    private String cid = new String();

    private long sectid;

    private User u = new User();

    private List<CustomerOrder> customerOrder = new ArrayList<>();

    private List<ItemImage> allItemImage = new ArrayList<>();

    private int quantity;

    private String paymethod;

    private String date;

    private Item choosenItem = new Item();

    private List<String> bankslip = new ArrayList<>();

    private List<Distributor> distributors = new ArrayList<>();

    private CustomerOrder delivery = new CustomerOrder();

    private CustomerOrder deliveryDetails = new CustomerOrder();

    CustomerOrderDao deliveryDao = new CustomerOrderDao();

    private Distributor dist = new Distributor();

    private String distId = new String();

    private Cart cart = new Cart();

    private List<Cart> carts = new ArrayList<>();

    private Cart choosencart = new Cart();

    @PostConstruct
    public void init() {
        distributors = new DistributorDao().FindAll(Distributor.class);
        customers = new CustomerDao().FindAll(Customer.class);
        orderinit();

        cartinit();
    }

    public String check(Distributor d) {
        dist = d;
        return "true";
    }

    public void orderinit() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Customer e = x.getCustomer();

        customerOrder = new CustomerOrderDao().findByCustomer(e);

    }

    public void cartinit() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Customer e = x.getCustomer();

        carts = new CartDao().findByCustomer(e);

    }

    public String updatecart(Cart c) {
        choosencart = c;
        choosenItem = c.getItem();
        quantity = c.getQuantity();
        date = new SimpleDateFormat("dd/MM/yyyy").format(c.getDueDate());
        return "single.xhtml?faces-redirect=true";
    }

    public void updateitem() throws ParseException {
        Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(date);

        Distributor d = new DistributorDao().FindOne(Distributor.class, distId);
        choosencart.setDistributor(choosencart.getDistributor());
        choosencart.setDueDate(dt);
        choosencart.setQuantity(quantity);
        choosencart.setCustomer(choosencart.getCustomer());
        choosencart.setItem(choosencart.getItem());

        new CartDao().Update(choosencart);
        choosencart = new Cart();
        cartinit();

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Cart Updated"));
    }

    public void createCustomer() {

        try {

            customerDao = new CustomerDao();

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

    public void createCustomerOrder() throws ParseException {
//        if (this.paymethod.equalsIgnoreCase("slip")) {

        if (this.bankslip.isEmpty()) {
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Your must supply Bank Slip Image", null));
        } else {
            User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
            System.out.println(x.getCustomer().getNames());
            Customer e = x.getCustomer();

            for (Cart c : new CartDao().findByCustomer(e)) {
                delivery.setDistributor(c.getDistributor());
                delivery.setDueDate(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate() + ""));
                delivery.setQuantity(c.getQuantity());
                delivery.setPayMethod(paymethod);
                delivery.setStatus("CREATED");
                delivery.setItem(c.getItem());
                delivery.setCustomer(c.getCustomer());

                deliveryDao = new CustomerOrderDao();
                deliveryDao.register(delivery);

                for (String i : this.bankslip) {
                    PayImage ite = new PayImage();
                    ite.setImage(i);
                    ite.setDelivery(delivery);
                    new PayImageDao().register(ite);
//                System.out.println(x);
                }
                new CartDao().Delete(c);
            }
            delivery = new CustomerOrder();

            cartinit();
//            deliverys = deliveryDao.FindAll(CustomerOrder.class);
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Order Placed, You will be updated on SMS"));

            customerOrder = new CustomerOrderDao().findByCustomer(e);

            final String ACCOUNT_SID
                    = "ACc7eb0c0cd0c175e81e02146be8b71f72";
            final String AUTH_TOKEN
                    = "6f682b919137ae61e6672ced33034942";
//            for (Distributor emp : new DistributorDao().FindAll(Distributor.class)) {
//                if (emp.getUser().getAccess().matches("Deliverer")) {
//                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//                    String receiver = emp.getPhone();
//                    com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
//                            .creator(new PhoneNumber(receiver), // to
//                                    new PhoneNumber("+18577634816"), // from
//                                    "There is new order to be delivered check in on your account.")
//                            .create();
//
//                    System.out.println(message.getSid());
//                }
//            }

        }
//        }
    }

    public void generateWomenPdf() throws FileNotFoundException, DocumentException, BadElementException, IOException, Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        Document document = new Document();
        Rectangle rect = new Rectangle(20, 20, 580, 500);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance((com.lowagie.text.Document) document, baos);
        writer.setBoxSize("art", rect);
        document.setPageSize(rect);
        if (!document.isOpen()) {
            document.open();
        }
//        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("\\layout");
//        path = path.substring(0, path.indexOf("\\build"));
//        path = path + "\\web\\layout\\layout\\img\\womenLOGO2.png";
//        Image image = Image.getInstance(path);
//        image.scaleAbsolute(50, 50);
//        image.setAlignment(Element.ALIGN_LEFT);
        Paragraph title = new Paragraph();
        //BEGIN page
//        title.add(image);
        document.add(title);
        Font font0 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL);
        Font font1 = new Font(Font.TIMES_ROMAN, 9, Font.ITALIC, new Color(90, 255, 20));
        Font font2 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL, new Color(0, 0, 0));
        Font font5 = new Font(Font.TIMES_ROMAN, 10, Font.ITALIC, new Color(0, 0, 0));
        Font colorFont = new Font(Font.TIMES_ROMAN, 10, Font.BOLD, new Color(0, 0, 0));
        Font font6 = new Font(Font.TIMES_ROMAN, 9, Font.NORMAL);
        document.add(new Paragraph("SORWATOM PRODUCTS\n"));
        document.add(new Paragraph("KG 625 ST 4\n", font0));
        document.add(new Paragraph("P.O.BOX 131 \n", font0));
        document.add(new Paragraph("KIGALI-RWANDA\n\n", font0));
        Paragraph p = new Paragraph("Orders Reports\n", colorFont);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(new Paragraph("\n"));
        PdfPTable tables = new PdfPTable(5);
        tables.setWidthPercentage(100);
        PdfPCell cell1s = new PdfPCell(new Phrase("#", font2));
        cell1s.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell1s);
        PdfPCell cell1 = new PdfPCell(new Phrase("Product Name", font2));
        cell1.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell1);
        PdfPCell cell = new PdfPCell(new Phrase("Quantity", font2));
        cell.setBorder(Rectangle.BOTTOM);
        tables.addCell(cell);
        PdfPCell c1 = new PdfPCell(new Phrase("Unit Price", font2));
        c1.setBorder(Rectangle.BOTTOM);
        tables.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase("Total Price", font2));
        c2.setBorder(Rectangle.BOTTOM);
        tables.addCell(c2);
        tables.setHeaderRows(1);
        PdfPCell pdfc5;
        PdfPCell pdfc1;
        PdfPCell pdfc3;
        PdfPCell pdfc2;
        PdfPCell pdfc4;
        PdfPCell pdfc6;
        PdfPCell pdfc7;
        PdfPCell pdfc8;
        int i = 1;
        DecimalFormat dcf = new DecimalFormat("###,###,###");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (CustomerOrder x : customerOrder) {
            pdfc1 = new PdfPCell(new Phrase(i + "", font6));
            pdfc1.setBorder(Rectangle.BOX);
            tables.addCell(pdfc1);
            pdfc5 = new PdfPCell(new Phrase(x.getItem().getProduct(), font6));
            pdfc5.setBorder(Rectangle.BOX);
            tables.addCell(pdfc5);
            pdfc3 = new PdfPCell(new Phrase(x.getQuantity() + "", font6));
            pdfc3.setBorder(Rectangle.BOX);
            tables.addCell(pdfc3);
            pdfc4 = new PdfPCell(new Phrase(x.getItem().getUnitPrice() + "", font6));
            pdfc4.setBorder(Rectangle.BOX);
            tables.addCell(pdfc4);

            pdfc6 = new PdfPCell(new Phrase(x.getItem().getUnitPrice() * x.getQuantity() + "", font6));
            pdfc6.setBorder(Rectangle.BOX);
            tables.addCell(pdfc6);

            i++;
        }
        document.add(tables);
        Paragraph par = new Paragraph("\n\nPrinted On: " + sdf.format(new Date()), font1);
        par.setAlignment(Element.ALIGN_RIGHT);
        document.add(par);
        document.close();
        String fileName = "Report_" + new Date().getTime() / (1000 * 3600 * 24);
        writePDFToResponse(context.getExternalContext(), baos, fileName);
        context.responseComplete();
    }

    private void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) throws IOException {
        externalContext.responseReset();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseHeader("Expires", "0");
        externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        externalContext.setResponseHeader("Pragma", "public");
        externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
        externalContext.setResponseContentLength(baos.size());
        OutputStream out = externalContext.getResponseOutputStream();
        baos.writeTo(out);
        externalContext.responseFlushBuffer();
    }

    public void complete(CustomerOrder d) {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Customer e = x.getCustomer();

        d.setStatus("COMPLETED");
        new CustomerOrderDao().Update(d);

        customerOrder = new CustomerOrderDao().findByCustomer(e);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Completed", ""));
    }

    public List<CustomerOrder> delivering() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        System.out.println(x.getCustomer().getNames());
        Customer e = x.getCustomer();

        customerOrder = new CustomerOrderDao().findByCustomer(e);

        return customerOrder;
    }

    public String singleton(Item it) {
        choosenItem = it;
        allItemImage = new ItemImageDao().findByItem(it.getId());
        return "single.xhtml?faces-redirect=true";
    }

    public void updateCustomer() {
        customerDao.Update(customerDetails);
        customerDetails = new Customer();

        customers = customerDao.FindAll(Customer.class);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Customer updated"));
    }

    public void deleteCustomer() {
        customerDao.Delete(customer);
        customers = customerDao.FindAll(Customer.class);

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Deleted", ""));

    }

    public void createcart() throws ParseException {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        System.out.println(x.getCustomer().getNames());
        Customer e = x.getCustomer();

        Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(date);

        Distributor d = new DistributorDao().FindOne(Distributor.class, distId);
        cart.setDistributor(d);
        cart.setDueDate(dt);
        cart.setQuantity(quantity);
        cart.setItem(choosenItem);
        cart.setCustomer(e);

        new CartDao().register(cart);

        cartinit();
        cart = new Cart();

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Items Added", ""));

    }

    public String redirected() {
        return "pages/index.xhtml?faces-redirect=true";

    }

    public void Upload(FileUploadEvent event) {
        bankslip.add(new FileUpload().Upload(event, "C:\\Users\\student\\Documents\\NetBeansProjects\\SorwatomApp\\web\\uploads\\bankslip\\"));
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(Customer customerDetails) {
        this.customerDetails = customerDetails;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomerelle() {
        return customerelle;
    }

    public void setCustomerelle(List<Customer> customerelle) {
        this.customerelle = customerelle;
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

    public List<CustomerOrder> getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(List<CustomerOrder> customerOrder) {
        this.customerOrder = customerOrder;
    }

    public List<ItemImage> getAllItemImage() {
        return allItemImage;
    }

    public void setAllItemImage(List<ItemImage> allItemImage) {
        this.allItemImage = allItemImage;
    }

    public Item getChoosenItem() {
        return choosenItem;
    }

    public void setChoosenItem(Item choosenItem) {
        this.choosenItem = choosenItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getBankslip() {
        return bankslip;
    }

    public void setBankslip(List<String> bankslip) {
        this.bankslip = bankslip;
    }

    public CustomerOrder getDelivery() {
        return delivery;
    }

    public void setDelivery(CustomerOrder delivery) {
        this.delivery = delivery;
    }

    public CustomerOrder getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(CustomerOrder deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }

    public CustomerOrderDao getDeliveryDao() {
        return deliveryDao;
    }

    public void setDeliveryDao(CustomerOrderDao deliveryDao) {
        this.deliveryDao = deliveryDao;
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

    public String getDistId() {
        return distId;
    }

    public void setDistId(String distId) {
        this.distId = distId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

}
