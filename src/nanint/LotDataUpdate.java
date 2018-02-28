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
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This is the method for updating all lot data based on the imported lot process data from imported files
 * @author Grdan Andreas
 */
public class LotDataUpdate {
    
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
     * Update the lot data with new lots
     * Calculate the cycle times and data based on the other imported data
     * 
     */
    public void lotDataUpdate() {
        
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
