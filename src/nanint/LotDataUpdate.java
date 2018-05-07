/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.List;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.io.BufferedReader;
import java.sql.ResultSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the method for updating all lot data
 * We are updating the whole DB using also the new imported data
 * @author Grdan Andreas
 */
public class LotDataUpdate {
    
    //private static final Logger logger = ApplicationLogger.getInstance();
    private static Logger logger = LogManager.getLogger(LotDataUpdate.class.getName());
    
    // sql query statements
    // sql for lot data
    private String sqlGetLotData = "SELECT * FROM lot_data;";
    
    // sql for wip_nan
    private String sqlLotDataWipNan = "SELECT Lot, Product, BasicType FROM wip_nan GROUP BY Lot ORDER BY Lot ASC;";
    private String sqlCreationDate = "SELECT Lot, MIN(ReportDate) AS CreationDate FROM wip_nan GROUP BY Lot ORDER BY Lot ASC;";
    private String sqlFirstStepDate = "SELECT Lot, MAX(ReportDate) AS FirstStepDate FROM wip_nan WHERE Step = 'DieBank' GROUP BY Lot ORDER BY Lot ASC;";
    private String sqlLastStepDate = "SELECT Lot, MIN(ReportDate) AS LastStepDate FROM wip_nan WHERE Step LIKE '%Shipping' GROUP BY Lot ORDER BY Lot ASC;";
    private String sqlLastDate = "SELECT Lot, MAX(ReportDate) AS LastDate FROM wip_nan GROUP BY Lot ORDER BY Lot ASC;";
    
    // sql insert lot data
    private String sqlInsertLotData= "INSERT INTO lot_data VALUES (?, ?, ?, ?, ?, ?, ?);";
    private String sqlUpdateLotData= "UPDATE lot_data SET Product=? , BasicType=? , CreationDate=? , FirstStepDate=? , LastStepDate=? , LastDate=? WHERE Lot=?;";
    
    /**
     * Full database query
     * get all data into lists to combine the inputs
     */
    public void fullDataCheck(){
        logger.info("Class for Full Data Check");
        
        // create lists of all objects
        List<WipNan> wipList = new ArrayList<WipNan>();
        List<AMKORtoASE> amkorList = new ArrayList<AMKORtoASE>();
        List<TSMCtoNan> tsmcList = new ArrayList<TSMCtoNan>();
        List<UMCItoNan> umciList = new ArrayList<UMCItoNan>();
        List<GFtoNan> gfList = new ArrayList<GFtoNan>();
        
        // execute SQL query and fill the list with data from database
        // this is for wip_nan
        // we load all table entries into a list
        try{
            // execute the sql queries
            String wipSQL = "SELECT * FROM wip_nan;";
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet
            ResultSet rsWip = db.selectTableResultSet(wipSQL);

            // loop through result set
            while (rsWip.next()){
                //insert data set from result set into object
                WipNan wipNan = new WipNan();
                wipNan.setReportDate(rsWip.getString("ReportDate"));
                wipNan.setLot(rsWip.getString("Lot"));
                wipNan.setOwner(rsWip.getString("Owner"));
                wipNan.setProduct(rsWip.getString("Product"));
                wipNan.setHoldFlag(rsWip.getString("HoldFlag"));
                wipNan.setWIP1(rsWip.getString("WIP1"));
                wipNan.setWIP2(rsWip.getString("WIP2"));
                wipNan.setShrink(rsWip.getString("Shrink"));
                wipNan.setFE_SITE(rsWip.getString("FE_SITE"));
                wipNan.setBasicType(rsWip.getString("BasicType"));
                wipNan.setPackage(rsWip.getString("Package"));
                wipNan.setStep(rsWip.getString("Step"));
                wipNan.setWorkCenter(rsWip.getString("WorkCenter"));
                wipNan.setSEQ(rsWip.getString("SEQ"));
                wipNan.setPercentage(rsWip.getString("Percentage"));
                wipNan.setReportFileName(rsWip.getString("ReportFileName"));

                // add the created object into the list
                wipList.add(wipNan);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting wip_nan data into List.");             
        }
        System.out.println("wip_nan count:" + wipList.size());
        
        // execute SQL query and fill the list with data from database
        // this is for amkor_to_ase
        // we load all table entries into a list
        try{
            // execute the sql queries
            String amkorSQL = "SELECT * FROM amkor_to_ase;";
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet
            ResultSet rsAmk = db.selectTableResultSet(amkorSQL);

            // loop through result set
            while (rsAmk.next()){
                //insert data set from result set into object
                AMKORtoASE amkASE = new AMKORtoASE();
                amkASE.setShippingDate(rsAmk.getString("ShippingDate"));
                amkASE.setPackingNo(rsAmk.getString("PackingNo"));
                amkASE.setInvoiceNo(rsAmk.getString("InvoiceNo"));
                amkASE.setShipTo(rsAmk.getString("ShipTo"));
                amkASE.setMAWB(rsAmk.getString("MAWB"));
                amkASE.setCustomerPO(rsAmk.getString("CustomerPO"));
                amkASE.setDescriptionOfGoods(rsAmk.getString("DescriptionOfGoods"));
                amkASE.setPdfFileName(rsAmk.getString("PdfFileName"));
                amkASE.setDelivery(rsAmk.getString("Delivery"));
                amkASE.setBox(rsAmk.getString("Box"));
                amkASE.setMaterial(rsAmk.getString("Material"));
                amkASE.setDeviceName(rsAmk.getString("DeviceName"));
                amkASE.setPONo(rsAmk.getString("PONo"));
                amkASE.setDateCode(rsAmk.getString("DateCode"));
                amkASE.setFABno(rsAmk.getString("FABno"));
                amkASE.setControlCode(rsAmk.getString("ControlCode"));
                amkASE.setATPOno(rsAmk.getString("ATPOno"));
                amkASE.setFPO(rsAmk.getString("FPO"));
                amkASE.setMCitem(rsAmk.getString("MCitem"));
                amkASE.setQuantity(rsAmk.getString("Quantity"));
                amkASE.setReportFileName(rsAmk.getString("ReportFileName"));

                // add the created object into the list
                amkorList.add(amkASE);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting amkor_to_ase data into List.");             
        }
        System.out.println("amkor_to_ase count:" + amkorList.size());
        
        // execute SQL query and fill the list with data from database
        // this is for tsmc_to_nan
        // we load all table entries into a list
        try{
            // execute the sql queries
            String tsmcSQL = "SELECT * FROM tsmc_to_nan;";
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet
            ResultSet rsTsm = db.selectTableResultSet(tsmcSQL);

            // loop through result set
            while (rsTsm.next()){
                //insert data set from result set into object
                TSMCtoNan tsmcNa = new TSMCtoNan();
                tsmcNa.setLot(rsTsm.getString("Lot"));
                tsmcNa.setWaferPcs(rsTsm.getString("WaferPcs"));
                tsmcNa.setInvoiceNo(rsTsm.getString("InvoiceNo"));
                tsmcNa.setInvoiceDate(rsTsm.getString("InvoiceDate"));
                tsmcNa.setForwarder(rsTsm.getString("Forwarder"));
                tsmcNa.setMAWB(rsTsm.getString("MAWB"));
                tsmcNa.setHAWB(rsTsm.getString("HAWB"));
                tsmcNa.setReportFileName(rsTsm.getString("ReportFileName"));
                tsmcNa.setTechnology(rsTsm.getString("Technology"));
                tsmcNa.setProduct(rsTsm.getString("Product"));
                tsmcNa.setProduct2000(rsTsm.getString("Product2000"));
                tsmcNa.setETA(rsTsm.getString("ETA"));

                // add the created object into the list
                tsmcList.add(tsmcNa);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting tsmc_to_nan data into List.");             
        }
        System.out.println("tsmc_to_nan count:" + tsmcList.size());
        
        // execute SQL query and fill the list with data from database
        // this is for umci_to_nan List
        // we load all table entries into a list
        try{
            // execute the sql queries
            String umciSQL = "SELECT * FROM umci_to_nan;";
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet
            ResultSet rsUmc = db.selectTableResultSet(umciSQL);

            // loop through result set
            while (rsUmc.next()){
                //insert data set from result set into object
                UMCItoNan umciNa = new UMCItoNan();
                umciNa.setPART_DIV(rsUmc.getString("PART_DIV"));
                umciNa.setINV_NO(rsUmc.getString("INV_NO"));
                umciNa.setSHPTO_ID(rsUmc.getString("SHPTO_ID"));
                umciNa.setINV_DATE(rsUmc.getString("INV_DATE"));
                umciNa.setMAWB_NO(rsUmc.getString("MAWB_NO"));
                umciNa.setHAWB_NO(rsUmc.getString("HAWB_NO"));
                umciNa.setFLT_NO(rsUmc.getString("FLT_NO"));
                umciNa.setFLT_DATE(rsUmc.getString("FLT_DATE"));
                umciNa.setFLT_DEST(rsUmc.getString("FLT_DEST"));
                umciNa.setCARTON_NO(rsUmc.getString("CARTON_NO"));
                umciNa.setPO_NO(rsUmc.getString("PO_NO"));
                umciNa.setPRD_NO(rsUmc.getString("PRD_NO"));
                umciNa.setLOT_TYPE(rsUmc.getString("LOT_TYPE"));
                umciNa.setLOT_NO(rsUmc.getString("LOT_NO"));
                umciNa.setSHIP_W_QTY(rsUmc.getString("SHIP_W_QTY"));
                umciNa.setSHIP_D_QTY(rsUmc.getString("SHIP_D_QTY"));
                umciNa.setSHP_PRD_NO(rsUmc.getString("SHP_PRD_NO"));
                umciNa.setCTM_DEVICE(rsUmc.getString("CTM_DEVICE"));
                umciNa.setCUSTOMER_LOT(rsUmc.getString("CUSTOMER_LOT"));
                umciNa.setUMC_INV_NO(rsUmc.getString("UMC_INV_NO"));
                umciNa.setREMARK(rsUmc.getString("REMARK"));
                umciNa.setWAFER_NO(rsUmc.getString("WAFER_NO"));
                umciNa.setReportFileName(rsUmc.getString("ReportFileName"));

                // add the created object into the list
                umciList.add(umciNa);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on getting umci_to_nan data into List.");             
        }
        System.out.println("umci_to_nan count:" + umciList.size());
        
        // WipNan lot list
        // we filter the wip nan list to get the lot numbers
        List<String> wipLot = wipList.stream()          // create a list from stream
                .map(WipNan::getLot)                    // get Lot
                .distinct()                             // distinct, no double entries
                .collect(Collectors.toList());          // collect into new list
        
        System.out.println("Number of WipNan Lots:" + wipLot.size());
        
        // loop through the wip nan lot list
        for(String wLot : wipLot){
            // we create an optional amkor ASE object
            // we check which objects equals the wip nan lot list
            Optional<AMKORtoASE> amkWip = amkorList.stream()                        // create a list from stream
                .filter(lot -> wLot.equals(lot.getATPOno()))                 // filter the lots which equals the amkor lot
                .sorted(Comparator.comparing(AMKORtoASE::getShippingDate))        // sort by report date
                .findFirst();
            
            // check and print the new created amkor object
            if (amkWip.isPresent()){
                System.out.print("WipNan-Loop, Lot:" + wLot + "|");
                AMKORtoASE amkWipP = amkWip.get();
                System.out.print("ATPOno:" + amkWipP.getATPOno()+ "|");
                System.out.print(amkWipP.getShippingDate()+ "|");
                System.out.print(amkWipP.getPackingNo()+ "|");
                System.out.print(amkWipP.getInvoiceNo()+ "|");
                System.out.print(amkWipP.getShipTo()+ "|");
                System.out.print(amkWipP.getMAWB()+ "|");
                System.out.print(amkWipP.getDescriptionOfGoods()+ "|");
                System.out.print(amkWipP.getPONo()+ "|");
                System.out.print(amkWipP.getMCitem()+ "|");
                System.out.println();
            }
        }

        // AMKOR lot list
        // we filter the amkor list to get the lot numbers
        List<String> amkLot = amkorList.stream()        // create a list from stream
                .map(AMKORtoASE::getATPOno)             // get ATPOno
                .distinct()                             // distinct, no double entries
                .collect(Collectors.toList());          // collect into new list
        
        System.out.println("Number of AMKOR lots:" + amkLot.size());

        // loop through the amkor lot list
        for(String ATPOno : amkLot){            
            // we create a wip nan object
            // we check which objects equals the amkor lot list
            Optional<WipNan> wipAmkorFirst = wipList.stream()                        // create a list from stream
                .filter(lot -> ATPOno.equals(lot.getLot()))                 // filter the lots which equals the amkor lot
                .sorted(Comparator.comparing(WipNan::getReportDate))        // sort by report date
                .findFirst();                                               // get the first entry
            
            if (wipAmkorFirst.isPresent()){
                System.out.println("WipNan, ATP:" + ATPOno);
                WipNan result2 = wipAmkorFirst.get();
                System.out.print("WipNan First Process, Lot:" + result2.getLot() + "|");
                System.out.print(result2.getBasicType()+ "|");
                System.out.print(result2.getOwner()+ "|");
                System.out.print(result2.getProduct()+ "|");
                System.out.print(result2.getReportDate()+ "|");
                System.out.print(result2.getStep()+ "|");
                System.out.print(result2.getPercentage()+ "|");
                System.out.println();
            }
            
            // we create a wip nan object
            // we check which objects equals the amkor lot list
            Optional<WipNan> wipAmkorFirstNonDieBank = wipList.stream()                        // create a list from stream
                .filter(lot -> ATPOno.equals(lot.getLot()))
                .filter(proc -> !"DieBank".equals(proc.getStep()))// filter the lots which equals the amkor lot
                .sorted(Comparator.comparing(WipNan::getReportDate))        // sort by report date
                .findFirst();                                               // get the first entry
            
            if (wipAmkorFirstNonDieBank.isPresent()){
                WipNan result2 = wipAmkorFirstNonDieBank.get();
                System.out.print("WipNan Non DieBank, Lot:" + result2.getLot() + "|");
                System.out.print(result2.getBasicType()+ "|");
                System.out.print(result2.getOwner()+ "|");
                System.out.print(result2.getProduct()+ "|");
                System.out.print(result2.getReportDate()+ "|");
                System.out.print(result2.getStep()+ "|");
                System.out.print(result2.getPercentage()+ "|");
                System.out.println();
            }
            
            // we create a wip nan object list
            // we check which objects equals the amkor lot list
            List<WipNan> wipAmkorList = wipList.stream()                        // create a list from stream
                .filter(lot -> ATPOno.equals(lot.getLot()))
                .filter(proc -> !"RDLShipping".equals(proc.getStep()))
                .sorted(Comparator.comparing(WipNan::getReportDate))
                .collect(Collectors.toList());
            
            // we create a wip nan object
            // we check which objects equals the amkor lot list
            WipNan wipAmkorLastNonDieBank = wipAmkorList.stream()                        // create a list from stream
                .filter(lot -> ATPOno.equals(lot.getLot()))
                .sorted(Comparator.comparing(WipNan::getReportDate))
                .reduce((first, second) -> second).get();
            System.out.print("WipNan Last Non Shipping, Lot:" + wipAmkorLastNonDieBank.getLot() + "|");
            System.out.print(wipAmkorLastNonDieBank.getBasicType()+ "|");
            System.out.print(wipAmkorLastNonDieBank.getOwner()+ "|");
            System.out.print(wipAmkorLastNonDieBank.getProduct()+ "|");
            System.out.print(wipAmkorLastNonDieBank.getReportDate()+ "|");
            System.out.print(wipAmkorLastNonDieBank.getStep()+ "|");
            System.out.print(wipAmkorLastNonDieBank.getPercentage()+ "|");
            System.out.println();
            
            // we create a wip nan object list
            // we check which objects equals the amkor lot list
            List<WipNan> wipAmkorListLast = wipList.stream()                        // create a list from stream
                .filter(lot -> ATPOno.equals(lot.getLot()))
                .sorted(Comparator.comparing(WipNan::getReportDate))
                .collect(Collectors.toList());
            
            // we create a wip nan object
            // we check which objects equals the amkor lot list
            WipNan wipAmkorLast = wipAmkorListLast.stream()                        // create a list from stream
                .filter(lot -> ATPOno.equals(lot.getLot()))
                .sorted(Comparator.comparing(WipNan::getReportDate))
                .reduce((first, second) -> second).get();
            System.out.print("WipNan Last Process, Lot:" + wipAmkorLast.getLot() + "|");
            System.out.print(wipAmkorLast.getBasicType()+ "|");
            System.out.print(wipAmkorLast.getOwner()+ "|");
            System.out.print(wipAmkorLast.getProduct()+ "|");
            System.out.print(wipAmkorLast.getReportDate()+ "|");
            System.out.print(wipAmkorLast.getStep()+ "|");
            System.out.print(wipAmkorLast.getPercentage()+ "|");
            System.out.println();
        }
    }
    
    
    /**
     * Update the lot data with new lots
     * Calculate the cycle times and data based on the other imported data
     * 
     */
    public void lotDataUpdate() {
        logger.info("Class for lot Data Update");
        
        // create instance of database
        DataBaseSQLite db = new DataBaseSQLite();
        
        // create arraylist for lot data to be inserted
        ArrayList<ArrayList<String>> lotDataUpdateArray = new ArrayList<ArrayList<String>>();

        // get all lot data from lot_data
        ArrayList<ArrayList<String>> lotDataArray = new ArrayList<ArrayList<String>>();
        lotDataArray = db.selectTable(sqlGetLotData);
        
        // create hash set of lot number from lot_data
        HashSet<String> lotDataLotNumberHashSet = new HashSet<String>();
        // loop through all lots from lot data Array
        for (ArrayList<String> lotDataRow : lotDataArray) {
            // add lot number to hash set
            lotDataLotNumberHashSet.add(lotDataRow.get(0));
        }
        
        // now get all lot data from wip_nan
        // get the lot details of every lot
        ArrayList<ArrayList<String>> lotDetailsWipNanArray = new ArrayList<ArrayList<String>>();
        lotDetailsWipNanArray = db.selectTable(sqlLotDataWipNan);
        
        // get the CreationDate of every lot
        ArrayList<ArrayList<String>> lotCreationDateWipNanArray = new ArrayList<ArrayList<String>>();
        lotCreationDateWipNanArray = db.selectTable(sqlCreationDate);
        
        // get the FirstStepDate of every lot
        ArrayList<ArrayList<String>> lotFirstStepDateWipNanArray = new ArrayList<ArrayList<String>>();
        lotFirstStepDateWipNanArray = db.selectTable(sqlFirstStepDate);
        
        // get the LastStepDate of every lot
        ArrayList<ArrayList<String>> lotLastStepDateWipNanArray = new ArrayList<ArrayList<String>>();
        lotLastStepDateWipNanArray = db.selectTable(sqlLastStepDate);
        
        // get the LastDate of every lot
        ArrayList<ArrayList<String>> lotLastDateWipNanArray = new ArrayList<ArrayList<String>>();
        lotLastDateWipNanArray = db.selectTable(sqlLastDate);
        
        // create hash map of Product from wip_nan
        HashMap<String, String> lotProductWipNanHashSet = new HashMap<String, String>();
        // create hash map of BasicType from wip_nan
        HashMap<String, String> lotBasicTypeWipNanHashSet = new HashMap<String, String>();
        // create hash map of CreationDate from wip_nan
        HashMap<String, String> lotCreationDateWipNanHashSet = new HashMap<String, String>();
        // create hash map of FirstStepDate from wip_nan
        HashMap<String, String> lotFirstStepDateWipNanHashSet = new HashMap<String, String>();
        // create hash map of LastStepDate from wip_nan
        HashMap<String, String> lotLastStepDateWipNanHashSet = new HashMap<String, String>();
        // create hash map of LastDate from wip_nan
        HashMap<String, String> lotLastDateWipNanHashSet = new HashMap<String, String>();

        // loop through all lots from LotDetails Array
        for (ArrayList<String> lotDetailsWipNanRow : lotDetailsWipNanArray) {
            // add lot with Product to hash set
            lotProductWipNanHashSet.put(lotDetailsWipNanRow.get(0), lotDetailsWipNanRow.get(1)); //Lot, Product
            // add lot with BasicType to hash set
            lotBasicTypeWipNanHashSet.put(lotDetailsWipNanRow.get(0), lotDetailsWipNanRow.get(2)); //Lot, BasicType
        }
        // loop through all lots from CreationDate Array
        for (ArrayList<String> lotCreationDateWipNanRow : lotCreationDateWipNanArray) {
            // add lot to hash set
            lotCreationDateWipNanHashSet.put(lotCreationDateWipNanRow.get(0), lotCreationDateWipNanRow.get(1)); //Lot, CreationDate
        }
        // loop through all lots from FirstStepDate Array
        for (ArrayList<String> lotFirstStepDateWipNanRow : lotFirstStepDateWipNanArray) {
            // add lot to hash set
            lotFirstStepDateWipNanHashSet.put(lotFirstStepDateWipNanRow.get(0), lotFirstStepDateWipNanRow.get(1)); //Lot, CreationDate
        }
        // loop through all lots from LastStepDate Array
        for (ArrayList<String> lotLastStepDateWipNanRow : lotLastStepDateWipNanArray) {
            // add lot to hash set
            lotLastStepDateWipNanHashSet.put(lotLastStepDateWipNanRow.get(0), lotLastStepDateWipNanRow.get(1)); //Lot, CreationDate
        }
        // loop through all lots from LastDate Array
        for (ArrayList<String> lotLastDateWipNanRow : lotLastDateWipNanArray) {
            // add lot to hash set
            lotLastDateWipNanHashSet.put(lotLastDateWipNanRow.get(0), lotLastDateWipNanRow.get(1)); //Lot, CreationDate
        }
        
        // loop through all lots from lotDetail hash map
        for (ArrayList<String> lotDetailsWipNanRow : lotDetailsWipNanArray) {
            // create arraylist of strings to add all lot data
            ArrayList<String> lotAllDataWipNan = new ArrayList<String>();
            
            // get all lot details into array list
            // add Lot
            lotAllDataWipNan.add(lotDetailsWipNanRow.get(0));
            // add Product
            lotAllDataWipNan.add(lotDetailsWipNanRow.get(1));
            // add BasicType
            lotAllDataWipNan.add(lotDetailsWipNanRow.get(2));
            
            // check if CreationDate is available in hash set
            if(lotCreationDateWipNanHashSet.containsKey(lotDetailsWipNanRow.get(0))){
                // add CreationDate
                lotAllDataWipNan.add(lotCreationDateWipNanHashSet.get(lotDetailsWipNanRow.get(0)));
            }else{
                // add empty CreationDate
                lotAllDataWipNan.add("");
            }
            // check if FirstStepDate is available in hash set
            if(lotFirstStepDateWipNanHashSet.containsKey(lotDetailsWipNanRow.get(0))){
                // add FirstStepDate
                lotAllDataWipNan.add(lotFirstStepDateWipNanHashSet.get(lotDetailsWipNanRow.get(0)));
            }else{
                // add empty FirstStepDate
                lotAllDataWipNan.add("");
            }
            // check if LastStepDate is available in hash set
            if(lotLastStepDateWipNanHashSet.containsKey(lotDetailsWipNanRow.get(0))){
                // add LastStepDate
                lotAllDataWipNan.add(lotLastStepDateWipNanHashSet.get(lotDetailsWipNanRow.get(0)));
            }else{
                // add empty LastStepDate
                lotAllDataWipNan.add("");
            }
            // check if LastDate is available in hash set
            if(lotLastDateWipNanHashSet.containsKey(lotDetailsWipNanRow.get(0))){
                // add LastDate
                lotAllDataWipNan.add(lotLastDateWipNanHashSet.get(lotDetailsWipNanRow.get(0)));
            }else{
                // add empty LastDate
                lotAllDataWipNan.add("");
            }
            // finally add Lot on the last Array Position
            lotAllDataWipNan.add(lotDetailsWipNanRow.get(0));
            // add ArrayList of Strings to ArrayList of ArrayList of Strings
            lotDataUpdateArray.add(lotAllDataWipNan);
        }
        
        // create ArrayList of ArrayList of Strings for lot_data INSERT statement
        ArrayList<ArrayList<String>> lotDataInsertStatementArray = new ArrayList<ArrayList<String>>();
        // create ArrayList of ArrayList of Strings for lot_data UPDATE statement
        ArrayList<ArrayList<String>> lotDataUpdateStatementArray = new ArrayList<ArrayList<String>>();
        
        // loop through ArrayList of ArrayList of Strings with all lots from wip_nan ArrayList
        for (ArrayList<String> lotDataUpdateWipNanRow : lotDataUpdateArray) {
            // check if lot data lot number hash set contains this lot already
            if(lotDataLotNumberHashSet.contains(lotDataUpdateWipNanRow.get(0))){
                // create arraylist of strings to update lot data
                ArrayList<String> lotDataUpdateArrayString = new ArrayList<String>();
                // this lot is already in lot data list
                // so we only update the data of the lot
                // insert all data needed into ArrayList of Strings
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(1)); // Product
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(2)); // BasicType
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(3)); // CreationDate
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(4)); // FirstStepDate
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(5)); // LastStepDate
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(6)); // LastDate
                lotDataUpdateArrayString.add(lotDataUpdateWipNanRow.get(0)); // Lot
                // add full ArrayList of Strings to Batch Array
                lotDataUpdateStatementArray.add(lotDataUpdateArrayString);
            }else{
                // create arraylist of strings to insert lot data
                ArrayList<String> lotDataInsertArrayString = new ArrayList<String>();
                // this lot is not available in lot data list
                // so we create the lot with the correspponding data
                // insert all data needed into ArrayList of Strings
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(0)); // Lot
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(1)); // Product
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(2)); // BasicType
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(3)); // CreationDate
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(4)); // FirstStepDate
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(5)); // LastStepDate
                lotDataInsertArrayString.add(lotDataUpdateWipNanRow.get(6)); // LastDate
                // add full ArrayList of Strings to Batch Array
                lotDataInsertStatementArray.add(lotDataInsertArrayString);
            }
        }
        // insert ArrayList of ArrayList of Strings into lot_data with sql batch for UPDATE data
        System.out.println("Update batch sending for lot_data UPDATE.");
        db.insertLotDataBatchIntoTable(lotDataUpdateStatementArray, sqlUpdateLotData, "lot_data");
        
        // insert ArrayList of ArrayList of Strings into lot_data with sql batch for INSERT data
        System.out.println("Insert batch sending for lot_data INSERT.");
        db.insertLotDataBatchIntoTable(lotDataInsertStatementArray, sqlInsertLotData, "lot_data");
    }
}
