/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the method for all database connections
 * @author Grdan Andreas
 */
public class DataBaseSQLite {
    // table structure
    // wip_nan
    private List<String> listTableWipNan = Arrays.asList("ReportDate", "Lot", "Owner", "Product", "HoldFlag", "WIP1", "WIP2", "Shrink", "FE_SITE", "BasicType",
            "Package", "Step", "WorkCenter", "SEQ", "Percentage", "ReportFileName");
    // tsmc_to_nan
    private List<String> listTableTSMC = Arrays.asList("Lot", "WaferPcs", "InvoiceNo", "InvoiceDate", "Forwarder", "MAWB", "HAWB", "ReportFileName", "Technology",
            "Product", "Product2000", "ETA");
    // gf_to_nan
    private List<String> listTableGF = Arrays.asList("ReportDate", "CustomerName", "ShipDate", "InvoiceNo", "InvoiceDate", "PO", "SO", "OrderDate",
            "ProcessName", "CustomerPartname", "InternalPartName", "LotId", "CustomerLotId", "LotType", "LotPriority", "AgreedGDPW", "CalculatedGDPL",
            "CycleTime", "BillQtyWfr", "WfrIds", "vBillDie", "ShipToLocation", "BillToLocation", "ETA", "ETD", "Forwarder", "HAWB", "MAWB", "FlightNo",
            "ConnectingFlightNo", "ReportFileName");
    // umci_to_nan
    private List<String> listTableUMCI = Arrays.asList("PART_DIV", "INV_NO", "SHPTO_ID", "INV_DATE", "MAWB_NO", "HAWB_NO", "FLT_NO", "FLT_DATE", "FLT_DEST",
            "CARTON_NO", "PO_NO", "PRD_NO", "LOT_TYPE", "LOT_NO", "SHIP_W_QTY", "SHIP_D_QTY", "SHP_PRD_NO", "CTM_DEVICE", "CUSTOMER_LOT", "UMC_INV_NO",
            "REMARK", "WAFER_NO", "ReportFileName");
    // file_list
    private List<String> listTableFileList = Arrays.asList("FILENAME");
    // lot_data
    private List<String> listTableLotData = Arrays.asList("Lot", "Product", "BasicType", "CreationDate", "FirstStepDate", "LastStepDate", "LastDate");

    // sql query statements      
    // sql for tsmc_to_nan
    private String sqlDropWIPnan = "DROP TABLE IF EXISTS wip_nan;";
    private String sqlCreateWIPnan = "CREATE TABLE wip_nan (ReportDate, Lot, Owner, Product, HoldFlag, WIP1, WIP2, Shrink, FE_SITE, BasicType, Package, Step,"
            + "WorkCenter, SEQ, Percentage, ReportFileName);";

    // sql for tsmc_to_nan
    private String sqlDropTSMC = "DROP TABLE IF EXISTS tsmc_to_nan;";
    private String sqlCreateTSMC = "CREATE TABLE tsmc_to_nan (Lot, WaferPcs, InvoiceNo, InvoiceDate, Forwarder, MAWB, HAWB, ReportFileName, Technology, "
            + "Product, Product2000, ETA);";

    // sql for gf_to_nan
    private String sqlDropGF = "DROP TABLE IF EXISTS gf_to_nan;";
    private String sqlCreateGF = "CREATE TABLE gf_to_nan (ReportDate, CustomerName, ShipDate, InvoiceNo, InvoiceDate, PO, SO, OrderDate, ProcessName,"
            + "CustomerPartname, InternalPartName, LotId, CustomerLotId, LotType, LotPriority, AgreedGDPW, CalculatedGDPL, CycleTime, BillQtyWfr, WfrIds,"
            + "vBillDie, ShipToLocation, BillToLocation, ETA, ETD, Forwarder, HAWB, MAWB, FlightNo, ConnectingFlightNo, ReportFileName);";

    // sql for umci_to_nan
    private String sqlDropUMCI = "DROP TABLE IF EXISTS umci_to_nan;";
    private String sqlCreateUMCI = "CREATE TABLE umci_to_nan (PART_DIV, INV_NO, SHPTO_ID, INV_DATE, MAWB_NO, HAWB_NO, FLT_NO, FLT_DATE, FLT_DEST, CARTON_NO,"
            + "PO_NO, PRD_NO, LOT_TYPE, LOT_NO, SHIP_W_QTY, SHIP_D_QTY, SHP_PRD_NO, CTM_DEVICE, CUSTOMER_LOT, UMC_INV_NO, REMARK, WAFER_NO, ReportFileName);";

    // sql for file_list
    private String sqlDropFileList = "DROP TABLE IF EXISTS file_list;";
    private String sqlCreateFileList = "CREATE TABLE file_list (FILENAME);";
    
    // sql for lot_data
    private String sqlDropLotData = "DROP TABLE IF EXISTS lot_data;";
    private String sqlCreateLotData = "CREATE TABLE lot_data (Lot, Product, BasicType, CreationDate, FirstStepDate, LastStepDate, LastDate);";
    
    /**
     * Connect to database
     * @return connection 
     */
    private Connection connect() {    
        // Database parameter
        String DB_PATH = "nanint.db";
        String DB_DRIVER = "jdbc:sqlite:";
        Connection connection = null;
        // connect to the database
        try {
            // create a connection to the database
            connection = DriverManager.getConnection(DB_DRIVER + DB_PATH);
            System.out.println("Connection to database, dp path: " + DB_PATH + ", db driver: " + DB_DRIVER);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
    
    /**
     * Create a new clean set of tables
     */
    public void runCreateNewDatabase() {
        // create instance of database
        DataBaseSQLite db = new DataBaseSQLite();
        
        // create new database tables
        // create WIP tables
        db.updateTable(sqlDropWIPnan, "drop wip_nan");
        System.out.println("Database table wip nan deleted");
        db.updateTable(sqlCreateWIPnan, "create wip_nan");
        System.out.println("Database table wip nan created");
        
        // create TSMC Ship tables
        db.updateTable(sqlDropTSMC, "drop tsmc_to_nan");
        System.out.println("Database table TSMC to nan deleted");
        db.updateTable(sqlCreateTSMC, "create tsmc_to_nan");
        System.out.println("Database table TSMC to nan created");
        
        // create GF Ship tables
        db.updateTable(sqlDropGF, "drop gf_to_nan");
        System.out.println("Database table GF to nan deleted");
        db.updateTable(sqlCreateGF, "create gf_to_nan");
        System.out.println("Database table GF to nan created");
        
        // create UMCI Ship tables
        db.updateTable(sqlDropUMCI, "drop umci_to_nan");
        System.out.println("Database table umci to nan deleted");
        db.updateTable(sqlCreateUMCI, "create umci_to_nan");
        System.out.println("Database table umci_to_nan created");
        
        // create FileList tables
        db.updateTable(sqlDropFileList, "drop file_list");
        System.out.println("Database table file list deleted");
        db.updateTable(sqlCreateFileList, "create file_list");
        System.out.println("Database table file list created");
        
        // create LotData tables
        db.updateTable(sqlDropLotData, "drop lot_data");
        System.out.println("Database table lot data deleted");
        db.updateTable(sqlCreateLotData, "create lot_data");
        System.out.println("Database table lot data created");
    }
    
    /**
     * Check table existence and table columns structure
     */
    public void runCheckDatabaseTables() {
        // create instance of database
        DataBaseSQLite db = new DataBaseSQLite();
        
        // check wip_nan table structure
        db.checkAddDatabaseTables("wip_nan", listTableWipNan);
        // check wip_nan table structure
        db.checkAddDatabaseTables("tsmc_to_nan", listTableTSMC);
        // check wip_nan table structure
        db.checkAddDatabaseTables("gf_to_nan", listTableGF);
        // check wip_nan table structure
        db.checkAddDatabaseTables("umci_to_nan", listTableUMCI);
        // check wip_nan table structure
        db.checkAddDatabaseTables("lot_data", listTableLotData);
    }
    
    /**
     * Check table existence and table columns structure
     * Add table columns if missing
     */
    public void checkAddDatabaseTables(String tableName, List<String> tableStructure) {
        // create instance of database
        DataBaseSQLite db = new DataBaseSQLite();

        // call pragma to get the column names
        ArrayList<String> pragmaTable = db.pragmaDatabaseTables(tableName);
        
        // loop through the ArrayList of Table Structure
        // Check if the names are in the ArrayList from PRAGMA table call
        for(String colName : tableStructure){
            // check if this name is inside of the Pragma-ArrayList
            if (pragmaTable.contains(colName)){
                // column name is existing, nothing to be done
            }else{
                // column not existing, needs to be added
                System.out.println("Table-Column: " + colName + " not existing in table: " + tableName + ". Need to be added.");
                // sql query preparation
                String sqlAddColumn = "ALTER TABLE " + tableName + " ADD COLUMN '" + colName + "';";
                // calling database update to add the column
                db.updateTable(sqlAddColumn, "Add column");
            }
        }
    }
    
    /**
     * Get table columns with PRAGMA sql query
     * @param tableName
     * @return ArrayList of Strings
     */
    public ArrayList<String> pragmaDatabaseTables(String tableName) {
        // create instance of database
        DataBaseSQLite db = new DataBaseSQLite();
        
        // check table structure with PRAGMA
        String sqlPragma = "PRAGMA table_info('" + tableName + "');";
        ArrayList<String> pragmaTable = new ArrayList<String>();
        
        // call the sql command and get the return set
        ResultSet rs = db.selectTableResultSet(sqlPragma);
        
        // loop through the result set and put name in the ArryList
        try {
            // loop 
            while (rs.next()) {
                pragmaTable.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Database PRAGMA table info from table: " + tableName + " executed");
        return pragmaTable;
    }
    
    /**
     * Call database update with the given sql query
     * @param sqlQuery
     * @param action 
     */
    public void updateTable(String sqlQuery, String action) {
        // connect to the database and run query
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);) {
            // execute database update
            pstmt.executeUpdate();
            System.out.println("Database update with query: " + sqlQuery + ", action: " + action);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Database Insert simple Data
     * @param dataValue
     * @param query
     * @param table 
     */
    public void insertIntoTable(String dataValue, String query, String table) {
        // connect to the database and prepare query
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);) {
            // update the query string with the data
            pstmt.setString(1,dataValue);
            // execute database update
            pstmt.executeUpdate();
            System.out.println("Insert data into database, table: " + table + ", query: " + query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Database Insert Array Data
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertArrayIntoTable(ArrayList<String> dataList, String query, String table) {
        // connect to the database and prepare query
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(query);) {
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // loop through the data of the ArrayList of Strings
            for (int i = 0; i<dataList.size(); i++) {
                // update the query string with the data
                pstmt.setString(i+1,dataList.get(i));
            }
            // execute database update
            pstmt.executeUpdate();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", query: " + query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } 

    /**
     * Database Insert Multiple Data Rows from ArrayList
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertBatchIntoTable(ArrayList<ArrayList<String>> dataList, String query, String table) {
        try {
            // connect to the database and prepare query
            Connection conn = this.connect();
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // insert the query into prepared statement
            PreparedStatement pstmt = conn.prepareStatement(query);
            // loop through ArrayLists of ArrayLists of Strings
            for (ArrayList<String> dataListRow : dataList) {
                // loop through the data of the ArrayList of Strings
                for (int i = 0; i<dataListRow.size(); i++) {
                    // update the query string parts with the data
                    pstmt.setString(i+1,dataListRow.get(i));
                }
                // add the prepared statement to a batch
                pstmt.addBatch();
            }
            // execute the full batch
            int[] inserted = pstmt.executeBatch();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", rows: " + inserted.length);
            // close the prepared statement
            pstmt.close();
            // close database connection
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Database Insert Multiple Data Rows from ArrayList for the Wip Nan Table
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertWipNanBatchIntoTable(ArrayList<ArrayList<String>> dataList, String query, String table) {
        try {
            // connect to the database and prepare query
            Connection conn = this.connect();
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // insert the query into prepared statement
            PreparedStatement pstmt = conn.prepareStatement(query);
            // loop through ArrayLists of ArrayLists of Strings
            for (ArrayList<String> dataListRow : dataList) {
                // update the query string parts with the data
                String dateWipReport = dataListRow.get(0).substring(0,8);
                pstmt.setString(1,dateWipReport); //ReportDate
                pstmt.setString(2,dataListRow.get(1)); //Lot
                pstmt.setString(3,dataListRow.get(2)); //Owner
                pstmt.setString(4,dataListRow.get(3)); //Product
                pstmt.setString(5,dataListRow.get(4)); //HoldFlag
                pstmt.setString(6,dataListRow.get(5)); //WIP1
                pstmt.setString(7,dataListRow.get(6)); //WIP2
                pstmt.setString(8,dataListRow.get(7)); //Shrink
                pstmt.setString(9,dataListRow.get(8)); //FE_SITE
                pstmt.setString(10,dataListRow.get(9)); //BasicType
                pstmt.setString(11,dataListRow.get(10)); //Package
                pstmt.setString(12,dataListRow.get(11)); //Step
                pstmt.setString(13,dataListRow.get(12)); //WorkCenter
                // check if the array list has all values
                // is must have 16 to have also SEQ and Percentage
                if(dataListRow.size()==16){
                    // update the query string parts with the data
                    pstmt.setString(14,dataListRow.get(13)); //SEQ
                    pstmt.setString(15,dataListRow.get(14)); //Percentage
                    pstmt.setString(16,dataListRow.get(15)); //ReportFileName
                }else{
                    // we are missing the values SEQ and Percentage
                    // this are the old reports without SEQ and Percentage
                    pstmt.setString(14,""); //SEQ
                    pstmt.setString(15,""); //Percentage
                    pstmt.setString(16,dataListRow.get(13)); //ReportFileName
                    System.out.println("Wip nan old CSV file with missing SEQ and Percentage");
                }
                // add the prepared statement to a batch
                pstmt.addBatch();
            }
            // execute the full batch
            int[] inserted = pstmt.executeBatch();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", rows: " + inserted.length);
            // close the prepared statement
            pstmt.close();
            // close database connection
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Database Insert Multiple Data Rows from ArrayList for the TSMC to Nan Table
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertTSMCtoNanBatchIntoTable(ArrayList<ArrayList<String>> dataList, String query, String table) {
        try {
            // connect to the database and prepare query
            Connection conn = this.connect();
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // insert the query into prepared statement
            PreparedStatement pstmt = conn.prepareStatement(query);
            // loop through ArrayLists of ArrayLists of Strings
            for (ArrayList<String> dataListRow : dataList) {
                // update the query string parts with the data
                //Lot, WaferPcs, Technology, InvoiceNo, InvoiceDate, Forwarder, MAWB, HAWB, ReportFileName, Product, Product2000, ETA
                pstmt.setString(1,dataListRow.get(0)); //Lot
                pstmt.setString(2,dataListRow.get(1)); //WaferPcs
                pstmt.setString(3,dataListRow.get(3)); //InvoiceNo
                pstmt.setString(4,dataListRow.get(4)); //InvoiceDate
                pstmt.setString(5,dataListRow.get(5)); //Forwarder
                pstmt.setString(6,dataListRow.get(6)); //MAWB
                pstmt.setString(7,dataListRow.get(7)); //HAWB
                pstmt.setString(8,dataListRow.get(8)); //ReportFileName
                pstmt.setString(9,dataListRow.get(2)); //Technology
                pstmt.setString(10,dataListRow.get(9)); //Product
                pstmt.setString(11,dataListRow.get(10)); //Product2000
                pstmt.setString(12,dataListRow.get(11)); //ETA
                // add the prepared statement to a batch
                pstmt.addBatch();
            }
            // execute the full batch
            int[] inserted = pstmt.executeBatch();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", rows: " + inserted.length);
            // close the prepared statement
            pstmt.close();
            // close database connection
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Database Insert Multiple Data Rows from ArrayList for the GF to Nan Table
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertGFtoNanBatchIntoTable(ArrayList<ArrayList<String>> dataList, String query, String table) {
        try {
            // connect to the database and prepare query
            Connection conn = this.connect();
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // insert the query into prepared statement
            PreparedStatement pstmt = conn.prepareStatement(query);
            // loop through ArrayLists of ArrayLists of Strings
            for (ArrayList<String> dataListRow : dataList) {
                // update the query string parts with the data
                pstmt.setString(1,dataListRow.get(0)); //ReportDate
                pstmt.setString(2,dataListRow.get(1)); //CustomerName
                pstmt.setString(3,dataListRow.get(2)); //ShipDate
                pstmt.setString(4,dataListRow.get(3)); //InvoiceNo
                pstmt.setString(5,dataListRow.get(4)); //InvoiceDate
                pstmt.setString(6,dataListRow.get(5)); //PO
                pstmt.setString(7,dataListRow.get(6)); //SO
                pstmt.setString(8,dataListRow.get(7)); //OrderDate
                pstmt.setString(9,dataListRow.get(8)); //ProcessName
                pstmt.setString(10,dataListRow.get(9)); //CustomerPartname
                pstmt.setString(11,dataListRow.get(10)); //InternalPartName
                pstmt.setString(12,dataListRow.get(11)); //LotId
                pstmt.setString(13,dataListRow.get(12)); //CustomerLotId
                pstmt.setString(14,dataListRow.get(13)); //LotType
                pstmt.setString(15,dataListRow.get(14)); //LotPriority
                pstmt.setString(16,dataListRow.get(15)); //AgreedGDPW
                pstmt.setString(17,dataListRow.get(16)); //CalculatedGDPL
                pstmt.setString(18,dataListRow.get(17)); //CycleTime
                pstmt.setString(19,dataListRow.get(18)); //BillQtyWfr
                pstmt.setString(20,dataListRow.get(19)); //WfrIds
                pstmt.setString(21,dataListRow.get(20)); //vBillDie
                pstmt.setString(22,dataListRow.get(21)); //ShipToLocation
                pstmt.setString(23,dataListRow.get(22)); //BillToLocation
                pstmt.setString(24,dataListRow.get(23)); //ETA
                pstmt.setString(25,dataListRow.get(24)); //ETD
                pstmt.setString(26,dataListRow.get(25)); //Forwarder
                pstmt.setString(27,dataListRow.get(26)); //HAWB
                pstmt.setString(28,dataListRow.get(27)); //MAWB
                pstmt.setString(29,dataListRow.get(28)); //FlightNo
                pstmt.setString(30,dataListRow.get(29)); //ConnectingFlightNo
                pstmt.setString(31,dataListRow.get(30)); //ReportFileName
                // add the prepared statement to a batch
                pstmt.addBatch();
            }
            // execute the full batch
            int[] inserted = pstmt.executeBatch();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", rows: " + inserted.length);
            // close the prepared statement
            pstmt.close();
            // close database connection
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } 

    /**
     * Database Insert Multiple Data Rows from ArrayList for the UMCI to Nan Table
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertUMCItoNanBatchIntoTable(ArrayList<ArrayList<String>> dataList, String query, String table) {
        try {
            // connect to the database and prepare query
            Connection conn = this.connect();
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // insert the query into prepared statement
            PreparedStatement pstmt = conn.prepareStatement(query);
            // loop through ArrayLists of ArrayLists of Strings
            for (ArrayList<String> dataListRow : dataList) {
                // update the query string parts with the data
                pstmt.setString(1,dataListRow.get(1)); //PART_DIV
                pstmt.setString(2,dataListRow.get(2)); //INV_NO
                pstmt.setString(3,dataListRow.get(3)); //SHPTO_ID
                pstmt.setString(4,dataListRow.get(4)); //INV_DATE
                pstmt.setString(5,dataListRow.get(5)); //MAWB_NO
                pstmt.setString(6,dataListRow.get(6)); //HAWB_NO
                pstmt.setString(7,dataListRow.get(7)); //FLT_NO
                pstmt.setString(8,dataListRow.get(8)); //FLT_DATE
                pstmt.setString(9,dataListRow.get(9)); //FLT_DEST
                pstmt.setString(10,dataListRow.get(10)); //CARTON_NO
                pstmt.setString(11,dataListRow.get(11)); //PO_NO
                pstmt.setString(12,dataListRow.get(12)); //PRD_NO
                pstmt.setString(13,dataListRow.get(13)); //LOT_TYPE
                pstmt.setString(14,dataListRow.get(14)); //LOT_NO
                pstmt.setString(15,dataListRow.get(15)); //SHIP_W_QTY
                pstmt.setString(16,dataListRow.get(16)); //SHIP_D_QTY
                pstmt.setString(17,dataListRow.get(17)); //SHP_PRD_NO
                pstmt.setString(18,dataListRow.get(18)); //CTM_DEVICE
                pstmt.setString(19,dataListRow.get(19)); //CUSTOMER_LOT
                pstmt.setString(20,dataListRow.get(20)); //UMC_INV_NO
                pstmt.setString(21,dataListRow.get(21)); //REMARK
                pstmt.setString(22,dataListRow.get(22)); //WAFER_NO
                pstmt.setString(23,dataListRow.get(23)); //ReportFileName
                // add the prepared statement to a batch
                pstmt.addBatch();
            }
            // execute the full batch
            int[] inserted = pstmt.executeBatch();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", rows: " + inserted.length);
            // close the prepared statement
            pstmt.close();
            // close database connection
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Database Insert Multiple Data Rows from ArrayList for the lot_data Table
     * call the full insertion without autocommit for database speed
     * @param dataList
     * @param query
     * @param table 
     */
    public void insertLotDataBatchIntoTable(ArrayList<ArrayList<String>> dataList, String query, String table) {
        try {
            // connect to the database and prepare query
            Connection conn = this.connect();
            // speeding up the database by setting autocommit to false
            conn.setAutoCommit(false);
            // insert the query into prepared statement
            PreparedStatement pstmt = conn.prepareStatement(query);
            // loop through ArrayLists of ArrayLists of Strings
            for (ArrayList<String> dataListRow : dataList) {
                // update the query string parts with the data
                pstmt.setString(1,dataListRow.get(0)); //Lot
                pstmt.setString(2,dataListRow.get(1)); //Product
                pstmt.setString(3,dataListRow.get(2)); //BasicType
                pstmt.setString(4,dataListRow.get(3)); //CreationDate
                pstmt.setString(5,dataListRow.get(4)); //FirstStepDate
                pstmt.setString(6,dataListRow.get(5)); //LastStepDate
                pstmt.setString(7,dataListRow.get(6)); //LastDate
                // add the prepared statement to a batch
                pstmt.addBatch();
            }
            // execute the full batch
            int[] inserted = pstmt.executeBatch();
            // closing the autocommit with commit
            conn.commit();
            System.out.println("Insert data into database, table: " + table + ", rows: " + inserted.length);
            // close the prepared statement
            pstmt.close();
            // close database connection
            conn.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Database query to get all file names from file table
     * @return ArrayList of Strings of file names
     */
    public ArrayList<String> checkFileNameExisting() {
        String sqlCheckFileName = "SELECT * FROM file_list;";
        ArrayList<String> fileName = new ArrayList<String>();
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlCheckFileName)) {
            // loop through the result set
            while (rs.next()) {
                fileName.add(rs.getString("FILENAME"));
            }
            System.out.println("Get the full file name list from database into Array.");
            return fileName;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    } 

    /**
     * Database Select with sql query
     * @param sqlSelect
     * @return ArrayList of ArrayList of Strings
     */
    public ArrayList<ArrayList<String>> selectTable(String sqlSelect) {
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlSelect)) {
            // setup the ArrayLists needed
            ArrayList<ArrayList<String>> databaseResult = new ArrayList<ArrayList<String>>();
            ArrayList<String> columnNames = new ArrayList<String>();
            // get the meta data from the result set
            ResultSetMetaData rsmd = rs.getMetaData();
            // we get the column count
            int columnCount = rsmd.getColumnCount();
            // we loop through the columns
            // The column count starts from 1
            for (int i = 1; i <= columnCount; i++ ) {
                // we get the column name
                String columnName = rsmd.getColumnName(i);
                // add column name to the ArrayList
                columnNames.add(columnName);
            }
            // we add the ArrayList to the ArrayList of ArrayList of Strings
            databaseResult.add(columnNames);
            // loop through the result set
            while (rs.next()) {
                ArrayList<String> rsValues = new ArrayList<String>();
                // we loop through the columns
                // The column count starts from 1
                for (int i = 1; i <= columnCount; i++ ) {
                    // we get the column name
                    String columnName = rsmd.getColumnName(i);
                    // we add the result based on the column name to the ArrayList
                    rsValues.add(rs.getString(columnName));
                }
                // we add the ArrayList to the ArrayList of ArrayList of Strings
                databaseResult.add(rsValues);
            }
            System.out.println("Return the database select as ArrayList from query: " + sqlSelect);
            return databaseResult;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    } 

    /**
     * Database query
     * @param sqlSelect
     * @return result set
     */
    public ResultSet selectTableResultSet(String sqlSelect) {
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlSelect);
            System.out.println("Return the database Result Set from query: " + sqlSelect);
            return rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    } 
    
    /**
     * Database query to get all lot names from wip_nan table
     * @return ArrayList of Strings of lot names
     */
    public ArrayList<String> getLotNamesWipNan() {
        String sqlGetLotNamesWipNan = "SELECT DISTINCT Lot FROM wip_nan;";
        ArrayList<String> lotNamesWipNan = new ArrayList<String>();
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlGetLotNamesWipNan)) {
            // loop through the result set
            while (rs.next()) {
                lotNamesWipNan.add(rs.getString("Lot"));
            }
            System.out.println("Get all lot names from table wip_nan into Array.");
            return lotNamesWipNan;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
