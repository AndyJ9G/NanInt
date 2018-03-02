/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for TSMC to nan Data definition
 * @author Grdan Andreas
 */
public class TSMCtoNan {

    private final StringProperty Lot; 
    private final StringProperty WaferPcs; 
    private final StringProperty InvoiceNo;
    private final StringProperty InvoiceDate;
    private final StringProperty Forwarder;
    private final StringProperty MAWB;
    private final StringProperty HAWB;
    private final StringProperty ReportFileName;
    private final StringProperty Technology;
    private final StringProperty Product;
    private final StringProperty Product2000;
    private final StringProperty ETA;
    
    /**
     * Default constructor
     */
    public TSMCtoNan() {
        this(null, null, null, null, null, null, null, null, null, null, null, null);
    }
    
    /**
     * Constructor with some initial data
     * @param Lot
     * @param WaferPcs
     * @param InvoiceNo
     * @param InvoiceDate
     * @param Forwarder
     * @param MAWB
     * @param HAWB
     * @param ReportFileName
     * @param Technology
     * @param Product
     * @param Product2000
     * @param ETA
     */
    public TSMCtoNan(String Lot, String WaferPcs, String InvoiceNo, String InvoiceDate, String Forwarder, String MAWB, String HAWB,
            String ReportFileName, String Technology, String Product, String Product2000, String ETA) {
        this.Lot = new SimpleStringProperty(Lot); 
        this.WaferPcs = new SimpleStringProperty(WaferPcs);
        this.InvoiceNo = new SimpleStringProperty(InvoiceNo);
        this.InvoiceDate = new SimpleStringProperty(InvoiceDate);
        this.Forwarder = new SimpleStringProperty(Forwarder);
        this.MAWB = new SimpleStringProperty(MAWB);
        this.HAWB = new SimpleStringProperty(HAWB);
        this.ReportFileName = new SimpleStringProperty(ReportFileName);
        this.Technology = new SimpleStringProperty(Technology);
        this.Product = new SimpleStringProperty(Product);
        this.Product2000 = new SimpleStringProperty(Product2000);
        this.ETA = new SimpleStringProperty(ETA);
    }
    
    /**
     * Return data
     */    
    public String getLot() {
        return Lot.get();
    }
    public void setLot(String Lot) {
        this.Lot.set(Lot);
    }
    public StringProperty LotProperty() {
        return Lot;
    }
    
    public String getWaferPcs() {
        return WaferPcs.get();
    }
    public void setWaferPcs(String WaferPcs) {
        this.WaferPcs.set(WaferPcs);
    }
    public StringProperty WaferPcsProperty() {
        return WaferPcs;
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
    
    public String getInvoiceDate() {
        return InvoiceDate.get();
    }
    public void setInvoiceDate(String InvoiceDate) {
        this.InvoiceDate.set(InvoiceDate);
    }
    public StringProperty InvoiceDateProperty() {
        return InvoiceDate;
    }
    
    public String getForwarder() {
        return Forwarder.get();
    }
    public void setForwarder(String Forwarder) {
        this.Forwarder.set(Forwarder);
    }
    public StringProperty ForwarderProperty() {
        return Forwarder;
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
    
    public String getHAWB() {
        return HAWB.get();
    }
    public void setHAWB(String HAWB) {
        this.HAWB.set(HAWB);
    }
    public StringProperty HAWBProperty() {
        return HAWB;
    }
    
    public String getReportFileName() {
        return ReportFileName.get();
    }
    public void setReportFileName(String ReportFileName) {
        this.ReportFileName.set(ReportFileName);
    }
    public StringProperty ReportFileNameProperty() {
        return ReportFileName;
    }
    
    public String getTechnology() {
        return Technology.get();
    }
    public void setTechnology(String Technology) {
        this.Technology.set(Technology);
    }
    public StringProperty TechnologyProperty() {
        return Technology;
    }
    
    public String getProduct() {
        return Product.get();
    }
    public void setProduct(String Product) {
        this.Product.set(Product);
    }
    public StringProperty ProductProperty() {
        return Product;
    }
    
    public String getProduct2000() {
        return Product2000.get();
    }
    public void setProduct2000(String Product2000) {
        this.Product2000.set(Product2000);
    }
    public StringProperty Product2000Property() {
        return Product2000;
    }
    
    public String getETA() {
        return ETA.get();
    }
    public void setETA(String ETA) {
        this.ETA.set(ETA);
    }
    public StringProperty ETAProperty() {
        return ETA;
    }
}
