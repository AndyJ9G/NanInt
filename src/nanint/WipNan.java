/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for Wip Nan Data definition
 * @author Grdan Andreas
 */
public class WipNan {
    private final StringProperty ReportDate; 
    private final StringProperty Lot; 
    private final StringProperty Owner;
    private final StringProperty Product;
    private final StringProperty HoldFlag;
    private final StringProperty WIP1;
    private final StringProperty WIP2;
    private final StringProperty Shrink;
    private final StringProperty FE_SITE;
    private final StringProperty BasicType;
    private final StringProperty Package;
    private final StringProperty Step;
    private final StringProperty WorkCenter;
    private final StringProperty SEQ;
    private final StringProperty Percentage;
    private final StringProperty ReportFileName;
    
    /**
     * Default constructor
     */
    public WipNan() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
    
    /**
     * Constructor with some initial data
     * @param ReportDate
     * @param Lot
     * @param Owner
     * @param Product
     * @param HoldFlag
     * @param WIP1
     * @param WIP2
     * @param Shrink
     * @param FE_SITE
     * @param BasicType
     * @param Package
     * @param Step
     * @param WorkCenter
     * @param SEQ
     * @param Percentage
     * @param ReportFileName 
     */
    public WipNan(String ReportDate, String Lot, String Owner, String Product, String HoldFlag, String WIP1, String WIP2, String Shrink,
            String FE_SITE, String BasicType, String Package, String Step, String WorkCenter, String SEQ, String Percentage, String ReportFileName) {
        this.ReportDate = new SimpleStringProperty(ReportDate); 
        this.Lot = new SimpleStringProperty(Lot); 
        this.Owner = new SimpleStringProperty(Owner);
        this.Product = new SimpleStringProperty(Product);
        this.HoldFlag = new SimpleStringProperty(HoldFlag);
        this.WIP1 = new SimpleStringProperty(WIP1);
        this.WIP2 = new SimpleStringProperty(WIP2);
        this.Shrink = new SimpleStringProperty(Shrink);
        this.FE_SITE = new SimpleStringProperty(FE_SITE);
        this.BasicType = new SimpleStringProperty(BasicType);
        this.Package = new SimpleStringProperty(Package);
        this.Step = new SimpleStringProperty(Step);
        this.WorkCenter = new SimpleStringProperty(WorkCenter);
        this.SEQ = new SimpleStringProperty(SEQ);
        this.Percentage = new SimpleStringProperty(Percentage);
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
    
    public String getLot() {
        return Lot.get();
    }
    public void setLot(String Lot) {
        this.Lot.set(Lot);
    }
    public StringProperty LotProperty() {
        return Lot;
    }
    
    public String getOwner() {
        return Owner.get();
    }
    public void setOwner(String Owner) {
        this.Owner.set(Owner);
    }
    public StringProperty OwnerProperty() {
        return Owner;
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
    
    public String getHoldFlag() {
        return HoldFlag.get();
    }
    public void setHoldFlag(String HoldFlag) {
        this.HoldFlag.set(HoldFlag);
    }
    public StringProperty HoldFlagProperty() {
        return HoldFlag;
    }
    
    public String getWIP1() {
        return WIP1.get();
    }
    public void setWIP1(String WIP1) {
        this.WIP1.set(WIP1);
    }
    public StringProperty WIP1Property() {
        return WIP1;
    }
    
    public String getWIP2() {
        return WIP2.get();
    }
    public void setWIP2(String WIP2) {
        this.WIP2.set(WIP2);
    }
    public StringProperty WIP2Property() {
        return WIP2;
    }
    
    public String getShrink() {
        return Shrink.get();
    }
    public void setShrink(String Shrink) {
        this.Shrink.set(Shrink);
    }
    public StringProperty ShrinkProperty() {
        return Shrink;
    }
    
    public String getFE_SITE() {
        return FE_SITE.get();
    }
    public void setFE_SITE(String FE_SITE) {
        this.FE_SITE.set(FE_SITE);
    }
    public StringProperty FE_SITEProperty() {
        return FE_SITE;
    }
    
    public String getBasicType() {
        return BasicType.get();
    }
    public void setBasicType(String BasicType) {
        this.BasicType.set(BasicType);
    }
    public StringProperty BasicTypeProperty() {
        return BasicType;
    }
    
    public String getPackage() {
        return Package.get();
    }
    public void setPackage(String Package) {
        this.Package.set(Package);
    }
    public StringProperty PackageProperty() {
        return Package;
    }
    
    public String getStep() {
        return Step.get();
    }
    public void setStep(String Step) {
        this.Step.set(Step);
    }
    public StringProperty StepProperty() {
        return Step;
    }
    
    public String getWorkCenter() {
        return WorkCenter.get();
    }
    public void setWorkCenter(String WorkCenter) {
        this.WorkCenter.set(WorkCenter);
    }
    public StringProperty WorkCenterProperty() {
        return WorkCenter;
    }
    
    public String getSEQ() {
        return SEQ.get();
    }
    public void setSEQ(String SEQ) {
        this.SEQ.set(SEQ);
    }
    public StringProperty SEQProperty() {
        return SEQ;
    }
    
    public String getPercentage() {
        return Percentage.get();
    }
    public void setPercentage(String Percentage) {
        this.Percentage.set(Percentage);
    }
    public StringProperty PercentageProperty() {
        return Percentage;
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
