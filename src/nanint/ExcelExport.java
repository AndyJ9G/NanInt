/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.io.File;
import java.io.FileOutputStream;  
import java.io.IOException; 
import java.sql.ResultSet;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFRow;  
import org.apache.poi.hssf.usermodel.HSSFSheet;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook; 

/**
 * This is the method for excel export of data
 * @author Grdan Andreas
 */
public class ExcelExport {
    
    /**
     * Export the Wip NAN data to excel file
     * @param lotData
     * @param file 
     */
    public void reportLotDataExcelExport(ObservableList<LotData> lotData, File file){  
        FileOutputStream fileOut = null;
        try {           
            //Variable counter for keeping track of number of rows inserted.  
            int counter = 1;
            
            //sql - query to be executed; filename - Name of the excel file 
            String filename = file.toString();
            System.out.println("Excel Export into file: " + filename);
            
            //Creation of New Work Book in Excel and sheet.  
            HSSFWorkbook hwb = new HSSFWorkbook();  
            HSSFSheet sheet = hwb.createSheet("LotData");
            
            //Creating Headings in Excel sheet.  
            HSSFRow rowhead=  sheet.createRow((short)0);  
            rowhead.createCell((short) 0).setCellValue("Lot");//Row Name1
            rowhead.createCell((short) 1).setCellValue("Product");//Row Name1 
            rowhead.createCell((short) 2).setCellValue("BasicType");//Row Name1 
            rowhead.createCell((short) 3).setCellValue("CreationDate");//Row Name1 
            rowhead.createCell((short) 4).setCellValue("FirstStepDate");//Row Name1 
            rowhead.createCell((short) 5).setCellValue("LastStepDate");//Row Name1 
            rowhead.createCell((short) 6).setCellValue("LastDate");//Row Name1 
            // loop through the observable list
            for(LotData ld : lotData){
                //Insertion in corresponding row  
                HSSFRow row=  sheet.createRow((int)counter); 
                row.createCell((short) 0).setCellValue(ld.getLot());
                row.createCell((short) 1).setCellValue(ld.getProduct());
                row.createCell((short) 2).setCellValue(ld.getBasicType());
                row.createCell((short) 3).setCellValue(ld.getCreationDate());
                row.createCell((short) 4).setCellValue(ld.getFirstStepDate());
                row.createCell((short) 5).setCellValue(ld.getLastStepDate());
                row.createCell((short) 6).setCellValue(ld.getLastDate());
                // increase the counter
                counter ++;
            }
            // loop through columns
            for(int i=0;i<16;i++){
                // auto size the columns
                sheet.autoSizeColumn(i);
            }
            try {  
                //For performing write to Excel file  
                fileOut = new FileOutputStream(filename);
                System.out.println("File Output Stream opened.");
                hwb.write(fileOut);
                System.out.println("Write to file finished.");
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } finally {
            try {
                //Close all the parameters once writing to excel is compelte.  
                fileOut.close();
                System.out.println("File closed.");
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
    }
    
    /**
     * Export the Wip NAN data to excel file
     * @param wipNanData
     * @param file 
     */
    public void reportWipNanExcelExport(ObservableList<WipNan> wipNanData, File file){  
        FileOutputStream fileOut = null;
        try {           
            //Variable counter for keeping track of number of rows inserted.  
            int counter = 1;
            
            //sql - query to be executed; filename - Name of the excel file 
            String filename = file.toString();
            System.out.println("Excel Export into file: " + filename);
            
            //Creation of New Work Book in Excel and sheet.  
            HSSFWorkbook hwb = new HSSFWorkbook();  
            HSSFSheet sheet = hwb.createSheet("WipNan");
            
            //Creating Headings in Excel sheet.  
            HSSFRow rowhead=  sheet.createRow((short)0);  
            rowhead.createCell((short) 0).setCellValue("ReportDate");//Row Name1
            rowhead.createCell((short) 1).setCellValue("Lot");//Row Name1 
            rowhead.createCell((short) 2).setCellValue("Owner");//Row Name1 
            rowhead.createCell((short) 3).setCellValue("Product");//Row Name1 
            rowhead.createCell((short) 4).setCellValue("HoldFlag");//Row Name1 
            rowhead.createCell((short) 5).setCellValue("WIP1");//Row Name1 
            rowhead.createCell((short) 6).setCellValue("WIP2");//Row Name1 
            rowhead.createCell((short) 7).setCellValue("Shrink");//Row Name1 
            rowhead.createCell((short) 8).setCellValue("FE_SITE");//Row Name1 
            rowhead.createCell((short) 9).setCellValue("BasicType");//Row Name1 
            rowhead.createCell((short) 10).setCellValue("Package");//Row Name1 
            rowhead.createCell((short) 11).setCellValue("Step");//Row Name1 
            rowhead.createCell((short) 12).setCellValue("WorkCenter");//Row Name1 
            rowhead.createCell((short) 13).setCellValue("SEQ");//Row Name1 
            rowhead.createCell((short) 14).setCellValue("Percentage");//Row Name1 
            rowhead.createCell((short) 15).setCellValue("ReportFileName");//Row Name1
            // loop through the observable list
            for(WipNan wn : wipNanData){
                //Insertion in corresponding row  
                HSSFRow row=  sheet.createRow((int)counter); 
                row.createCell((short) 0).setCellValue(wn.getReportDate());
                row.createCell((short) 1).setCellValue(wn.getLot());
                row.createCell((short) 2).setCellValue(wn.getOwner());
                row.createCell((short) 3).setCellValue(wn.getProduct());
                row.createCell((short) 4).setCellValue(wn.getHoldFlag());
                row.createCell((short) 5).setCellValue(wn.getWIP1());
                row.createCell((short) 6).setCellValue(wn.getWIP2());
                row.createCell((short) 7).setCellValue(wn.getShrink());
                row.createCell((short) 8).setCellValue(wn.getFE_SITE());
                row.createCell((short) 9).setCellValue(wn.getBasicType());
                row.createCell((short) 10).setCellValue(wn.getPackage());
                row.createCell((short) 11).setCellValue(wn.getStep());
                row.createCell((short) 12).setCellValue(wn.getWorkCenter());
                row.createCell((short) 13).setCellValue(wn.getSEQ());
                row.createCell((short) 14).setCellValue(wn.getPercentage());
                row.createCell((short) 15).setCellValue(wn.getReportFileName());
                // increase the counter
                counter ++;
            }
            // loop through columns
            for(int i=0;i<16;i++){
                // auto size the columns
                sheet.autoSizeColumn(i);
            }
            try {  
                //For performing write to Excel file  
                fileOut = new FileOutputStream(filename);
                System.out.println("File Output Stream opened.");
                hwb.write(fileOut);
                System.out.println("Write to file finished.");
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } finally {
            try {
                //Close all the parameters once writing to excel is compelte.  
                fileOut.close();
                System.out.println("File closed.");
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
    }
    
    /**
     * Export the GF to NAN data to excel file
     * @param GFtoNanData
     * @param file 
     */
    public void reportGFtoNanExcelExport(ObservableList<GFtoNan> GFtoNanData, File file){  
        FileOutputStream fileOut = null;
        try {           
            //Variable counter for keeping track of number of rows inserted.  
            int counter = 1;
            
            //sql - query to be executed; filename - Name of the excel file 
            String filename = file.toString();
            System.out.println("Excel Export into file: " + filename);
            
            //Creation of New Work Book in Excel and sheet.  
            HSSFWorkbook hwb = new HSSFWorkbook();  
            HSSFSheet sheet = hwb.createSheet("GFtoNan");
            
            //Creating Headings in Excel sheet.  
            HSSFRow rowhead=  sheet.createRow((short)0);  
            rowhead.createCell((short) 0).setCellValue("ReportDate");//Row Name1
            rowhead.createCell((short) 1).setCellValue("CustomerName");//Row Name1 
            rowhead.createCell((short) 2).setCellValue("ShipDate");//Row Name1 
            rowhead.createCell((short) 3).setCellValue("InvoiceNo");//Row Name1 
            rowhead.createCell((short) 4).setCellValue("InvoiceDate");//Row Name1 
            rowhead.createCell((short) 5).setCellValue("PO");//Row Name1 
            rowhead.createCell((short) 6).setCellValue("SO");//Row Name1 
            rowhead.createCell((short) 7).setCellValue("OrderDate");//Row Name1 
            rowhead.createCell((short) 8).setCellValue("ProcessName");//Row Name1 
            rowhead.createCell((short) 9).setCellValue("CustomerPartname");//Row Name1 
            rowhead.createCell((short) 10).setCellValue("InternalPartName");//Row Name1 
            rowhead.createCell((short) 11).setCellValue("LotId");//Row Name1 
            rowhead.createCell((short) 12).setCellValue("CustomerLotId");//Row Name1 
            rowhead.createCell((short) 13).setCellValue("LotType");//Row Name1 
            rowhead.createCell((short) 14).setCellValue("LotPriority");//Row Name1
            rowhead.createCell((short) 15).setCellValue("AgreedGDPW");//Row Name1
            rowhead.createCell((short) 16).setCellValue("CalculatedGDPL");//Row Name1
            rowhead.createCell((short) 17).setCellValue("CycleTime");//Row Name1
            rowhead.createCell((short) 18).setCellValue("BillQtyWfr");//Row Name1
            rowhead.createCell((short) 19).setCellValue("WfrIds");//Row Name1
            rowhead.createCell((short) 20).setCellValue("vBillDie");//Row Name1
            rowhead.createCell((short) 21).setCellValue("ShipToLocation");//Row Name1
            rowhead.createCell((short) 22).setCellValue("BillToLocation");//Row Name1
            rowhead.createCell((short) 23).setCellValue("ETA");//Row Name1
            rowhead.createCell((short) 24).setCellValue("ETD");//Row Name1
            rowhead.createCell((short) 25).setCellValue("Forwarder");//Row Name1
            rowhead.createCell((short) 26).setCellValue("HAWB");//Row Name1
            rowhead.createCell((short) 27).setCellValue("MAWB");//Row Name1
            rowhead.createCell((short) 28).setCellValue("FlightNo");//Row Name1
            rowhead.createCell((short) 29).setCellValue("ConnectingFlightNo");//Row Name1            
            
            rowhead.createCell((short) 30).setCellValue("ReportFileName");//Row Name1
            // loop through the observable list
            for(GFtoNan wn : GFtoNanData){
                //Insertion in corresponding row  
                HSSFRow row=  sheet.createRow((int)counter); 
                row.createCell((short) 0).setCellValue(wn.getReportDate());
                row.createCell((short) 1).setCellValue(wn.getCustomerName());
                row.createCell((short) 2).setCellValue(wn.getShipDate());
                row.createCell((short) 3).setCellValue(wn.getInvoiceNo());
                row.createCell((short) 4).setCellValue(wn.getInvoiceDate());
                row.createCell((short) 5).setCellValue(wn.getPO());
                row.createCell((short) 6).setCellValue(wn.getSO());
                row.createCell((short) 7).setCellValue(wn.getOrderDate());
                row.createCell((short) 8).setCellValue(wn.getProcessName());
                row.createCell((short) 9).setCellValue(wn.getCustomerPartname());
                row.createCell((short) 10).setCellValue(wn.getInternalPartName());
                row.createCell((short) 11).setCellValue(wn.getLotId());
                row.createCell((short) 12).setCellValue(wn.getCustomerLotId());
                row.createCell((short) 13).setCellValue(wn.getLotType());
                row.createCell((short) 14).setCellValue(wn.getLotPriority());
                row.createCell((short) 15).setCellValue(wn.getAgreedGDPW());
                row.createCell((short) 16).setCellValue(wn.getCalculatedGDPL());
                row.createCell((short) 17).setCellValue(wn.getCycleTime());
                row.createCell((short) 18).setCellValue(wn.getBillQtyWfr());
                row.createCell((short) 19).setCellValue(wn.getWfrIds());
                row.createCell((short) 20).setCellValue(wn.getvBillDie());
                row.createCell((short) 21).setCellValue(wn.getShipToLocation());
                row.createCell((short) 22).setCellValue(wn.getBillToLocation());
                row.createCell((short) 23).setCellValue(wn.getETA());
                row.createCell((short) 24).setCellValue(wn.getETD());
                row.createCell((short) 25).setCellValue(wn.getForwarder());
                row.createCell((short) 26).setCellValue(wn.getHAWB());
                row.createCell((short) 27).setCellValue(wn.getMAWB());
                row.createCell((short) 28).setCellValue(wn.getFlightNo());
                row.createCell((short) 29).setCellValue(wn.getConnectingFlightNo());
                
                row.createCell((short) 30).setCellValue(wn.getReportFileName());
                // increase the counter
                counter ++;
            }
            // loop through columns
            for(int i=0;i<31;i++){
                // auto size the columns
                sheet.autoSizeColumn(i);
            }
            try {  
                //For performing write to Excel file  
                fileOut = new FileOutputStream(filename);
                System.out.println("File Output Stream opened.");
                hwb.write(fileOut);
                System.out.println("Write to file finished.");
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } finally {
            try {
                //Close all the parameters once writing to excel is compelte.  
                fileOut.close();
                System.out.println("File closed.");
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
    }
    
    /**
     * Export the TSMC to NAN data to excel file
     * @param TSMCtoNanData
     * @param file 
     */
    public void reportTSMCtoNanExcelExport(ObservableList<TSMCtoNan> TSMCtoNanData, File file){  
        FileOutputStream fileOut = null;
        try {           
            //Variable counter for keeping track of number of rows inserted.  
            int counter = 1;
            
            //sql - query to be executed; filename - Name of the excel file 
            String filename = file.toString();
            System.out.println("Excel Export into file: " + filename);
            
            //Creation of New Work Book in Excel and sheet.  
            HSSFWorkbook hwb = new HSSFWorkbook();  
            HSSFSheet sheet = hwb.createSheet("TSMCtoNan");
            
            //Creating Headings in Excel sheet.  
            HSSFRow rowhead=  sheet.createRow((short)0);  
            rowhead.createCell((short) 0).setCellValue("Lot");//Row Name1
            rowhead.createCell((short) 1).setCellValue("WaferPcs");//Row Name1 
            rowhead.createCell((short) 2).setCellValue("InvoiceNo");//Row Name1 
            rowhead.createCell((short) 3).setCellValue("InvoiceDate");//Row Name1 
            rowhead.createCell((short) 4).setCellValue("Forwarder");//Row Name1 
            rowhead.createCell((short) 5).setCellValue("MAWB");//Row Name1 
            rowhead.createCell((short) 6).setCellValue("HAWB");//Row Name1 
            rowhead.createCell((short) 7).setCellValue("ReportFileName");//Row Name1
            rowhead.createCell((short) 8).setCellValue("Technology");//Row Name1
            rowhead.createCell((short) 9).setCellValue("Product");//Row Name1
            rowhead.createCell((short) 10).setCellValue("Product2000");//Row Name1
            rowhead.createCell((short) 11).setCellValue("ETA");//Row Name1
            // loop through the observable list
            for(TSMCtoNan wn : TSMCtoNanData){
                //Insertion in corresponding row  
                HSSFRow row=  sheet.createRow((int)counter); 
                row.createCell((short) 0).setCellValue(wn.getLot());
                row.createCell((short) 1).setCellValue(wn.getWaferPcs());
                row.createCell((short) 2).setCellValue(wn.getInvoiceNo());
                row.createCell((short) 3).setCellValue(wn.getInvoiceDate());
                row.createCell((short) 4).setCellValue(wn.getForwarder());
                row.createCell((short) 5).setCellValue(wn.getMAWB());
                row.createCell((short) 6).setCellValue(wn.getHAWB());
                row.createCell((short) 7).setCellValue(wn.getReportFileName());
                row.createCell((short) 8).setCellValue(wn.getTechnology());
                row.createCell((short) 9).setCellValue(wn.getProduct());
                row.createCell((short) 10).setCellValue(wn.getProduct2000());
                row.createCell((short) 11).setCellValue(wn.getETA());
                // increase the counter
                counter ++;
            }
            // loop through columns
            for(int i=0;i<8;i++){
                // auto size the columns
                sheet.autoSizeColumn(i);
            }
            try {  
                //For performing write to Excel file  
                fileOut = new FileOutputStream(filename);
                System.out.println("File Output Stream opened.");
                hwb.write(fileOut);
                System.out.println("Write to file finished.");
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } finally {
            try {
                //Close all the parameters once writing to excel is compelte.  
                fileOut.close();
                System.out.println("File closed.");
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
    }
    
    /**
     * Export the UMCI to NAN data to excel file
     * @param UMCItoNanData
     * @param file 
     */
    public void reportUMCItoNanExcelExport(ObservableList<UMCItoNan> UMCItoNanData, File file){  
        FileOutputStream fileOut = null;
        try {           
            //Variable counter for keeping track of number of rows inserted.  
            int counter = 1;
            
            //sql - query to be executed; filename - Name of the excel file 
            String filename = file.toString();
            System.out.println("Excel Export into file: " + filename);
            
            //Creation of New Work Book in Excel and sheet.  
            HSSFWorkbook hwb = new HSSFWorkbook();  
            HSSFSheet sheet = hwb.createSheet("TSMCtoNan");
            
            //Creating Headings in Excel sheet.  
            HSSFRow rowhead=  sheet.createRow((short)0);  
            rowhead.createCell((short) 0).setCellValue("PART_DIV");//Row Name1
            rowhead.createCell((short) 1).setCellValue("INV_NO");//Row Name1 
            rowhead.createCell((short) 2).setCellValue("SHPTO_ID");//Row Name1 
            rowhead.createCell((short) 3).setCellValue("INV_DATE");//Row Name1 
            rowhead.createCell((short) 4).setCellValue("MAWB_NO");//Row Name1 
            rowhead.createCell((short) 5).setCellValue("HAWB_NO");//Row Name1 
            rowhead.createCell((short) 6).setCellValue("FLT_NO");//Row Name1
            rowhead.createCell((short) 7).setCellValue("FLT_DATE");//Row Name1
            rowhead.createCell((short) 8).setCellValue("FLT_DEST");//Row Name1
            rowhead.createCell((short) 9).setCellValue("CARTON_NO");//Row Name1
            rowhead.createCell((short) 10).setCellValue("PO_NO");//Row Name1
            rowhead.createCell((short) 11).setCellValue("PRD_NO");//Row Name1
            rowhead.createCell((short) 12).setCellValue("LOT_TYPE");//Row Name1
            rowhead.createCell((short) 13).setCellValue("LOT_NO");//Row Name1
            rowhead.createCell((short) 14).setCellValue("SHIP_W_QTY");//Row Name1
            rowhead.createCell((short) 15).setCellValue("SHIP_D_QTY");//Row Name1
            rowhead.createCell((short) 16).setCellValue("SHP_PRD_NO");//Row Name1
            rowhead.createCell((short) 17).setCellValue("CTM_DEVICE");//Row Name1
            rowhead.createCell((short) 18).setCellValue("CUSTOMER_LOT");//Row Name1
            rowhead.createCell((short) 19).setCellValue("UMC_INV_NO");//Row Name1
            rowhead.createCell((short) 20).setCellValue("REMARK");//Row Name1
            rowhead.createCell((short) 21).setCellValue("WAFER_NO");//Row Name1
            
            rowhead.createCell((short) 22).setCellValue("ReportFileName");//Row Name1
            // loop through the observable list
            for(UMCItoNan wn : UMCItoNanData){
                //Insertion in corresponding row  
                HSSFRow row=  sheet.createRow((int)counter); 
                row.createCell((short) 0).setCellValue(wn.getPART_DIV());
                row.createCell((short) 1).setCellValue(wn.getINV_NO());
                row.createCell((short) 2).setCellValue(wn.getSHPTO_ID());
                row.createCell((short) 3).setCellValue(wn.getINV_DATE());
                row.createCell((short) 4).setCellValue(wn.getMAWB_NO());
                row.createCell((short) 5).setCellValue(wn.getHAWB_NO());
                row.createCell((short) 6).setCellValue(wn.getFLT_NO());
                row.createCell((short) 7).setCellValue(wn.getFLT_DATE());
                row.createCell((short) 8).setCellValue(wn.getFLT_DEST());
                row.createCell((short) 9).setCellValue(wn.getCARTON_NO());
                row.createCell((short) 10).setCellValue(wn.getPO_NO());
                row.createCell((short) 11).setCellValue(wn.getPRD_NO());
                row.createCell((short) 12).setCellValue(wn.getLOT_TYPE());
                row.createCell((short) 13).setCellValue(wn.getLOT_NO());
                row.createCell((short) 14).setCellValue(wn.getSHIP_W_QTY());
                row.createCell((short) 15).setCellValue(wn.getSHIP_D_QTY());
                row.createCell((short) 16).setCellValue(wn.getSHP_PRD_NO());
                row.createCell((short) 17).setCellValue(wn.getCTM_DEVICE());
                row.createCell((short) 18).setCellValue(wn.getCUSTOMER_LOT());
                row.createCell((short) 19).setCellValue(wn.getUMC_INV_NO());
                row.createCell((short) 20).setCellValue(wn.getREMARK());
                row.createCell((short) 21).setCellValue(wn.getWAFER_NO());
                
                row.createCell((short) 22).setCellValue(wn.getReportFileName());
                // increase the counter
                counter ++;
            }
            // loop through columns
            for(int i=0;i<23;i++){
                // auto size the columns
                sheet.autoSizeColumn(i);
            }
            try {  
                //For performing write to Excel file  
                fileOut = new FileOutputStream(filename);
                System.out.println("File Output Stream opened.");
                hwb.write(fileOut);
                System.out.println("Write to file finished.");
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } finally {
            try {
                //Close all the parameters once writing to excel is compelte.  
                fileOut.close();
                System.out.println("File closed.");
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
    }
    
    /**
     * Export the AMKOR to ASE data to excel file
     * @param ObservableList<AMKORtoASE>
     * @param file 
     */
    public void reportAMKORtoASEExcelExport(ObservableList<AMKORtoASE> amkASE, File file){  
        FileOutputStream fileOut = null;
        try {           
            //Variable counter for keeping track of number of rows inserted.  
            int counter = 1;
            
            //sql - query to be executed; filename - Name of the excel file 
            String filename = file.toString();
            System.out.println("Excel Export into file: " + filename);
            
            //Creation of New Work Book in Excel and sheet.  
            HSSFWorkbook hwb = new HSSFWorkbook();  
            HSSFSheet sheet = hwb.createSheet("AMKORshipment");
            
            //Creating Headings in Excel sheet.  
            HSSFRow rowhead=  sheet.createRow((short)0);  
            rowhead.createCell((short) 0).setCellValue("ShippingDate");//Row Name1 
            rowhead.createCell((short) 1).setCellValue("PackingNo");//Row Name1 
            rowhead.createCell((short) 2).setCellValue("InvoiceNo");//Row Name1
            rowhead.createCell((short) 3).setCellValue("ShipTo");//Row Name1
            rowhead.createCell((short) 4).setCellValue("MAWB");//Row Name1
            rowhead.createCell((short) 5).setCellValue("CustomerPO");//Row Name1
            rowhead.createCell((short) 6).setCellValue("DescriptionOfGoods");//Row Name1
            rowhead.createCell((short) 7).setCellValue("PdfFileName");//Row Name1
            rowhead.createCell((short) 8).setCellValue("Delivery");//Row Name1
            rowhead.createCell((short) 9).setCellValue("Box");//Row Name1
            rowhead.createCell((short) 10).setCellValue("Material");//Row Name1
            rowhead.createCell((short) 11).setCellValue("DeviceName");//Row Name1
            rowhead.createCell((short) 12).setCellValue("PONo");//Row Name1
            rowhead.createCell((short) 13).setCellValue("DateCode");//Row Name1
            rowhead.createCell((short) 14).setCellValue("FABno");//Row Name1
            rowhead.createCell((short) 15).setCellValue("ControlCode");//Row Name1
            rowhead.createCell((short) 16).setCellValue("ATPOno");//Row Name1
            rowhead.createCell((short) 17).setCellValue("FPO");//Row Name1
            rowhead.createCell((short) 18).setCellValue("MCitem");//Row Name1
            rowhead.createCell((short) 19).setCellValue("Quantity");//Row Name1
            rowhead.createCell((short) 20).setCellValue("ReportFileName");//Row Name1

            // loop through the observable list
            for(AMKORtoASE am : amkASE){
                //Insertion in corresponding row  
                HSSFRow row=  sheet.createRow((int)counter); 
                row.createCell((short) 0).setCellValue(am.getShippingDate());
                row.createCell((short) 1).setCellValue(am.getPackingNo()); 
                row.createCell((short) 2).setCellValue(am.getInvoiceNo());
                row.createCell((short) 3).setCellValue(am.getShipTo());
                row.createCell((short) 4).setCellValue(am.getMAWB());
                row.createCell((short) 5).setCellValue(am.getCustomerPO());
                row.createCell((short) 6).setCellValue(am.getDescriptionOfGoods());
                row.createCell((short) 7).setCellValue(am.getPdfFileName());
                row.createCell((short) 8).setCellValue(am.getDelivery());
                row.createCell((short) 9).setCellValue(am.getBox());
                row.createCell((short) 10).setCellValue(am.getMaterial());
                row.createCell((short) 11).setCellValue(am.getDeviceName());
                row.createCell((short) 12).setCellValue(am.getPONo());
                row.createCell((short) 13).setCellValue(am.getDateCode());
                row.createCell((short) 14).setCellValue(am.getFABno());
                row.createCell((short) 15).setCellValue(am.getControlCode());
                row.createCell((short) 16).setCellValue(am.getATPOno());
                row.createCell((short) 17).setCellValue(am.getFPO());
                row.createCell((short) 18).setCellValue(am.getMCitem());
                row.createCell((short) 19).setCellValue(am.getQuantity());
                row.createCell((short) 20).setCellValue(am.getReportFileName());
                // increase the counter
                counter ++;
            }
            // loop through columns
            for(int i=0;i<16;i++){
                // auto size the columns
                sheet.autoSizeColumn(i);
            }
            try {  
                //For performing write to Excel file  
                fileOut = new FileOutputStream(filename);
                System.out.println("File Output Stream opened.");
                hwb.write(fileOut);
                System.out.println("Write to file finished.");
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } finally {
            try {
                //Close all the parameters once writing to excel is compelte.  
                fileOut.close();
                System.out.println("File closed.");
            } catch (IOException e) {  
                e.printStackTrace();  
            } 
        }
    }
}
