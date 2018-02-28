/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for GF to nan Data definition
 * @author Grdan Andreas
 */
public class GFtoNan {

    private final StringProperty ReportDate; 
    private final StringProperty CustomerName; 
    private final StringProperty ShipDate;
    private final StringProperty InvoiceNo;
    private final StringProperty InvoiceDate;
    private final StringProperty PO;
    private final StringProperty SO;
    private final StringProperty OrderDate;
    private final StringProperty ProcessName;
    private final StringProperty CustomerPartname;
    private final StringProperty InternalPartName;
    private final StringProperty LotId;
    private final StringProperty CustomerLotId;
    private final StringProperty LotType;
    private final StringProperty LotPriority;
    private final StringProperty AgreedGDPW;
    private final StringProperty CalculatedGDPL;
    private final StringProperty CycleTime;
    private final StringProperty BillQtyWfr;
    private final StringProperty WfrIds;
    private final StringProperty vBillDie;
    private final StringProperty ShipToLocation;
    private final StringProperty BillToLocation;
    private final StringProperty ETA;
    private final StringProperty ETD;
    private final StringProperty Forwarder;
    private final StringProperty HAWB;
    private final StringProperty MAWB;
    private final StringProperty FlightNo;
    private final StringProperty ConnectingFlightNo;
    private final StringProperty ReportFileName;
    
    /**
     * Default constructor
     */
    public GFtoNan() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    
    /**
     * Constructor with some initial data
     * @param ReportDate
     * @param CustomerName
     * @param ShipDate
     * @param InvoiceNo
     * @param InvoiceDate
     * @param PO
     * @param SO
     * @param OrderDate
     * @param ProcessName
     * @param CustomerPartname
     * @param InternalPartName
     * @param LotId
     * @param CustomerLotId
     * @param LotType
     * @param LotPriority
     * @param AgreedGDPW
     * @param CalculatedGDPL
     * @param CycleTime
     * @param BillQtyWfr
     * @param WfrIds
     * @param vBillDie
     * @param ShipToLocation
     * @param BillToLocation
     * @param ETA
     * @param ETD
     * @param Forwarder
     * @param HAWB
     * @param MAWB
     * @param FlightNo
     * @param ConnectingFlightNo
     * @param ReportFileName 
     */
    public GFtoNan(String ReportDate, String CustomerName, String ShipDate, String InvoiceNo, String InvoiceDate, String PO, String SO,
            String OrderDate, String ProcessName, String CustomerPartname, String InternalPartName, String LotId, String CustomerLotId,
            String LotType, String LotPriority, String AgreedGDPW, String CalculatedGDPL, String CycleTime, String BillQtyWfr, String WfrIds,
            String vBillDie, String ShipToLocation, String BillToLocation, String ETA, String ETD, String Forwarder, String HAWB, String MAWB,
            String FlightNo, String ConnectingFlightNo, String ReportFileName) {
        this.ReportDate = new SimpleStringProperty(ReportDate); 
        this.CustomerName = new SimpleStringProperty(CustomerName); 
        this.ShipDate = new SimpleStringProperty(ShipDate); 
        this.InvoiceNo = new SimpleStringProperty(InvoiceNo); 
        this.InvoiceDate = new SimpleStringProperty(InvoiceDate); 
        this.PO = new SimpleStringProperty(PO); 
        this.SO = new SimpleStringProperty(SO); 
        this.OrderDate = new SimpleStringProperty(OrderDate); 
        this.ProcessName = new SimpleStringProperty(ProcessName); 
        this.CustomerPartname = new SimpleStringProperty(CustomerPartname); 
        this.InternalPartName = new SimpleStringProperty(InternalPartName); 
        this.LotId = new SimpleStringProperty(LotId); 
        this.CustomerLotId = new SimpleStringProperty(CustomerLotId); 
        this.LotType = new SimpleStringProperty(LotType); 
        this.LotPriority = new SimpleStringProperty(LotPriority); 
        this.AgreedGDPW = new SimpleStringProperty(AgreedGDPW); 
        this.CalculatedGDPL = new SimpleStringProperty(CalculatedGDPL); 
        this.CycleTime = new SimpleStringProperty(CycleTime); 
        this.BillQtyWfr = new SimpleStringProperty(BillQtyWfr); 
        this.WfrIds = new SimpleStringProperty(WfrIds); 
        this.vBillDie = new SimpleStringProperty(vBillDie); 
        this.ShipToLocation = new SimpleStringProperty(ShipToLocation); 
        this.BillToLocation = new SimpleStringProperty(BillToLocation); 
        this.ETA = new SimpleStringProperty(ETA); 
        this.ETD = new SimpleStringProperty(ETD); 
        this.Forwarder = new SimpleStringProperty(Forwarder); 
        this.HAWB = new SimpleStringProperty(HAWB); 
        this.MAWB = new SimpleStringProperty(MAWB); 
        this.FlightNo = new SimpleStringProperty(FlightNo); 
        this.ConnectingFlightNo = new SimpleStringProperty(ConnectingFlightNo); 
        this.ReportFileName = new SimpleStringProperty(ReportFileName);
    }
    
    /**
     * Return data
     */
    public String getReportDate() {
        return ReportDate.get();
    }
    public void setReportDate(String ReportDate) {
        this.ReportDate.set(ReportDate);
    }
    public StringProperty ReportDateProperty() {
        return ReportDate;
    }
    
    public String getCustomerName() {
        return CustomerName.get();
    }
    public void setCustomerName(String CustomerName) {
        this.CustomerName.set(CustomerName);
    }
    public StringProperty CustomerNameProperty() {
        return CustomerName;
    }
    
    public String getShipDate() {
        return ShipDate.get();
    }
    public void setShipDate(String ShipDate) {
        this.ShipDate.set(ShipDate);
    }
    public StringProperty ShipDateProperty() {
        return ShipDate;
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
    
    public String getPO() {
        return PO.get();
    }
    public void setPO(String PO) {
        this.PO.set(PO);
    }
    public StringProperty POProperty() {
        return PO;
    }
    
    public String getSO() {
        return SO.get();
    }
    public void setSO(String SO) {
        this.SO.set(SO);
    }
    public StringProperty SOProperty() {
        return SO;
    }
    
    public String getOrderDate() {
        return OrderDate.get();
    }
    public void setOrderDate(String OrderDate) {
        this.OrderDate.set(OrderDate);
    }
    public StringProperty OrderDateProperty() {
        return OrderDate;
    }
    
    public String getProcessName() {
        return ProcessName.get();
    }
    public void setProcessName(String ProcessName) {
        this.ProcessName.set(ProcessName);
    }
    public StringProperty ProcessNameProperty() {
        return ProcessName;
    }
    
    public String getCustomerPartname() {
        return CustomerPartname.get();
    }
    public void setCustomerPartname(String CustomerPartname) {
        this.CustomerPartname.set(CustomerPartname);
    }
    public StringProperty CustomerPartnameProperty() {
        return CustomerPartname;
    }
    
    public String getInternalPartName() {
        return InternalPartName.get();
    }
    public void setInternalPartName(String InternalPartName) {
        this.InternalPartName.set(InternalPartName);
    }
    public StringProperty InternalPartNameProperty() {
        return InternalPartName;
    }
    
    public String getLotId() {
        return LotId.get();
    }
    public void setLotId(String LotId) {
        this.LotId.set(LotId);
    }
    public StringProperty LotIdProperty() {
        return LotId;
    }
    
    public String getCustomerLotId() {
        return CustomerLotId.get();
    }
    public void setCustomerLotId(String CustomerLotId) {
        this.CustomerLotId.set(CustomerLotId);
    }
    public StringProperty CustomerLotIdProperty() {
        return CustomerLotId;
    }
    
    public String getLotType() {
        return LotType.get();
    }
    public void setLotType(String LotType) {
        this.LotType.set(LotType);
    }
    public StringProperty LotTypeProperty() {
        return LotType;
    }
    
    public String getLotPriority() {
        return LotPriority.get();
    }
    public void setLotPriority(String LotPriority) {
        this.LotPriority.set(LotPriority);
    }
    public StringProperty LotPriorityProperty() {
        return LotPriority;
    }
    
    public String getAgreedGDPW() {
        return AgreedGDPW.get();
    }
    public void setAgreedGDPW(String AgreedGDPW) {
        this.AgreedGDPW.set(AgreedGDPW);
    }
    public StringProperty AgreedGDPWProperty() {
        return AgreedGDPW;
    }
    
    public String getCalculatedGDPL() {
        return CalculatedGDPL.get();
    }
    public void setCalculatedGDPL(String CalculatedGDPL) {
        this.CalculatedGDPL.set(CalculatedGDPL);
    }
    public StringProperty CalculatedGDPLProperty() {
        return CalculatedGDPL;
    }
    
    public String getCycleTime() {
        return CycleTime.get();
    }
    public void setCycleTime(String CycleTime) {
        this.CycleTime.set(CycleTime);
    }
    public StringProperty CycleTimeProperty() {
        return CycleTime;
    }
    
    public String getBillQtyWfr() {
        return BillQtyWfr.get();
    }
    public void setBillQtyWfr(String BillQtyWfr) {
        this.BillQtyWfr.set(BillQtyWfr);
    }
    public StringProperty BillQtyWfrProperty() {
        return BillQtyWfr;
    }
    
    public String getWfrIds() {
        return WfrIds.get();
    }
    public void setWfrIds(String WfrIds) {
        this.WfrIds.set(WfrIds);
    }
    public StringProperty WfrIdsProperty() {
        return WfrIds;
    }
    
    public String getvBillDie() {
        return vBillDie.get();
    }
    public void setvBillDie(String vBillDie) {
        this.vBillDie.set(vBillDie);
    }
    public StringProperty vBillDieProperty() {
        return vBillDie;
    }
    
    public String getShipToLocation() {
        return ShipToLocation.get();
    }
    public void setShipToLocation(String ShipToLocation) {
        this.ShipToLocation.set(ShipToLocation);
    }
    public StringProperty ShipToLocationProperty() {
        return ShipToLocation;
    }
    
    public String getBillToLocation() {
        return BillToLocation.get();
    }
    public void setBillToLocation(String BillToLocation) {
        this.BillToLocation.set(BillToLocation);
    }
    public StringProperty BillToLocationProperty() {
        return BillToLocation;
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
    
    public String getETD() {
        return ETD.get();
    }
    public void setETD(String ETD) {
        this.ETD.set(ETD);
    }
    public StringProperty ETDProperty() {
        return ETD;
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
    
    public String getHAWB() {
        return HAWB.get();
    }
    public void setHAWB(String HAWB) {
        this.HAWB.set(HAWB);
    }
    public StringProperty HAWBProperty() {
        return HAWB;
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
    
    public String getFlightNo() {
        return FlightNo.get();
    }
    public void setFlightNo(String FlightNo) {
        this.FlightNo.set(FlightNo);
    }
    public StringProperty FlightNoProperty() {
        return FlightNo;
    }
    
    public String getConnectingFlightNo() {
        return ConnectingFlightNo.get();
    }
    public void setConnectingFlightNo(String ConnectingFlightNo) {
        this.ConnectingFlightNo.set(ConnectingFlightNo);
    }
    public StringProperty ConnectingFlightNoProperty() {
        return ConnectingFlightNo;
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
