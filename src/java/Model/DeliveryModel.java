/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Common.FileUpload;
import Dao.CustomerOrderDao;
import Dao.DistributorOrderDao;
import Dao.DistributorStockDao;
import Dao.ItemDao;
import Dao.PayImageDao;
import Domain.Customer;
import Domain.CustomerOrder;
import Domain.Distributor;
import Domain.DistributorOrder;
import Domain.DistributorStock;
import Domain.Item;
import Domain.ItemImage;
import Domain.PayImage;
import Domain.User;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
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
public class DeliveryModel {

    private CustomerOrder delivery = new CustomerOrder();

    private CustomerOrder deliveryDetails = new CustomerOrder();

    CustomerOrderDao deliveryDao = new CustomerOrderDao();

    private String searchKey = new String();

    private User loggedInUser = new User();

    private List<DistributorOrder> deliverys;

    private List<CustomerOrder> deliveryelle;

    private List<CustomerOrder> customerOrder = new ArrayList<>();

    private List<CustomerOrder> pendingorder = new ArrayList<>();

    private String cid = new String();

    private Item choosenItem = new Item();

    private List<ItemImage> allItemImage = new ArrayList<>();

    private int quantity;

    private String paymethod;

    private String date;

    private List<String> bankslip = new ArrayList<>();

    private String username = new String();

    private String password = new String();

    private User user = new User();

    private List<CustomerOrder> owndelivery = new ArrayList<>();

    private DistributorOrder distOrder = new DistributorOrder();

    private List<DistributorOrder> ownorder = new ArrayList<>();

    private List<String> choosenImage = new ArrayList<>();

    @PostConstruct
    public void init() {
        deliverys = new DistributorOrderDao().FindAll(DistributorOrder.class);
        pending();
        distributororderinit();
    }

    public void distributororderinit() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Distributor e = x.getDistributor();

        ownorder = new DistributorOrderDao().findByDistributor(e);
    }

    public void distributororder() throws ParseException {
        if (this.bankslip.isEmpty()) {
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Your must supply Bank Slip Image", null));
        } else if (choosenItem.getQuantity() < quantity) {
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Insufficient quantity ordered- " + choosenItem.getQuantity() + " available", null));
        } else {

            User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
            Distributor e = x.getDistributor();

            Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(date);

            distOrder.setDistributor(e);
            distOrder.setItem(choosenItem);
            distOrder.setStatus("CREATED");
            distOrder.setDueDate(dt);
            distOrder.setQuantity(quantity);
            new DistributorOrderDao().register(distOrder);

            for (String i : this.bankslip) {
                PayImage ite = new PayImage();
                ite.setImage(i);
                ite.setDistributorOrder(distOrder);
                new PayImageDao().register(ite);
//                System.out.println(x);
            }
            distributororderinit();
            distOrder = new DistributorOrder();
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Placed", ""));
        }
    
}

    public void myorder() {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        System.out.println(x.getCustomer().getNames());
        Customer e = x.getCustomer();
        customerOrder = new CustomerOrderDao().findByCustomer(e);

    }

    public void pending() {
        pendingorder.clear();
        

        for (CustomerOrder d : new CustomerOrderDao().FindAll(CustomerOrder.class)) {
            if (d.getStatus().matches("CREATED")) {
                pendingorder.add(d);
            }
        }
//        pendingorder = new CustomerOrderDao().findByStatus("CREATED");
//        deliverys = new CustomerOrderDao().FindAll(CustomerOrder.class);
//        return pendingorder;

    }

    public void createDelivery() throws ParseException {
//        if (this.paymethod.equalsIgnoreCase("slip")) {

        if (this.bankslip.isEmpty()) {
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Your must supply Bank Slip Image", null));
        } else {
            User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
            System.out.println(x.getCustomer().getNames());
            Customer e = x.getCustomer();

            Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(date);

            delivery.setDueDate(dt);
            delivery.setQuantity(quantity);
            delivery.setPayMethod(paymethod);
            delivery.setStatus("CREATED");
            delivery.setItem(choosenItem);
            delivery.setCustomer(e);

            deliveryDao = new CustomerOrderDao();
            deliveryDao.register(delivery);

            for (String i : this.bankslip) {
                PayImage ite = new PayImage();
                ite.setImage(i);
                ite.setDelivery(delivery);
                new PayImageDao().register(ite);
//                System.out.println(x);
            }
            delivery = new CustomerOrder();

//            deliverys = deliveryDao.FindAll(CustomerOrder.class);
            FacesContext ct = FacesContext.getCurrentInstance();
            ct.addMessage(null, new FacesMessage("Order Placed, You will be updated on SMS and Email"));

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

    public void confirm(DistributorOrder d) {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Distributor e = x.getDistributor();

        d.setDistributor(e);
        d.setStatus("INITIATED");
        new DistributorOrderDao().Update(d);
        deliverys = new DistributorOrderDao().FindAll(DistributorOrder.class);
        
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Initiated", ""));
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
        
        PdfPCell c3 = new PdfPCell(new Phrase("Status", font2));
        c1.setBorder(Rectangle.BOTTOM);
        tables.addCell(c3);
        PdfPCell c4 = new PdfPCell(new Phrase("Date Due", font2));
        c2.setBorder(Rectangle.BOTTOM);
        tables.addCell(c4);
        
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
        for (DistributorOrder x : deliverys) {
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
            
            pdfc7 = new PdfPCell(new Phrase(x.getStatus() + "", font6));
            pdfc7.setBorder(Rectangle.BOX);
            tables.addCell(pdfc7);
            pdfc8 = new PdfPCell(new Phrase(x.getDueDate() + "", font6));
            pdfc8.setBorder(Rectangle.BOX);
            tables.addCell(pdfc8);


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
    public void complete(DistributorOrder d) {
        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        Distributor e = x.getDistributor();

        d.setDistributor(e);
        d.setStatus("COMPLETED");
        new DistributorOrderDao().Update(d);
        deliverys = new DistributorOrderDao().FindAll(DistributorOrder.class);
        
        Item i = d.getItem();
        i.setQuantity(i.getQuantity()-d.getQuantity());
        new ItemDao().Update(i);
        
        DistributorStock ds = new DistributorStock();
        ds.setDistributor(e);
        ds.setItem(d.getItem());
        ds.setPurchaseDate(new Date());
        ds.setPrice(d.getItem().getUnitPrice());
        ds.setQuantity(Double.parseDouble(d.getQuantity()+""));
        new DistributorStockDao().register(ds);
        
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Completed", ""));
    }

    public String singleton(Item it) {
        choosenItem = it;
        return "single.xhtml?faces-redirect=true";
    }

    public void completed(DistributorOrder d) {
//        User x = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
//        Distributor e = x.getDistributor();

//        d.setEmployee(e);
        d.setStatus("COMPLETED");
        new DistributorOrderDao().Update(d);
        deliverys = new DistributorOrderDao().FindAll(DistributorOrder.class);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Completed", ""));

        final String ACCOUNT_SID
                = "ACc7eb0c0cd0c175e81e02146be8b71f72";
        final String AUTH_TOKEN
                = "6f682b919137ae61e6672ced33034942";

//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        String receiver = d.getCustomer().getPhone();
//        com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
//                .creator(new PhoneNumber(receiver), // to
//                        new PhoneNumber("+18577634816"), // from
//                        "Your Order has been completed.")
//                .create();
//
//        System.out.println(message.getSid());
    }

    public void cancel(CustomerOrder d) {
        d.setStatus("CANCELED");
        new CustomerOrderDao().Update(d);
//        deliverys = new CustomerOrderDao().FindAll(CustomerOrder.class);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order Canceled", ""));
    }

    public String redirected() {
        return "pages/index.xhtml?faces-redirect=true";
    }

    public User findLogger() {
        loggedInUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session"); //hano nuku getting the username
        return loggedInUser;
    }

    public void Upload(FileUploadEvent event) {
        bankslip.add(new FileUpload().Upload(event, "C:\\Users\\student\\Documents\\NetBeansProjects\\SorwatomApp\\web\\uploads\\bankslip\\"));
    }

    public void UploadItem(FileUploadEvent event) {
        choosenImage.add(new FileUpload().Upload(event, "C:\\Users\\student\\Documents\\NetBeansProjects\\SorwatomApp\\web\\uploads\\"));
    }

    public void updateDelivery() {
        deliveryDao.Update(deliveryDetails);
        deliveryDetails = new CustomerOrder();

//        deliverys = deliveryDao.FindAll(CustomerOrder.class);
        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Delivery updated"));
    }

    public void deleteDelivery() {
        deliveryDao.Delete(delivery);
//        deliverys = deliveryDao.FindAll(CustomerOrder.class);

        FacesContext ct = FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Delivery Deleted", ""));

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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public List<DistributorOrder> getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(List<DistributorOrder> deliverys) {
        this.deliverys = deliverys;
    }

    public List<CustomerOrder> getDeliveryelle() {
        return deliveryelle;
    }

    public void setDeliveryelle(List<CustomerOrder> deliveryelle) {
        this.deliveryelle = deliveryelle;
    }

    public List<CustomerOrder> getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(List<CustomerOrder> customerOrder) {
        this.customerOrder = customerOrder;
    }

    public List<CustomerOrder> getPendingorder() {
        return pendingorder;
    }

    public void setPendingorder(List<CustomerOrder> pendingorder) {
        this.pendingorder = pendingorder;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Item getChoosenItem() {
        return choosenItem;
    }

    public void setChoosenItem(Item choosenItem) {
        this.choosenItem = choosenItem;
    }

    public List<ItemImage> getAllItemImage() {
        return allItemImage;
    }

    public void setAllItemImage(List<ItemImage> allItemImage) {
        this.allItemImage = allItemImage;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CustomerOrder> getOwndelivery() {
        return owndelivery;
    }

    public void setOwndelivery(List<CustomerOrder> owndelivery) {
        this.owndelivery = owndelivery;
    }

    public DistributorOrder getDistOrder() {
        return distOrder;
    }

    public void setDistOrder(DistributorOrder distOrder) {
        this.distOrder = distOrder;
    }

    public List<DistributorOrder> getOwnorder() {
        return ownorder;
    }

    public void setOwnorder(List<DistributorOrder> ownorder) {
        this.ownorder = ownorder;
    }

    public List<String> getChoosenImage() {
        return choosenImage;
    }

    public void setChoosenImage(List<String> choosenImage) {
        this.choosenImage = choosenImage;
    }

}
