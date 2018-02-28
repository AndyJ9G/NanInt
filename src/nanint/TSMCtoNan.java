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
    
    /**
     * Default constructor
     */
    public TSMCtoNan() {
        this(null, null, null, null, null, null, null, null);
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
     */
    public TSMCtoNan(String Lot, String WaferPcs, String InvoiceNo, String InvoiceDate, String Forwarder, String MAWB, String HAWB, String ReportFileName) {
        this.Lot = new SimpleStringProperty(Lot); 
        this.WaferPcs = new SimpleStringProperty(WaferPcs);
        this.InvoiceNo = new SimpleStringProperty(InvoiceNo);
        this.InvoiceDate = new SimpleStringProperty(InvoiceDate);
        this.Forwarder = new SimpleStringProperty(Forwarder);
        this.MAWB = new SimpleStringProperty(MAWB);
        this.HAWB = new SimpleStringProperty(HAWB);
        this.ReportFileName = new SimpleStringProperty(ReportFileName);
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
}
