package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;
//==============================================================================
public class InventoryTableModel {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty gender;
    private final SimpleStringProperty size;
    private final SimpleStringProperty articleType;
    private final SimpleStringProperty color1;
    private final SimpleStringProperty color2;
    private final SimpleStringProperty brand;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty status;
    private final SimpleStringProperty donorLastName;
    private final SimpleStringProperty donorFirstName;
    private final SimpleStringProperty donorPhone;
    private final SimpleStringProperty donorEmail;
    private final SimpleStringProperty dateDonated;
    //----------------------------------------------------------------------------
    public InventoryTableModel(Vector<String> inventoryData) {
        barcode = new SimpleStringProperty(inventoryData.elementAt(0));
        gender = new SimpleStringProperty(inventoryData.elementAt(1));
        size = new SimpleStringProperty(inventoryData.elementAt(2));
        articleType = new SimpleStringProperty(inventoryData.elementAt(3));
        color1 = new SimpleStringProperty(inventoryData.elementAt(4));
        color2 = new SimpleStringProperty(inventoryData.elementAt(5));
        brand = new SimpleStringProperty(inventoryData.elementAt(6));
        notes = new SimpleStringProperty(inventoryData.elementAt(7));
        status = new SimpleStringProperty(inventoryData.elementAt(8));
        donorLastName = new SimpleStringProperty(inventoryData.elementAt(9));
        donorFirstName = new SimpleStringProperty(inventoryData.elementAt(10));
        donorPhone = new SimpleStringProperty(inventoryData.elementAt(11));
        donorEmail = new SimpleStringProperty(inventoryData.elementAt(12));
        dateDonated = new SimpleStringProperty(inventoryData.elementAt(16));
    }
    //----------------------------------------------------------------------------
    public String getBarcode() {
        return barcode.get();
    }

    //----------------------------------------------------------------------------
    public void setBarcode(String id) {
        barcode.set(id);
    }
    //---------------------------------------------------------------
    public String getGender() {
        return gender.get();
    }
    //---------------------------------------------------------------
    public void setGender(String g) {
        gender.set(g);
    }
    //---------------------------------------------------------------
    public String getSize() {
        return size.get();
    }
    //---------------------------------------------------------------
    public void setSize(String s) {
        size.set(s);
    }
    //---------------------------------------------------------------
    public String getArticleType() {
        return articleType.get();
    }
    //---------------------------------------------------------------
    public void setArticleType(String aT) {
        articleType.set(aT);
    }
    //---------------------------------------------------------------
    public String getColor1() {
        return color1.get();
    }
    //---------------------------------------------------------------
    public void setColor1(String c1) {
        color1.set(c1);
    }
    //---------------------------------------------------------------
    public String getColor2() {
        return color2.get();
    }
    //---------------------------------------------------------------
    public void setColor2(String c2) {
        this.color2.set(c2);
    }
    //---------------------------------------------------------------
    public String getBrand() {
        return brand.get();
    }
    //---------------------------------------------------------------
    public void setBrand(String b) {
        this.brand.set(b);
    }
    //---------------------------------------------------------------
    public String getNotes() {
        return notes.get();
    }
    //---------------------------------------------------------------
    public void setNotes(String n) {
        this.notes.set(n);
    }
    //---------------------------------------------------------------
    public String getStatus() {
        return status.get();
    }
    //---------------------------------------------------------------
    public void setStatus(String s) {
        this.status.set(s);
    }
    //---------------------------------------------------------------
    public String getDonorLastName() {
        return donorLastName.get();
    }
    //---------------------------------------------------------------
    public void setDonorLastName(String dLN) {
        this.donorLastName.set(dLN);
    }
    //---------------------------------------------------------------
    public String getDonorFirstName() {
        return donorFirstName.get();
    }
    //---------------------------------------------------------------
    public void setDonorFirstName(String dFN) {
        this.donorFirstName.set(dFN);
    }
    //---------------------------------------------------------------
    public String getDonorPhone() {
        return donorPhone.get();
    }
    //---------------------------------------------------------------
    public void setDonorPhone(String dP) {
        this.donorPhone.set(dP);
    }
    //---------------------------------------------------------------
    public String getDonorEmail() {
        return donorEmail.get();
    }
    //---------------------------------------------------------------
    public void setDonorEmail(String dE) {
        this.donorEmail.set(dE);
    }
    //---------------------------------------------------------------
    public String getDateDonated() {
        return dateDonated.get();
    }
    //---------------------------------------------------------------
    public void setDateDonated(String dD) {
        this.dateDonated.set(dD);
    }
    //---------------------------------------------------------------
}