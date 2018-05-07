/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for Amkor to ASE Data definition
 * @author Grdan Andreas
 */
public class AMKORtoASE {

    private final StringProperty ShippingDate; 
    private final StringProperty PackingNo; 
    private final StringProperty InvoiceNo;
    private final StringProperty ShipTo;
    private final StringProperty MAWB;
    private final StringProperty CustomerPO;
    private final StringProperty DescriptionOfGoods;
    private final StringProperty PdfFileName;
    private final StringProperty Delivery;
    private final StringProperty Box;
    private final StringProperty Material;
    private final StringProperty DeviceName;
    private final StringProperty PONo;
    private final StringProperty DateCode;
    private final StringProperty FABno;
    private final StringProperty ControlCode;
    private final StringProperty ATPOno;
    private final StringProperty FPO;
    private final StringProperty MCitem;
    private final StringProperty Quantity;
    private final StringProperty ReportFileName;
    
    /**
     * Default constructor
     */
    public AMKORtoASE() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null);
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
     * @param Delivery
     * @param Box
     * @param Material
     * @param DeviceName
     * @param PONo
     * @param DateCode
     * @param FABno
     * @param ControlCode
     * @param ATPOno
     * @param FPO
     * @param MCitem
     * @param Quantity
     * @param ReportFileName
     */
    public AMKORtoASE(String ShippingDate, String PackingNo, String InvoiceNo, String ShipTo, String MAWB, String CustomerPO,
            String DescriptionOfGoods, String PdfFileName, String Delivery, String Box, String Material, String DeviceName,
            String PONo, String DateCode, String FABno, String ControlCode, String ATPOno, String FPO, String MCitem, 
            String Quantity, String ReportFileName) {
    
        this.ShippingDate = new SimpleStringProperty(ShippingDate); 
        this.PackingNo = new SimpleStringProperty(PackingNo); 
        this.InvoiceNo = new SimpleStringProperty(InvoiceNo); 
        this.ShipTo = new SimpleStringProperty(ShipTo); 
        this.MAWB = new SimpleStringProperty(MAWB); 
        this.CustomerPO = new SimpleStringProperty(CustomerPO); 
        this.DescriptionOfGoods = new SimpleStringProperty(DescriptionOfGoods); 
        this.PdfFileName = new SimpleStringProperty(PdfFileName); 
        this.Delivery = new SimpleStringProperty(Delivery); 
        this.Box = new SimpleStringProperty(Box); 
        this.Material = new SimpleStringProperty(Material); 
        this.DeviceName = new SimpleStringProperty(DeviceName); 
        this.PONo = new SimpleStringProperty(PONo); 
        this.DateCode = new SimpleStringProperty(DateCode); 
        this.FABno = new SimpleStringProperty(FABno); 
        this.ControlCode = new SimpleStringProperty(ControlCode); 
        this.ATPOno = new SimpleStringProperty(ATPOno); 
        this.FPO = new SimpleStringProperty(FPO); 
        this.MCitem = new SimpleStringProperty(MCitem); 
        this.Quantity = new SimpleStringProperty(Quantity);  
        this.ReportFileName = new SimpleStringProperty(ReportFileName);
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
    
    public String getDelivery() {
        return Delivery.get();
    }
    public void setDelivery(String Delivery) {
        this.Delivery.set(Delivery);
    }
    public StringProperty DeliveryProperty() {
        return Delivery;
    }
    
    public String getBox() {
        return Box.get();
    }
    public void setBox(String Box) {
        this.Box.set(Box);
    }
    public StringProperty BoxProperty() {
        return Box;
    }
    
    public String getMaterial() {
        return Material.get();
    }
    public void setMaterial(String Material) {
        this.Material.set(Material);
    }
    public StringProperty MaterialProperty() {
        return Material;
    }
    
    public String getDeviceName() {
        return DeviceName.get();
    }
    public void setDeviceName(String DeviceName) {
        this.DeviceName.set(DeviceName);
    }
    public StringProperty DeviceNameProperty() {
        return DeviceName;
    }
    
    public String getPONo() {
        return PONo.get();
    }
    public void setPONo(String PONo) {
        this.PONo.set(PONo);
    }
    public StringProperty PONoProperty() {
        return PONo;
    }
    
    public String getDateCode() {
        return DateCode.get();
    }
    public void setDateCode(String DateCode) {
        this.DateCode.set(DateCode);
    }
    public StringProperty DateCodeProperty() {
        return DateCode;
    }
    
    public String getFABno() {
        return FABno.get();
    }
    public void setFABno(String FABno) {
        this.FABno.set(FABno);
    }
    public StringProperty FABnoProperty() {
        return FABno;
    }
    
    public String getControlCode() {
        return ControlCode.get();
    }
    public void setControlCode(String ControlCode) {
        this.ControlCode.set(ControlCode);
    }
    public StringProperty ControlCodeProperty() {
        return ControlCode;
    }
    
    public String getATPOno() {
        return ATPOno.get();
    }
    public void setATPOno(String ATPOno) {
        this.ATPOno.set(ATPOno);
    }
    public StringProperty ATPOnoProperty() {
        return ATPOno;
    }
    
    public String getFPO() {
        return FPO.get();
    }
    public void setFPO(String FPO) {
        this.FPO.set(FPO);
    }
    public StringProperty FPOProperty() {
        return FPO;
    }
    
    public String getMCitem() {
        return MCitem.get();
    }
    public void setMCitem(String MCitem) {
        this.MCitem.set(MCitem);
    }
    public StringProperty MCitemProperty() {
        return MCitem;
    }
    
    public String getQuantity() {
        return Quantity.get();
    }
    public void setQuantity(String Quantity) {
        this.Quantity.set(Quantity);
    }
    public StringProperty QuantityProperty() {
        return Quantity;
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
