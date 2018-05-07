/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author grdana
 */
public class AmkorExcelImport {
    
    //private static final Logger logger = ApplicationLogger.getInstance();
    private static Logger logger = LogManager.getLogger(AmkorExcelImport.class.getName());
    
    /**
     * Import data from excel workbook AMKOR to ASE
     * @param workbook
     * @param file
     */
    public ObservableList<AMKORtoASE> excelImportAMKORtoASE(Workbook workbook, File file){
        
        logger.info("Import data from excel workbook AMKOR to ASE");
        
        // The data as an observable list of AMKOR to ASE 
        ObservableList<AMKORtoASE> AMKtoASE = FXCollections.observableArrayList();
        
        // open sheet
        Sheet boxSheet = workbook.getSheetAt(0);
        System.out.println("Open Sheet.");
        logger.info("Open Sheet");

        // loop through cells of header row to get the right column based on Value
        // the row count starts at 0
        int HEADER_ROW = 0;
        // create hashmap for headers
        HashMap<Integer, String> cellStringArray = new HashMap<Integer, String>();
        // get the row
        Row headerRow = boxSheet.getRow(HEADER_ROW);
        System.out.println("XLS, Header Row :" + HEADER_ROW + ", last row :" + boxSheet.getLastRowNum());
        logger.debug("XLS, Header Row :" + HEADER_ROW + ", last row :" + boxSheet.getLastRowNum());
        // get all cells and put them in the hashmap
        // loop through the cells
        for (int c=0; c < headerRow.getLastCellNum();c++){
            // get the cell
            Cell cellHeader = headerRow.getCell(c);
            // check the cell values with the proper format
            String cellHeaderValueString = checkCellValue(workbook, cellHeader);
            // trim the String
            String cellHeaderValue = cellHeaderValueString.trim();
            // add the header string from cell to the hashmap
            cellStringArray.put(c, cellHeaderValue);
        }

        // loop through all rows starting after header row
        // the row count starts at 0
        int FIRST_ROW_TO_GET = 1;
        System.out.println("XLS, First Data Row :" + FIRST_ROW_TO_GET + ", last row :" + boxSheet.getLastRowNum());
        logger.debug("XLS, First Data Row :" + FIRST_ROW_TO_GET + ", last row :" + boxSheet.getLastRowNum());
        
        for (int r = FIRST_ROW_TO_GET ; r <= boxSheet.getLastRowNum()  ;r++){
            // get the row
            Row row = boxSheet.getRow(r);
            
            System.out.println("XLS, Get Row :" + r + ", last row :" + boxSheet.getLastRowNum());
            logger.debug("XLS, Get Row :" + r + ", last row :" + boxSheet.getLastRowNum());

            // create new AMKOR to ASE Object
            AMKORtoASE amkToAse = new AMKORtoASE();

            // get all cells from row
            // loop through the cells
            for (int c=0; c < row.getLastCellNum();c++){
                // get the cell
                Cell cell = row.getCell(c);
                // check the cell values with the proper format
                String cellValueString = checkCellValue(workbook, cell);
                // trim the String
                String cellValue = cellValueString.trim();

                // get the hashmap header cell name
                // put the cell value based on the header in the front end ticket
                switch (cellStringArray.get(c)) {
                    case "Delivery":
                        // add cell values to AMKOR to ASE
                        amkToAse.setDelivery(cellValue);
                    break;
                    case "Box":
                        // add cell values to AMKOR to ASE
                        amkToAse.setBox(cellValue);
                    break;
                    case "Material":
                        // add cell values to AMKOR to ASE
                        amkToAse.setMaterial(cellValue);
                    break;
                    case "Device Name":
                        // add cell values to AMKOR to ASE
                        amkToAse.setDeviceName(cellValue);
                    break;
                    case "PO No":
                        // add cell values to AMKOR to ASE
                        amkToAse.setPONo(cellValue);
                    break;
                    case "Date Code":
                        // add cell values to AMKOR to ASE
                        amkToAse.setDateCode(cellValue);
                    break;
                    case "FAB no":
                        // add cell values to AMKOR to ASE
                        amkToAse.setFABno(cellValue);
                    break;
                    case "Control Code":
                        // add cell values to AMKOR to ASE
                        amkToAse.setControlCode(cellValue);
                    break;
                    case "ATPO no":
                        // add cell values to AMKOR to ASE
                        amkToAse.setATPOno(cellValue);
                    break;
                    case "FPO":
                        // add cell values to AMKOR to ASE
                        amkToAse.setFPO(cellValue);
                    break;
                    case "MC Item":
                        // add cell values to AMKOR to ASE
                        amkToAse.setMCitem(cellValue);
                    break;
                    case "Quantity":
                        // add cell values to AMKOR to ASE
                        amkToAse.setQuantity(cellValue);
                    break;
                }
            }
            // add the file name
            ReportImport repimp = new ReportImport();
            String fileName = repimp.getFileNameFromPath(file.toString());
            amkToAse.setReportFileName(fileName);
            // add new AMKORtoASEObject to AMKORtoASE observable list
            AMKtoASE.add(amkToAse);
            logger.info("Add new object to AMKOR to ASE observable list");
            logger.debug(amkToAse.toString());
        }
        return AMKtoASE;
    }
    
    /**
     * Check cell value and give the right output based on format
     * @param workbook
     * @param cell
     * @return 
     */
    //public String checkCellValue(Workbook workbook, Cell cell, FormulaEvaluator evaluator){
    public String checkCellValue(Workbook workbook, Cell cell){
        String cellString = "";
        // check the cell value and use the proper format
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                // create String
                cellString = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                // create String from Boolean
                cellString = Boolean.toString(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                // check if value is date formatted
                if (DateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdformatter = new SimpleDateFormat("dd/MM/yyyy");
                    cellString = sdformatter.format(cell.getDateCellValue());
                }else{
                    // get value as Double
                    Double cellDouble = cell.getNumericCellValue();
                    // get value as Long
                    Long cellLong = cellDouble.longValue();
                    // create String from Number
                    cellString = cellLong.toString();
                }
                break;
        }
        logger.debug("Check cell value and return string :" + cellString);
        return cellString;
    }
}
