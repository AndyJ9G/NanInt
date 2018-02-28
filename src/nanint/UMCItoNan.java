/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for UMCI to nan Data definition
 * @author Grdan Andreas
 */
public class UMCItoNan {

    private final StringProperty PART_DIV; 
    private final StringProperty INV_NO; 
    private final StringProperty SHPTO_ID;
    private final StringProperty INV_DATE;
    private final StringProperty MAWB_NO;
    private final StringProperty HAWB_NO;
    private final StringProperty FLT_NO;
    private final StringProperty FLT_DATE;
    private final StringProperty FLT_DEST;
    private final StringProperty CARTON_NO;
    private final StringProperty PO_NO;
    private final StringProperty PRD_NO;
    private final StringProperty LOT_TYPE;
    private final StringProperty LOT_NO;
    private final StringProperty SHIP_W_QTY;
    private final StringProperty SHIP_D_QTY;
    private final StringProperty SHP_PRD_NO;
    private final StringProperty CTM_DEVICE;
    private final StringProperty CUSTOMER_LOT;
    private final StringProperty UMC_INV_NO;
    private final StringProperty REMARK;
    private final StringProperty WAFER_NO;
    private final StringProperty ReportFileName;
    
    /**
     * Default constructor
     */
    public UMCItoNan() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);
    }
    
    /**
     * Constructor with some initial data
     * @param PART_DIV
     * @param INV_NO
     * @param SHPTO_ID
     * @param INV_DATE
     * @param MAWB_NO
     * @param HAWB_NO
     * @param FLT_NO
     * @param FLT_DATE
     * @param FLT_DEST
     * @param CARTON_NO
     * @param PO_NO
     * @param PRD_NO
     * @param LOT_TYPE
     * @param LOT_NO
     * @param SHIP_W_QTY
     * @param SHIP_D_QTY
     * @param SHP_PRD_NO
     * @param CTM_DEVICE
     * @param CUSTOMER_LOT
     * @param UMC_INV_NO
     * @param REMARK
     * @param WAFER_NO
     * @param ReportFileName 
     */
    public UMCItoNan(String PART_DIV, String INV_NO, String SHPTO_ID, String INV_DATE, String MAWB_NO, String HAWB_NO,
            String FLT_NO, String FLT_DATE, String FLT_DEST, String CARTON_NO, String PO_NO, String PRD_NO, String LOT_TYPE, String LOT_NO, 
            String SHIP_W_QTY, String SHIP_D_QTY, String SHP_PRD_NO, String CTM_DEVICE, String CUSTOMER_LOT, String UMC_INV_NO,
            String REMARK, String WAFER_NO, String ReportFileName) {
        this.PART_DIV = new SimpleStringProperty(PART_DIV); 
        this.INV_NO = new SimpleStringProperty(INV_NO); 
        this.SHPTO_ID = new SimpleStringProperty(SHPTO_ID); 
        this.INV_DATE = new SimpleStringProperty(INV_DATE); 
        this.MAWB_NO = new SimpleStringProperty(MAWB_NO); 
        this.HAWB_NO = new SimpleStringProperty(HAWB_NO); 
        this.FLT_NO = new SimpleStringProperty(FLT_NO); 
        this.FLT_DATE = new SimpleStringProperty(FLT_DATE); 
        this.FLT_DEST = new SimpleStringProperty(FLT_DEST); 
        this.CARTON_NO = new SimpleStringProperty(CARTON_NO); 
        this.PO_NO = new SimpleStringProperty(PO_NO); 
        this.PRD_NO = new SimpleStringProperty(PRD_NO); 
        this.LOT_TYPE = new SimpleStringProperty(LOT_TYPE); 
        this.LOT_NO = new SimpleStringProperty(LOT_NO); 
        this.SHIP_W_QTY = new SimpleStringProperty(SHIP_W_QTY); 
        this.SHIP_D_QTY = new SimpleStringProperty(SHIP_D_QTY); 
        this.SHP_PRD_NO = new SimpleStringProperty(SHP_PRD_NO); 
        this.CTM_DEVICE = new SimpleStringProperty(CTM_DEVICE); 
        this.CUSTOMER_LOT = new SimpleStringProperty(CUSTOMER_LOT); 
        this.UMC_INV_NO = new SimpleStringProperty(UMC_INV_NO); 
        this.REMARK = new SimpleStringProperty(REMARK); 
        this.WAFER_NO = new SimpleStringProperty(WAFER_NO);  
        this.ReportFileName = new SimpleStringProperty(ReportFileName);
    }
    
    /**
     * Return data
     */
    public String getPART_DIV() {
        return PART_DIV.get();
    }
    public void setPART_DIV(String PART_DIV) {
        this.PART_DIV.set(PART_DIV);
    }
    public StringProperty PART_DIVProperty() {
        return PART_DIV;
    }
    
    public String getINV_NO() {
        return INV_NO.get();
    }
    public void setINV_NO(String INV_NO) {
        this.INV_NO.set(INV_NO);
    }
    public StringProperty INV_NOProperty() {
        return INV_NO;
    }
    
    public String getSHPTO_ID() {
        return SHPTO_ID.get();
    }
    public void setSHPTO_ID(String SHPTO_ID) {
        this.SHPTO_ID.set(SHPTO_ID);
    }
    public StringProperty SHPTO_IDProperty() {
        return SHPTO_ID;
    }
    
    public String getINV_DATE() {
        return INV_DATE.get();
    }
    public void setINV_DATE(String INV_DATE) {
        this.INV_DATE.set(INV_DATE);
    }
    public StringProperty INV_DATEProperty() {
        return INV_DATE;
    }
    
    public String getMAWB_NO() {
        return MAWB_NO.get();
    }
    public void setMAWB_NO(String MAWB_NO) {
        this.MAWB_NO.set(MAWB_NO);
    }
    public StringProperty MAWB_NOProperty() {
        return MAWB_NO;
    }
    
    public String getHAWB_NO() {
        return HAWB_NO.get();
    }
    public void setHAWB_NO(String HAWB_NO) {
        this.HAWB_NO.set(HAWB_NO);
    }
    public StringProperty HAWB_NOProperty() {
        return HAWB_NO;
    }
    
    public String getFLT_NO() {
        return FLT_NO.get();
    }
    public void setFLT_NO(String FLT_NO) {
        this.FLT_NO.set(FLT_NO);
    }
    public StringProperty FLT_NOProperty() {
        return FLT_NO;
    }
    
    public String getFLT_DATE() {
        return FLT_DATE.get();
    }
    public void setFLT_DATE(String FLT_DATE) {
        this.FLT_DATE.set(FLT_DATE);
    }
    public StringProperty FLT_DATEProperty() {
        return FLT_DATE;
    }
    
    public String getFLT_DEST() {
        return FLT_DEST.get();
    }
    public void setFLT_DEST(String FLT_DEST) {
        this.FLT_DEST.set(FLT_DEST);
    }
    public StringProperty FLT_DESTProperty() {
        return FLT_DEST;
    }
    
    public String getCARTON_NO() {
        return CARTON_NO.get();
    }
    public void setCARTON_NO(String CARTON_NO) {
        this.CARTON_NO.set(CARTON_NO);
    }
    public StringProperty CARTON_NOProperty() {
        return CARTON_NO;
    }
    
    public String getPO_NO() {
        return PO_NO.get();
    }
    public void setPO_NO(String PO_NO) {
        this.PO_NO.set(PO_NO);
    }
    public StringProperty PO_NOProperty() {
        return PO_NO;
    }
    
    public String getPRD_NO() {
        return PRD_NO.get();
    }
    public void setPRD_NO(String PRD_NO) {
        this.PRD_NO.set(PRD_NO);
    }
    public StringProperty PRD_NOProperty() {
        return PRD_NO;
    }
    
    public String getLOT_TYPE() {
        return LOT_TYPE.get();
    }
    public void setLOT_TYPE(String LOT_TYPE) {
        this.LOT_TYPE.set(LOT_TYPE);
    }
    public StringProperty LOT_TYPEProperty() {
        return LOT_TYPE;
    }
    
    public String getLOT_NO() {
        return LOT_NO.get();
    }
    public void setLOT_NO(String LOT_NO) {
        this.LOT_NO.set(LOT_NO);
    }
    public StringProperty LOT_NOProperty() {
        return LOT_NO;
    }
    
    public String getSHIP_W_QTY() {
        return SHIP_W_QTY.get();
    }
    public void setSHIP_W_QTY(String SHIP_W_QTY) {
        this.SHIP_W_QTY.set(SHIP_W_QTY);
    }
    public StringProperty SHIP_W_QTYProperty() {
        return SHIP_W_QTY;
    }
    
    public String getSHIP_D_QTY() {
        return SHIP_D_QTY.get();
    }
    public void setSHIP_D_QTY(String SHIP_D_QTY) {
        this.SHIP_D_QTY.set(SHIP_D_QTY);
    }
    public StringProperty SHIP_D_QTYProperty() {
        return SHIP_D_QTY;
    }
    
    public String getSHP_PRD_NO() {
        return SHP_PRD_NO.get();
    }
    public void setSHP_PRD_NO(String SHP_PRD_NO) {
        this.SHP_PRD_NO.set(SHP_PRD_NO);
    }
    public StringProperty SHP_PRD_NOProperty() {
        return SHP_PRD_NO;
    }
    
    public String getCTM_DEVICE() {
        return CTM_DEVICE.get();
    }
    public void setCTM_DEVICE(String CTM_DEVICE) {
        this.CTM_DEVICE.set(CTM_DEVICE);
    }
    public StringProperty CTM_DEVICEProperty() {
        return CTM_DEVICE;
    }
    
    public String getCUSTOMER_LOT() {
        return CUSTOMER_LOT.get();
    }
    public void setCUSTOMER_LOT(String CUSTOMER_LOT) {
        this.CUSTOMER_LOT.set(CUSTOMER_LOT);
    }
    public StringProperty CUSTOMER_LOTProperty() {
        return CUSTOMER_LOT;
    }
    
    public String getUMC_INV_NO() {
        return UMC_INV_NO.get();
    }
    public void setUMC_INV_NO(String UMC_INV_NO) {
        this.UMC_INV_NO.set(UMC_INV_NO);
    }
    public StringProperty UMC_INV_NOProperty() {
        return UMC_INV_NO;
    }
    
    public String getREMARK() {
        return REMARK.get();
    }
    public void setREMARK(String REMARK) {
        this.REMARK.set(REMARK);
    }
    public StringProperty REMARKProperty() {
        return REMARK;
    }
    
    public String getWAFER_NO() {
        return WAFER_NO.get();
    }
    public void setWAFER_NO(String WAFER_NO) {
        this.WAFER_NO.set(WAFER_NO);
    }
    public StringProperty WAFER_NOProperty() {
        return WAFER_NO;
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
