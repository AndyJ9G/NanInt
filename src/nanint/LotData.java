/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is the class for Lot Data definition
 * @author Grdan Andreas
 */
public class LotData { 
    private final StringProperty Lot; 
    private final StringProperty Product;
    private final StringProperty BasicType;
    private final StringProperty CreationDate;
    private final StringProperty FirstStepDate;
    private final StringProperty LastStepDate;
    private final StringProperty LastDate;
    
    /**
     * Default constructor
     */
    public LotData() {
        this(null, null, null, null, null, null, null);
    }
    
    /**
     * Constructor with some initial data
     * @param Lot
     * @param Product
     * @param BasicType
     * @param CreationDate
     * @param FirstStepDate
     * @param LastStepDate
     * @param LastDate 
     */
    public LotData(String Lot, String Product, String BasicType, String CreationDate, String FirstStepDate, String LastStepDate, String LastDate) { 
        this.Lot = new SimpleStringProperty(Lot); 
        this.Product = new SimpleStringProperty(Product);
        this.BasicType = new SimpleStringProperty(BasicType);
        this.CreationDate = new SimpleStringProperty(CreationDate);
        this.FirstStepDate = new SimpleStringProperty(FirstStepDate);
        this.LastStepDate = new SimpleStringProperty(LastStepDate);
        this.LastDate = new SimpleStringProperty(LastDate);
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
    
    public String getProduct() {
        return Product.get();
    }
    public void setProduct(String Product) {
        this.Product.set(Product);
    }
    public StringProperty ProductProperty() {
        return Product;
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
    
    public String getCreationDate() {
        return CreationDate.get();
    }
    public void setCreationDate(String CreationDate) {
        this.CreationDate.set(CreationDate);
    }
    public StringProperty CreationDateProperty() {
        return CreationDate;
    }
    
    public String getFirstStepDate() {
        return FirstStepDate.get();
    }
    public void setFirstStepDate(String FirstStepDate) {
        this.FirstStepDate.set(FirstStepDate);
    }
    public StringProperty FirstStepDateProperty() {
        return FirstStepDate;
    }
    
    public String getLastStepDate() {
        return LastStepDate.get();
    }
    public void setLastStepDate(String LastStepDate) {
        this.LastStepDate.set(LastStepDate);
    }
    public StringProperty LastStepDateProperty() {
        return LastStepDate;
    }
    
    public String getLastDate() {
        return LastDate.get();
    }
    public void setLastDate(String LastDate) {
        this.LastDate.set(LastDate);
    }
    public StringProperty LastDateProperty() {
        return LastDate;
    }
}
