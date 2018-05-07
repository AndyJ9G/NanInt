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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * This is the method for importing all data to database from imported files
 * @author Grdan Andreas
 */
public class ReportImport {
    //private static final Logger logger = ApplicationLogger.getInstance();
    private static Logger logger = LogManager.getLogger(ReportImport.class.getName());
    
    // sql query statements      
    // sql for tsmc_to_nan
    private String sqlDropWIPnan = "DROP TABLE IF EXISTS wip_nan;";
    private String sqlCreateWIPnan = "CREATE TABLE wip_nan (ReportDate, Lot, Owner, Product, HoldFlag, WIP1, WIP2, Shrink, FE_SITE, BasicType, Package, Step, WorkCenter, SEQ, Percentage, ReportFileName);";
    private String sqlInsertWIPnan = "INSERT INTO wip_nan VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    // sql for tsmc_to_nan
    private String sqlDropTSMC = "DROP TABLE IF EXISTS tsmc_to_nan;";
    private String sqlCreateTSMC = "CREATE TABLE tsmc_to_nan (Lot, WaferPcs, InvoiceNo, InvoiceDate, Forwarder, MAWB, HAWB, ReportFileName, Technology, Product, Product2000, ETA);";
    private String sqlInsertTSMC= "INSERT INTO tsmc_to_nan VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    // sql for gf_to_nan
    private String sqlDropGF = "DROP TABLE IF EXISTS gf_to_nan;";
    private String sqlCreateGF = "CREATE TABLE gf_to_nan (ReportDate, CustomerName, ShipDate, InvoiceNo, InvoiceDate, PO, SO, OrderDate, ProcessName, CustomerPartname, InternalPartName, LotId, CustomerLotId, LotType, LotPriority, AgreedGDPW, CalculatedGDPL, CycleTime, BillQtyWfr, WfrIds, vBillDie, ShipToLocation, BillToLocation, ETA, ETD, Forwarder, HAWB, MAWB, FlightNo, ConnectingFlightNo, ReportFileName);";
    private String sqlInsertGF= "INSERT INTO gf_to_nan VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    // sql for umci_to_nan
    private String sqlDropUMCI = "DROP TABLE IF EXISTS umci_to_nan;";
    private String sqlCreateUMCI = "CREATE TABLE umci_to_nan (PART_DIV, INV_NO, SHPTO_ID, INV_DATE, MAWB_NO, HAWB_NO, FLT_NO, FLT_DATE, FLT_DEST, CARTON_NO, PO_NO, PRD_NO, LOT_TYPE, LOT_NO, SHIP_W_QTY, SHIP_D_QTY, SHP_PRD_NO, CTM_DEVICE, CUSTOMER_LOT, UMC_INV_NO, REMARK, WAFER_NO, ReportFileName);";
    private String sqlInsertUMCI= "INSERT INTO umci_to_nan VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    // sql for amkor_to_ase
    private String sqlDropAMKOR = "DROP TABLE IF EXISTS amkor_to_ase;";
    private String sqlCreateAMKOR = "CREATE TABLE amkor_to_ase (ShippingDate, PackingNo, InvoiceNo, ShipTo, MAWB, CustomerPO, DescriptionOfGoods, PdfFileName,"
            + "Delivery, Box, Material, DeviceName, PONo, DateCode, FABno, ControlCode, ATPOno, FPO, MCitem, Quantity, ReportFileName);";
    private String sqlInsertAMKOR = "INSERT INTO amkor_to_ase VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    // sql for file_list
    private String sqlDropFileList = "DROP TABLE IF EXISTS file_list;";
    private String sqlCreateFileList = "CREATE TABLE file_list (FILENAME);";
    private String sqlInsertFileList = "INSERT INTO file_list VALUES (?);";
    
    // create new ArrayList of Strings for method return
    ArrayList<String> repfeed = new ArrayList<String>();
    
    // The data as an observable list of AMKOR to ASE 
    ObservableList<AMKORtoASE> AMKORtoASElist  = FXCollections.observableArrayList();
    ObservableList<AMKORtoASE> AMKORtoASEfullList  = FXCollections.observableArrayList();
    
    // The data as an observable list of AMKOR pdf import 
    ObservableList<AMKORpdf> AMKORpdfList  = FXCollections.observableArrayList();
    ObservableList<AMKORpdf> AMKORpdfFullList  = FXCollections.observableArrayList();
    
    // The final data as an observable list of AMKOR to ASE
    ObservableList<AMKORtoASE> AMKORfinalList  = FXCollections.observableArrayList();

    /**
     * Run the full report import
     * Extract all attachments from the msg files
     * Read and import all csv and txt files
     * Delete the files and save the filename in the database
     */
    public void runReportImport() {
        
        logger.info("Run Report Import Class");
       
        // create instance of database
        DataBaseSQLite db = new DataBaseSQLite();
        
        // create new ArrayLists of ArrayLists of Strings
        ArrayList<ArrayList<String>> csvLineRowsWIP = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> csvAllLineRowsWIP = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> csvLineRowsGF = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> csvAllLineRowsGF = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> shipAlertLine = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> shipAlertAllLine = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> csvLineRowsUMCI = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> csvAllLineRowsUMCI = new ArrayList<ArrayList<String>>();
        
        // create the instances
        ReportImport repimp = new ReportImport();
        MsgMail msgmail = new MsgMail();
        
        // extract the attachments from Outlook msg into the folder
        // create ArrayList for number of files and attachments
        logger.info("Extract the attachments from Outlook msg into the folder");
        ArrayList<Integer> msgatt = new ArrayList<Integer>();
        // loop folder and get attachments extracted
        msgatt = msgmail.loopMsgAttFiles("wip-nan", "");
        logger.info("Loop folder and get attachments extracted for wip nan");
        // add the file number and attachment number to string and add to report array list
        repfeed.add("From folder wip-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        logger.info("From folder wip-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1)); 
        // loop folder and get attachments extracted
        msgatt = msgmail.loopMsgAttFiles("tsmc-to-nan", "");
        logger.info("Loop folder and get attachments extracted for tsmc-to-nan");
        // add the file number and attachment number to string and add to report array list
        repfeed.add("From folder tsmc-to-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        logger.info("From folder tsmc-to-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        // loop folder and get attachments extracted
        msgatt = msgmail.loopMsgAttFiles("gf-to-nan", "");
        logger.info("Loop folder and get attachments extracted for gf-to-nan");
        // add the file number and attachment number to string and add to report array list
        repfeed.add("From folder gf-to-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        logger.info("From folder gf-to-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        // loop folder and get attachments extracted
        msgatt = msgmail.loopMsgAttFiles("umci-to-nan", "");
        logger.info("Loop folder and get attachments extracted for umci-to-nan");
        // add the file number and attachment number to string and add to report array list
        repfeed.add("From folder umci-to-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        logger.info("From folder umci-to-nan, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        // loop folder and get attachments extracted
        msgatt = msgmail.loopMsgAttFiles("amkor-to-ase", "");
        logger.info("Loop folder and get attachments extracted for amkor-to-ase");
        // add the file number and attachment number to string and add to report array list
        repfeed.add("From folder amkor-to-ase, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        logger.info("From folder amkor-to-ase, found number of msg. files: " + msgatt.get(0) + ", extracted number of attachments: " + msgatt.get(1));
        
        // get the TXT data as an ArrayLists of ArrayLists of Strings
        // define the directory and the Regex for filename
        shipAlertLine = repimp.loopLineTXTFiles("tsmc-to-nan", "");
        logger.info("Get the TXT data as an ArrayLists of ArrayLists of Strings tsmc-to-nan");
        // add to ArrayLists of ArrayLists of Strings
        shipAlertAllLine.addAll(shipAlertLine);
        // get the size of the ArrayList with data and add to the report
        repfeed.add("Number of data points from tsmc to nan extracted and inserted into database: " + shipAlertAllLine.size());
        logger.info("Number of data points from tsmc to nan extracted and inserted into database: " + shipAlertAllLine.size());
        // insert ArrayList of ArrayList of Strings into tsmc-to-nan with sql batch
        db.insertTSMCtoNanBatchIntoTable(shipAlertAllLine, sqlInsertTSMC, "tsmc-to-nan");
        
        // get the CSV data from WIP as an ArrayLists of ArrayLists of Strings
        // define the directory and the Regex for filename
        csvLineRowsWIP = repimp.loopLineCSVFiles("wip-nan", "");
        logger.info("Get the CSV data from WIP as an ArrayLists of ArrayLists of Strings for wip-nan");
        // add to ArrayLists of ArrayLists of Strings
        csvAllLineRowsWIP.addAll(csvLineRowsWIP);
        // get the size of the ArrayList with data and add to the report
        repfeed.add("Number of data points from wip nan extracted and inserted into database: " + csvAllLineRowsWIP.size());
        logger.info("Number of data points from wip nan extracted and inserted into database: " + csvAllLineRowsWIP.size());
        // insert ArrayList of ArrayList of Strings into wip-nan with sql batch
        db.insertWipNanBatchIntoTable(csvAllLineRowsWIP, sqlInsertWIPnan, "wip-nan");
        
        // get the CSV data from GF as an ArrayLists of ArrayLists of Strings
        // define the directory and the Regex for filename
        csvLineRowsGF = repimp.loopLineCSVFiles("gf-to-nan", "");
        logger.info("Get the CSV data from WIP as an ArrayLists of ArrayLists of Strings for gf-to-nan");
        // add to ArrayLists of ArrayLists of Strings
        csvAllLineRowsGF.addAll(csvLineRowsGF);
        // get the size of the ArrayList with data and add to the report
        repfeed.add("Number of data points from gf to nan extracted and inserted into database: " + csvAllLineRowsGF.size());
        logger.info("Number of data points from gf to nan extracted and inserted into database: " + csvAllLineRowsGF.size());
        // insert ArrayList of ArrayList of Strings into gf-to-nan with sql batch
        db.insertBatchIntoTable(csvAllLineRowsGF, sqlInsertGF, "gf-to-nan");
        
        // get the CSV data from UMCI as an ArrayLists of ArrayLists of Strings
        // define the directory and the Regex for filename
        csvLineRowsUMCI = repimp.loopLineCSVFiles("umci-to-nan", "");
        logger.info("Get the CSV data from WIP as an ArrayLists of ArrayLists of Strings for umci-to-nan");
        // add to ArrayLists of ArrayLists of Strings
        csvAllLineRowsUMCI.addAll(csvLineRowsUMCI);
        // get the size of the ArrayList with data and add to the report
        repfeed.add("Number of data points from umci to nan extracted and inserted into database: " + csvAllLineRowsUMCI.size());
        logger.info("Number of data points from umci to nan extracted and inserted into database: " + csvAllLineRowsUMCI.size());
        // insert ArrayList of ArrayList of Strings into umci-to-nan with sql batch
        db.insertUMCItoNanBatchIntoTable(csvAllLineRowsUMCI, sqlInsertUMCI, "umci-to-nan");
        
        // get the Excel data from AMKOR as an Observable List
        // loop through XLS files
        // define the directory and the Regex for filename
        AMKORtoASElist.clear();
        AMKORtoASElist = repimp.loopAmkorXLSFiles("amkor-to-ase", ".xls", "");
        logger.info("Get the Excel data from AMKOR as an Observable List");
        // add to Observable List
        AMKORtoASEfullList.addAll(AMKORtoASElist);
        // get the size of the Observable List with data and add to the report
        repfeed.add("Number of data points from AMKOR to ASE extracted and inserted into database: " + AMKORtoASEfullList.size());
        logger.info("Number of data points from AMKOR to ASE extracted and inserted into database: " + AMKORtoASEfullList.size());
        
        // get the PDF data from AMKOR pdf import as an Observable List
        // loop through pdf files
        // define the directory and the Regex for filename
        AMKORpdfList.clear();
        AMKORpdfList = repimp.loopAmkorPDFfiles("amkor-to-ase", ".pdf", "PL ");
        logger.info("Get the PDF data from AMKOR pdf import as an Observable List");
        // add to Observable List
        AMKORpdfFullList.addAll(AMKORpdfList);
        // get the size of the Observable List with data and add to the report
        repfeed.add("Number of data points from AMKOR pdf extracted and inserted into database: " + AMKORpdfFullList.size());
        logger.info("Number of data points from AMKOR pdf extracted and inserted into database: " + AMKORpdfFullList.size());

        // combine AMKOR to ASE data with AMKOR pdf data
        AMKORfinalList = repimp.combineAMKORlists(AMKORpdfFullList, AMKORtoASEfullList);
        // insert Observable List into amkor-to-ase with sql batch
        db.insertAMKORtoASEBatchIntoTable(AMKORfinalList, sqlInsertAMKOR, "amkor-to-ase");
    }
    
    /**
     * Combine List of AMKOR shipment with List of AMKOR PDF shipment details
     * @param ObservableList<AMKORpdf>
     * @param ObservableList<AMKORtoASE>
     * @return ObservableList<AMKORtoASE>
     */
    public ObservableList<AMKORtoASE> combineAMKORlists(ObservableList<AMKORpdf> amPDF, ObservableList<AMKORtoASE> amASE){
        logger.info("Combine List of AMKOR shipment with List of AMKOR PDF shipment details");
        // The data as an observable list of AMKOR to ASE 
        ObservableList<AMKORtoASE> AMKORfinalASEList  = FXCollections.observableArrayList();

        // loop through AMKOR pdf List
        logger.info("loop through AMKOR pdf List");
        for(AMKORpdf a : amPDF){
            // get the matching list of AMKOR shipments
            List<AMKORtoASE> am = amASE.stream()
                    .filter(p -> p.getDelivery().equals(a.getPackingNo())).collect(Collectors.toList());
            
            // loop through matching list
            logger.info("loop through matching list");
            for(AMKORtoASE ams : am){
                // add value to AMKOR ASE list
                ams.setShippingDate(a.getShippingDate());

                ams.setShippingDate(a.getShippingDate());
                ams.setPackingNo(a.getPackingNo());
                ams.setInvoiceNo(a.getInvoiceNo());
                ams.setShipTo(a.getShipTo());
                ams.setMAWB(a.getMAWB());
                ams.setCustomerPO(a.getCustomerPO());
                ams.setDescriptionOfGoods(a.getDescriptionOfGoods());
                ams.setPdfFileName(a.getPdfFileName());
                AMKORfinalASEList.add(ams);
                logger.debug(ams.toString());
            }
        }
        return AMKORfinalASEList;
    }
    
    /**
     * Loop through PDF files of specified directory
     * Find the files with corresponding ending
     * @param directory
     * @param Regex
     * @return Array List of an Array List of Strings
     */
    public ObservableList<AMKORpdf> loopAmkorPDFfiles(String directory, String fileExtension, String Regex){
        logger.info("Loop through PDF files of specified directory");
        // create new object
        ReportImport repimp = new ReportImport();
        // The data as an observable list of AMKOR pdf 
        ObservableList<AMKORpdf> AMKORpdfList  = FXCollections.observableArrayList();
        
        LoopDirectory lopdir = new LoopDirectory();
        // loop directory with all pdf files
        File[] fileLocations = lopdir.loopDirectorySpecific(directory, fileExtension, Regex);
        // check if file already processed, in file_name table of database
        // connect to database
        DataBaseSQLite db = new DataBaseSQLite();
        // check if file in file_name
        ArrayList<String> fileArray = new ArrayList<String>();
        fileArray = db.checkFileNameExisting();
        // loop through files
        for (File file : fileLocations) {
            // check if file already processed, compare file name with file list
            // get filename from path
            String fileName = getFileNameFromPath(file.toString());
            // check if file is already in list
            if(fileArray.contains(fileName)) {
                System.out.println("File already imported, no need for skipping, PDF file only appending data: " + file.toString());
                logger.info("File already imported, no need for skipping, PDF file only appending data: " + file.toString());
            }
            // read the file
            try{
                // read the pdf file
                logger.info("Read PDF file :" + file);
                // create instance of excel import
                AMKORpdfBox pdfimp = new AMKORpdfBox();

                // The data as an observable list of AMKOR pdf
                AMKORpdf AMKORpdfFileList  = new AMKORpdf();

                // open pdf from input file
                System.out.println("Open pdf File.");
                // import lines to observable list
                //pdfimp.pdfImportAMKOR(file);
                AMKORpdfFileList = pdfimp.pdfImportAMKOR(file);
                // insert file into table file_list and delete the file
                repimp.insertAndDeleteFileNameIntoTracking(file.toString());
                // add objects to observable list
                AMKORpdfList.addAll(AMKORpdfFileList);

            }catch(FileNotFoundException e){
                System.out.println("FileNotFoundException:" + e);
                logger.error("FileNotFoundException:" + e);
            }catch(IOException e){
                System.out.println("IOException:" + e);
                logger.error("IOException:" + e);
            }
        }
        // return the observable list
        return AMKORpdfList;
    }
    
    /**
     * Loop through XLS files of specified directory
     * Find the files with corresponding ending
     * @param directory
     * @param Regex
     * @return Array List of an Array List of Strings
     */
    public ObservableList<AMKORtoASE> loopAmkorXLSFiles(String directory, String fileExtension, String Regex){
        logger.info("Loop through XLS files of specified directory");
        // create new object
        ReportImport repimp = new ReportImport();
        
        // The data as an observable list of AMKOR to ASE 
        ObservableList<AMKORtoASE> AMKORtoASEList  = FXCollections.observableArrayList();
        
        LoopDirectory lopdir = new LoopDirectory();
        // loop directory with all xls files
        File[] fileLocations = lopdir.loopDirectorySpecific(directory, fileExtension, Regex);
        // check if file already processed, in file_name table of database
        // connect to database
        DataBaseSQLite db = new DataBaseSQLite();
        // check if file in file_name
        ArrayList<String> fileArray = new ArrayList<String>();
        fileArray = db.checkFileNameExisting();
        // loop through files
        for (File file : fileLocations) {
            // check if file already processed, compare file name with file list
            // get filename from path
            String fileName = getFileNameFromPath(file.toString());
            // check if file is already in list
            if(fileArray.contains(fileName)) {
                System.out.println("File already imported, skipping: " + file.toString());
                logger.info("File already imported, skipping: " + file.toString());
                // call delete file mathod
                DeleteFile fileDel = new DeleteFile();
                fileDel.deleteSpecifiedFile(file.toString());
            } else {
                // read the file
                try{
                    // read the xls file
                    logger.info("Read XLS File :" + file);
                    // create instance of excel import
                    AmkorExcelImport eximp = new AmkorExcelImport();

                    // The data as an observable list of AMKOR to ASE 
                    ObservableList<AMKORtoASE> AMKORtoASEfileList  = FXCollections.observableArrayList();
                    
                    // create input stream from file
                    FileInputStream inputStream = new FileInputStream(file);
                    System.out.println("Open input stream from XLS file.");

                    // open workbook from input stream file
                    Workbook workbook = new HSSFWorkbook(inputStream);
                    System.out.println("Open Workbook.");
                    // import lines to observable list
                    AMKORtoASEfileList = eximp.excelImportAMKORtoASE(workbook, file);
                    // insert file into table file_list and delete the file
                    repimp.insertAndDeleteFileNameIntoTracking(file.toString());
                    // add objects to observable list
                    AMKORtoASEList.addAll(AMKORtoASEfileList);

                    // close input stream
                    inputStream.close();
                    System.out.println("InputStream closed");
                }catch(FileNotFoundException e){
                    System.out.println("FileNotFoundException:" + e);
                    logger.error("FileNotFoundException:" + e);
                }catch(IOException e){
                    System.out.println("IOException:" + e);
                    logger.error("IOException:" + e);
                }
            }
        }
        // return the observable list
        return AMKORtoASEList;
    }
    
    /**
     * Loop through CSV files of specified directory
     * Find the files with corresponding ending
     * @param directory
     * @param Regex
     * @return Array List of an Array List of Strings
     */
    public ArrayList<ArrayList<String>> loopLineCSVFiles(String directory, String Regex) {
        logger.info("Loop through CSV files of specified directory");
        ArrayList<ArrayList<String>> allFileRows = new ArrayList<ArrayList<String>>();
        // create new object
        ReportImport repimp = new ReportImport();
        LoopDirectory lopdir = new LoopDirectory();
        // loop directory with all csv files
        File[] fileLocations = lopdir.loopDirectorySpecific(directory, ".csv", Regex);
        // check if file already processed, in file_name table of database
        // connect to database
        DataBaseSQLite db = new DataBaseSQLite();
        // check if file in file_name
        ArrayList<String> fileArray = new ArrayList<String>();
        fileArray = db.checkFileNameExisting();
        // loop through files
        for (File file : fileLocations) {
            // check if file already processed, compare file name with file list
            // get filename from path
            String fileName = getFileNameFromPath(file.toString());
            // check if file is already in list
            if(fileArray.contains(fileName)) {
                System.out.println("File already imported, skipping: " + file.toString());
                logger.info("File already imported, skipping: " + file.toString());
                // call delete file mathod
                DeleteFile fileDel = new DeleteFile();
                fileDel.deleteSpecifiedFile(file.toString());
            } else {
                // read all csv files
                logger.info("Read CSV File :" + file);
                // get the data as ArrayList of ArrayList of Strings
                ArrayList<ArrayList<String>> allRows = repimp.readLineCSV(file.toString());
                // insert file into table file_list and delete the file
                repimp.insertAndDeleteFileNameIntoTracking(file.toString());
                // add the data to the StringArray list
                allFileRows.addAll(allRows);
            }
        }
        System.out.println("Return data from all .csv files.");
        return allFileRows;
    }

    /**
     * Loop through TXT files of specified directory
     * Find the files with corresponding ending
     * @param directory
     * @param Regex
     * @return Array List of an Array List of Strings
     */
    public ArrayList<ArrayList<String>> loopLineTXTFiles(String directory, String Regex) {
        logger.info("Loop through TXT files of specified directory");
        ArrayList<ArrayList<String>> shipAlert = new ArrayList<ArrayList<String>>();
        // create new object
        ReportImport repimp = new ReportImport();
        LoopDirectory lopdir = new LoopDirectory();
        // loop directory with all txt files
        File[] fileLocations = lopdir.loopDirectorySpecific(directory, ".txt", Regex);
        // check if file already processed, in file_name table of database
        // connect to database
        DataBaseSQLite db = new DataBaseSQLite();
        // check if file in file_name, get the full list of file names
        ArrayList<String> fileArray = new ArrayList<String>();
        fileArray = db.checkFileNameExisting();
        // how many txt files are new and processed
        int newtxt = 0;
        int oldtxt = 0;
        // loop through files
        for (File file : fileLocations) {
            // check if file already processed, compare file name with file list
            // get filename from path
            String fileName = getFileNameFromPath(file.toString());
            // check if file is already in list
            if(fileArray.contains(fileName)) {
                // count the old txt files
                oldtxt = oldtxt + 1;
                System.out.println("File already imported, skipping: " + file.toString());
                logger.info("File already imported, skipping: " + file.toString());
                // call delete file mathod
                DeleteFile fileDel = new DeleteFile();
                fileDel.deleteSpecifiedFile(file.toString());
            } else {
                logger.info("Read file :" + file);
                // count the new txt files
                newtxt = newtxt + 1;
                // read the txt file
                ArrayList<ArrayList<String>> shipAlertRows = repimp.readShipAlertTXT(file.toString());
                // insert file into table file_list and delete the file
                repimp.insertAndDeleteFileNameIntoTracking(file.toString());
                // add the data to the StringArray list
                shipAlert.addAll(shipAlertRows);                
            }
        }
        // add the file number count to report array list
        repfeed.add("From txt files found new: " + newtxt + ", and skipped old: " + oldtxt);
        System.out.println("Return data from all .txt files.");
        // return the data from files
        return shipAlert;
    }
    
    /**
     * OpenCSV CSV Reader
     * Read the file without the header
     * Loop through each line and add the filename as date at the beginning
     * @param fileLocation
     * @return ArrayList of an ArrayList of Strings
     */
    public ArrayList<ArrayList<String>> readLineCSV(String fileLocation) {
        logger.info("Read Line CSV");
        // create ReportImport instance
        ReportImport repimp = new ReportImport();
        // create List of List of Strings
        ArrayList<ArrayList<String>> fullList = new ArrayList<ArrayList<String>>();
        // read CSV but exclude first line (header)
        try (CSVReader reader = new CSVReader(new FileReader(fileLocation), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1)) {
            logger.info("Read CSV File with CSV Reader :" + fileLocation);
            // read all lines and store in list of string arrays
            String[] nextLine;
            // get the filename from file path
            String fileName = repimp.getFileNameFromPath(fileLocation);
            // loop through the lines
            while ((nextLine = reader.readNext()) != null) {
                // create the List
                ArrayList<String> fullLine = new ArrayList<String>();
                // insert the line to the List
                fullLine.addAll(Arrays.asList(nextLine));
                // insert the filename as date as first list item
                // get the date out of the filename
                // get date from filename
                String fileDate = repimp.readDatefromCSVFileName(fileLocation);
                // add date to beginning of ArrayList
                fullLine.add(0,fileDate);
                // get the filename from file path
                fileName = repimp.getFileNameFromPath(fileLocation);
                // add filename to end of ArrayList
                fullLine.add(fileName);
                // insert the whole line to the List
                fullList.add(fullLine);
            }
            System.out.println("Read data from file: " + fileLocation);
            return fullList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * Get the date string out of the full filename of the CSV file
     * @param filePath
     * @return date string
     */
    public String readDatefromCSVFileName(String filePath) {
        logger.info("Read date from file name");
        // get the date out of the filename by splitting using '_' delimiter
        // the last part is the date
        String fileName = filePath.substring(filePath.lastIndexOf("_") + 1);
        // remove the ".csv" ending
        String dateFileName = fileName.replace(".csv", "");
        return dateFileName;
    }

    /**
     * Get data from TXT file of ShipALert
     * Parse all needed data
     * @param filePath
     * @return ArrayList of an ArrayList of Strings
     */
    public ArrayList<ArrayList<String>> readShipAlertTXT(String filePath) {
        logger.info("Get data from TXT file of ShipALert");
        // create new instance
        ReportImport repimp = new ReportImport();
        // get the filename from file path
        String fileName = repimp.getFileNameFromPath(filePath);
        // create the ArrayList of Strings to store the Shipment Data
        ArrayList<String> shipData = new ArrayList<String>();
        // create the variable for storing the number of lots
        int lotNumber = 0;
        int lotTSMCNumber = 0;
        // create the ArrayList of ArrayList of Strings to store the full Lot Data
        ArrayList<ArrayList<String>> lotDataAll = new ArrayList<ArrayList<String>>();
        // create the ArrayList of ArrayList of Strings to store the TSMC to Customer Lot Data
        ArrayList<ArrayList<String>> lotTSMCDataAll = new ArrayList<ArrayList<String>>();
        // create the ArrayList of ArrayList of Strings to store the full Shipment Lot Data
        ArrayList<ArrayList<String>> shipDataAll = new ArrayList<ArrayList<String>>();
        // create all variables needed
        String invoiceNo = "";
        String invoiceDate = "";
        String forwarderTrim = "";
        String mawbTrim = "";
        String hawbTrim = "";
        String productNumber = "";
        String product2000Number = "";
        String ETA = "";
        // line number
        int lineNumber = 0;
        int custLotNumberStartLine = 0;
        int custLotNumberEndLine = 0;
        int productNumberLine = 0;
        int product2000NumberLine = 0;
        // read buffered txt file
        logger.info("Read buffered TXT file :" + filePath);
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // loop through file line by line
            logger.info("Loop through file line by line");
            for(String line; (line = br.readLine()) != null; ) {
                // count the line number
                lineNumber = lineNumber + 1;
                // process the line, get product number
                if (line.contains("12\" WAFER")) {
                    // one line after this we have the product number line
                    productNumberLine = lineNumber + 1;
                    // one line after we have the 2000 product number line
                    product2000NumberLine = lineNumber + 2;
                }
                // process the line, get the product number
                if (productNumberLine == lineNumber) {
                    // we have the right line for the product number
                    Scanner scan = new Scanner(line);
                    ArrayList<String> stringList = new ArrayList<String>();
                    // split
                    while(scan.hasNext()){
                        stringList.add(scan.next());
                    }
                    // get the second part as product number
                    productNumber = stringList.get(1);
                }
                // process the line, get the product 2000 number
                if (product2000NumberLine == lineNumber) {
                    // we have the right line for the product 2000 number
                    Scanner scan = new Scanner(line);
                    ArrayList<String> stringList = new ArrayList<String>();
                    // split
                    while(scan.hasNext()){
                        stringList.add(scan.next());
                    }
                    // get the first part as product 2000 number
                    product2000Number = stringList.get(0);
                }
                // process the line, get invoice data
                if (line.contains("INVOICE NO.:")) {
                    // cut away the first part
                    String cutAwayFirst = line.split("INVOICE NO.:")[1];
                    // get the Invoice No
                    String getFirst = cutAwayFirst.split("INVOICE DATE:")[0];
                    // get the Invoice date
                    String getLast = cutAwayFirst.split("INVOICE DATE:")[1];
                    // trim the Invoice No
                    invoiceNo = getFirst.trim();
                    // get the Invoice date
                    invoiceDate = getLast.trim();
                }
                // process the line, get lot data
                if (line.contains("PO Line:")) {
                    // create the ArrayList of Strings to store the Lot Data
                    ArrayList<String> lotData = new ArrayList<String>();
                    // lot found, we count the lot number
                    lotNumber = lotNumber + 1;
                    // get the first part with PCS
                    String cutAwayFirst = line.split("PCS")[0];
                    // trim the string
                    String getFirst = cutAwayFirst.trim();
                    // get the pcs as last index
                    String getPCSValue = getFirst.substring(getFirst.lastIndexOf(" "));
                    // trim the pcs
                    String getPCSTrim = getPCSValue.trim();
                    // get the second part after PCS
                    String getTechnology = line.split("PCS")[1];
                    // trim the technology
                    String getTechnologyTrimmed = getTechnology.trim();
                    // get the first part before "*" wildcard
                    String getTSMCLotNumber = line.split("\\*")[0];
                    // trim the tsmc lot number string
                    String getTSMCLotTrim = getTSMCLotNumber.trim();
                    // get the tsmc lot number as last index
                    String getTSMCLotValue = getTSMCLotTrim.substring(getTSMCLotTrim.lastIndexOf(" "));
                    // trim the final tsmc lot number
                    String getTSMCLotTrimmed = getTSMCLotValue.trim();
                    // add the lot data to the ArrayList of lots
                    //lotData.add(getLotTrim);
                    lotData.add(getTSMCLotTrimmed);
                    lotData.add(getPCSTrim);
                    lotData.add(getTechnologyTrimmed);
                    // add the lot data ArrayList to the full ArrayList
                    lotDataAll.add(lotData);
                }
                // process the line, get forwarder
                if (line.contains("FORWARDER:")) {
                    // get the second part with FORWARDER
                    String forwarder = line.split("FORWARDER:")[1];
                    // trim the forwarder
                    forwarderTrim = forwarder.trim();
                }
                // process the line, get the MAWB
                if (line.contains("MAWB NO. :")) {
                    // get the second part with MAWB
                    String mawb = line.split("MAWB NO. :")[1];
                    // trim the mawb
                    mawbTrim = mawb.trim();
                }
                // process the line, get the HAWB
                if (line.contains("HAWB NO. :")) {
                    // get the second part with HAWB
                    String hawb = line.split("HAWB NO. :")[1];
                    // trim the hawb
                    hawbTrim = hawb.trim();
                }
                // process the line, get the ETA
                if (line.contains("ETA      :")) {                   
                    // cut away the first part
                    String cutAwayFirstETA = line.split("ETA      :")[1];
                    // trim the first part
                    String ETAtrim = cutAwayFirstETA.trim();
                    // get the ETA
                    String getETA = ETAtrim.split(" ")[0];
                    // trim the final ETA
                    ETA = getETA.trim();
                }
                // process the line, get the line number where the customer lots start
                if (line.contains("CUST LOT NO")) {
                    // remember the starting line number
                    custLotNumberStartLine = lineNumber;
                }
                // process the line, get the line number where the customer lots ends
                // trim the line to be empty
                String emptyLine = line.trim();
                // check if we found the customer lot number start
                // only then the first empty line is the customer end line
                if (emptyLine.isEmpty() && custLotNumberStartLine != 0 ) {
                    // remember the ending line number
                    custLotNumberEndLine = lineNumber;
                }
                // print the customer lot line
                // check if the line number is bigger than the start line and the start line is not null
                // and the end line is still 0
                if (lineNumber>custLotNumberStartLine && custLotNumberStartLine != 0 && custLotNumberEndLine == 0 ) {
                        // create the ArrayList of Strings to store the TSMC to Customer Lot Data
                        ArrayList<String> lotTSMCData = new ArrayList<String>();
                        // lot found, we count the lot number
                        lotTSMCNumber = lotTSMCNumber + 1;
                        // trim the line
                        String getTrimmed = line.trim();
                        // get the lot as last index
                        String getLotValue = getTrimmed.substring(getTrimmed.lastIndexOf(" "));
                        // get the tsmc lot as first index
                        String getTSMCLotValue = getTrimmed.split(" ")[0];
                        // trim the TSMC final Lot
                        String getTSMCLotTrimmed = getTSMCLotValue.trim();
                        // trim the Customer final Lot
                        String getCustomerLotTrimmed = getLotValue.trim();
                        // add the lot data to the ArrayList of lots
                        //lotData.add(getLotTrim);
                        lotTSMCData.add(getTSMCLotTrimmed);
                        lotTSMCData.add(getCustomerLotTrimmed);
                        // add the lot data ArrayList to the full ArrayList
                        lotTSMCDataAll.add(lotTSMCData);
                        logger.debug(lotTSMCData.toString());
                }
            }
        // line is not visible here.
        }catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
        // loop through the TSMC lot array list
        for(ArrayList lotTSMCArray : lotTSMCDataAll) {
            // loop through the lot Array list
            for(ArrayList lotArrayData : lotDataAll) {
                // check if the lot equals
                if (lotArrayData.get(0).equals(lotTSMCArray.get(0))){
                    // replace the TSMC lot with the customer lot
                    lotArrayData.set(0,lotTSMCArray.get(1));
                }
            }
        }
        // loop through the lot Array list
        for(ArrayList lotArray : lotDataAll) {
            // add the data to the Array List
            lotArray.add(invoiceNo);
            lotArray.add(invoiceDate);
            lotArray.add(forwarderTrim);
            lotArray.add(mawbTrim);
            lotArray.add(hawbTrim);
            lotArray.add(fileName);
            lotArray.add(productNumber);
            lotArray.add(product2000Number);
            lotArray.add(ETA);
            // add the data to the full ArrayList of ArrayList of Strings
            shipDataAll.add(lotArray);
        }
        System.out.println("Read data from file: " + filePath);
        // return the file data
        return shipDataAll;
    }
    
    /**
     * Get data from TSMC TXT file of ShipALert
     * Parse all needed data
     * @param filePath
     */
    public void readLineShipAlertTXT(String filePath) {
        logger.info("Get data from TSMC TXT file of ShipALert");
        // create new instance
        ReportImport repimp = new ReportImport();
        // get the filename from file path
        String fileName = repimp.getFileNameFromPath(filePath);
        // create the ArrayList of Strings to store the Shipment Data
        ArrayList<String> shipData = new ArrayList<String>();
        // create the variable for storing the number of lots
        int lotNumber = 0;
        int lotTSMCNumber = 0;
        // create the ArrayList of ArrayList of Strings to store the full Lot Data
        ArrayList<ArrayList<String>> lotDataAll = new ArrayList<ArrayList<String>>();
        // create the ArrayList of ArrayList of Strings to store the TSMC to Customer Lot Data
        ArrayList<ArrayList<String>> lotTSMCDataAll = new ArrayList<ArrayList<String>>();
        // create the ArrayList of ArrayList of Strings to store the full Shipment Lot Data
        ArrayList<ArrayList<String>> shipDataAll = new ArrayList<ArrayList<String>>();
        // create all variables needed
        String invoiceNo = "";
        String invoiceDate = "";
        String forwarderTrim = "";
        String mawbTrim = "";
        String hawbTrim = "";
        // line number
        int lineNumber = 0;
        int custLotNumberStartLine = 0;
        int custLotNumberEndLine = 0;
        
        // create the ArrayList of ArrayList of Strings for the text file data
        //ArrayList<ArrayList<String>> txtFileAll = new ArrayList<ArrayList<String>>();
        ArrayList<String> txtFileAll = new ArrayList<String>();

        // read buffered txt file
        logger.info("Read buffered txt file :" + filePath);
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            
            // loop through file line by line
            for(String line; (line = br.readLine()) != null; ) {
                
                // count the line number
                lineNumber = lineNumber + 1;
                
                // create the ArrayList of Strings for the text file Line data
                ArrayList<String> txtFileLine = new ArrayList<String>();
                
                // add line number at the beginning of ArrayList
                txtFileLine.add(Integer.toString(lineNumber));

                // use Scanner to scan the line
                Scanner scanner = new Scanner(line);
                
                // loop through line and separate by whitespace
                while (scanner.hasNext()){
                    // add scanner input to ArrayList
                    txtFileLine.add(scanner.next());
                }
                // add the ArrayList to the full TextArray
                //txtFileAll.add(txtFileLine);
                txtFileAll.add(line);
                logger.debug(line.toString());
            }
            for(String txtFileLine : txtFileAll){
                if(txtFileLine.contains("INVOICE NO.:")){
                    System.out.println("Invoice:Line:" + txtFileLine);
                }
            }
        // line is not visible here.
        }catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    /**
     * Get file name from full path string
     * @param filePath
     * @return file name as string
     */
    public String getFileNameFromPath(String filePath) {
        // get the filename, first find the system path delimiter
        String splitRegex = Pattern.quote(System.getProperty("file.separator"));
        // now get the filename after the path delimiter
        String fileName = filePath.split(splitRegex)[1];
        // return the file name
        System.out.println("Get file name from file path: " + filePath + ", file name: " + fileName);
        logger.info("Get file name from file path: " + filePath + ", file name: " + fileName);
        return fileName;
    }
    
    /**
     * Insert specific file into table file_list
     * Inserting the filename means that the file was already processed
     * This makes sure that the file will not be processed a second time
     * @param filePath
     * @return file name as string
     */
    public void insertAndDeleteFileNameIntoTracking(String filePath) {
        logger.info("Insert specific file into table file_list");
        // create new instance
        ReportImport repimp = new ReportImport();
        // get the filename from file path
        String fileName = repimp.getFileNameFromPath(filePath);

        // insert file into table file_list
        // connect to database
        DataBaseSQLite db = new DataBaseSQLite();
        // sql query for file_name
        String sqlInsertFileList = "INSERT INTO file_list VALUES (?);";
        // insert String into file_name
        db.insertIntoTable(fileName, sqlInsertFileList, "file_list");
        // call delete file mathod
        DeleteFile fileDel = new DeleteFile();
        fileDel.deleteSpecifiedFile(filePath);
    }
}
