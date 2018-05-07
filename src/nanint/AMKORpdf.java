/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for Amkor PDF reports Data definition
 * @author Grdan Andreas
 */
public class AMKORpdf {

    private final StringProperty ShippingDate; 
    private final StringProperty PackingNo; 
    private final StringProperty InvoiceNo;
    private final StringProperty ShipTo;
    private final StringProperty MAWB;
    private final StringProperty CustomerPO;
    private final StringProperty DescriptionOfGoods;
    private final StringProperty PdfFileName;
    
    /**
     * Default constructor
     */
    public AMKORpdf() {
        this(null, null, null, null, null, null, null, null);
    }
    
    /**
     * Constructor with some initial data
     * @param ShippingDate
     * @param PackingNo
     * @param InvoiceNo
     * @param ShipTo
     * @param MAWB
     * @param CustomerPO
     * @param DescriptionOfGoods
     * @param PdfFileName
     */
    public AMKORpdf(String ShippingDate, String PackingNo, String InvoiceNo, String ShipTo, String MAWB, String CustomerPO,
            String DescriptionOfGoods, String PdfFileName) {
    
        this.ShippingDate = new SimpleStringProperty(ShippingDate); 
        this.PackingNo = new SimpleStringProperty(PackingNo); 
        this.InvoiceNo = new SimpleStringProperty(InvoiceNo); 
        this.ShipTo = new SimpleStringProperty(ShipTo); 
        this.MAWB = new SimpleStringProperty(MAWB); 
        this.CustomerPO = new SimpleStringProperty(CustomerPO); 
        this.DescriptionOfGoods = new SimpleStringProperty(DescriptionOfGoods); 
        this.PdfFileName = new SimpleStringProperty(PdfFileName); 
    }
    
    /**
     * Return data
     */
    public String getShippingDate() {
        return ShippingDate.get();
    }
    public void setShippingDate(String ShippingDate) {
        this.ShippingDate.set(ShippingDate);
    }
    public StringProperty ShippingDateProperty() {
        return ShippingDate;
    }
    
    public String getPackingNo() {
        return PackingNo.get();
    }
    public void setPackingNo(String PackingNo) {
        this.PackingNo.set(PackingNo);
    }
    public StringProperty PackingNoProperty() {
        return PackingNo;
    }
    
    public String getInvoiceNo() {
        return InvoiceNo.get();
    }
    public void setInvoiceNo(String InvoiceNo) {
        this.InvoiceNo.set(InvoiceNo);
    }
    public StringProperty InvoiceNoProperty() {
        return InvoiceNo;
    }
    
    public String getShipTo() {
        return ShipTo.get();
    }
    public void setShipTo(String ShipTo) {
        this.ShipTo.set(ShipTo);
    }
    public StringProperty ShipToProperty() {
        return ShipTo;
    }
    
    public String getMAWB() {
        return MAWB.get();
    }
    public void setMAWB(String MAWB) {
        this.MAWB.set(MAWB);
    }
    public StringProperty MAWBProperty() {
        return MAWB;
    }
    
    public String getCustomerPO() {
        return CustomerPO.get();
    }
    public void setCustomerPO(String CustomerPO) {
        this.CustomerPO.set(CustomerPO);
    }
    public StringProperty CustomerPOProperty() {
        return CustomerPO;
    }
    
    public String getDescriptionOfGoods() {
        return DescriptionOfGoods.get();
    }
    public void setDescriptionOfGoods(String DescriptionOfGoods) {
        this.DescriptionOfGoods.set(DescriptionOfGoods);
    }
    public StringProperty DescriptionOfGoodsProperty() {
        return DescriptionOfGoods;
    }
    
    public String getPdfFileName() {
        return PdfFileName.get();
    }
    public void setPdfFileName(String PdfFileName) {
        this.PdfFileName.set(PdfFileName);
    }
    public StringProperty PdfFileNameProperty() {
        return PdfFileName;
    }
}
