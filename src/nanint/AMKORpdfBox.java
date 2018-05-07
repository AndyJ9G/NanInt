/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the class for Amkor PDF import
 * @author Grdan Andreas
 */
public class AMKORpdfBox {
    
    //private static final Logger logger = ApplicationLogger.getInstance();
    private static Logger logger = LogManager.getLogger(AMKORpdfBox.class.getName());

    //public void pdfImportAMKOR(File file) throws IOException {
    public AMKORpdf pdfImportAMKOR(File file) throws IOException { 
        logger.info("Class Amkor PDF import");
        
        // The data as an observable list of AMKOR to ASE 
        AMKORpdf AMKpdf = new AMKORpdf();

        // open the pdf document
        try (PDDocument document = PDDocument.load(file)) {
            // get class
            document.getClass();
            logger.info("Get Amkor PDF file");

            // check encryption
            if (!document.isEncrypted()) {
		
                // open pdf stripper
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                // create instance of text stripper
                PDFTextStripper tStripper = new PDFTextStripper();

                // read the text of the pdf file
                String pdfFileInText = tStripper.getText(document);
                logger.info("Read Text from Amkor PDF import");

		// split text by whitespace and save in lines[]
                String lines[] = pdfFileInText.split("\\r?\\n");
                logger.info("Split Text in lines from Amkor PDF import");
                
                // get the file name
                ReportImport repimp = new ReportImport();
                String fileName = repimp.getFileNameFromPath(file.toString());
                System.out.println("PDF-File:" + fileName);
            
                int intLine = 0;
                int intPackingListDate = 0;
                int intPackingNo = 0;
                int intInvoice = 0;
                int intShipTo = 0;
                int intMAWB = 0;
                int intCustomer = 0;
                String desOfGods = "";
                
                logger.info("Loop through lines from Amkor PDF import");
                
                // loop through lines
                for (String line : lines) {
                    //System.out.println(intLine + ":" + line);
                    // check Packing List Date line
                    if(line.contains("Packing List Date") && intPackingListDate == 0){
                        // next line ist the data
                        intPackingListDate = intLine + 1;
                    }
                    // check Packing no line
                    if(line.contains("Packing No") && intPackingNo == 0){
                        // next line ist the data
                        intPackingNo = intLine + 1;
                    }
                    // check Invoice line
                    if(line.contains("Invoice") && intInvoice == 0){
                        // next line ist the data
                        intInvoice = intLine + 1;
                    }
                    // check Ship To line
                    if(line.contains("Ship To") && intShipTo == 0){
                        // next line ist the data
                        intShipTo = intLine + 1;
                    }
                    // check MAWB / HAWB NO line
                    if(line.contains("MAWB / HAWB NO") && intMAWB == 0){
                        // next line ist the data
                        intMAWB = intLine + 1;
                    }
                    // check CUSTOMER P/O line
                    if(line.contains("CUSTOMER P/O") && intCustomer == 0){
                        // next line ist the data
                        intCustomer = intLine + 1;
                    }
                    // check Material Data lines
                    if(line.matches("9000.*PT.*")){
                        // Split line by " - ", we want the second part
                        String[] split9000 = line.split(" - ");
                        // split the second part with whitespaces
                        String[] splitDescription = split9000[1].split("\\s+");
                        // count lenght of array
                        int splitL = splitDescription.length;
                        // remove the last element, the pieces information
                        splitDescription = ArrayUtils.remove(splitDescription, (splitL-1) );
                        // join all elements into the needed string
                        // it's always the same element, so we use the last entry of the loop
                        desOfGods = String.join(" ",splitDescription);
                    }
                    // add count to loop lines
                    intLine = intLine + 1;
                }
                logger.info("Check data from lines from Amkor PDF import");
                if (intPackingListDate != 0){
                    // add value to AMKOR pdf
                    AMKpdf.setShippingDate(lines[intPackingListDate]);
                    //System.out.println("PackingListDate:" + lines[intPackingListDate]);
                }
                if (intPackingNo != 0){
                    // add value to AMKOR pdf
                    AMKpdf.setPackingNo(lines[intPackingNo]);
                    //System.out.println("PackingNo:" + lines[intPackingNo]);
                }
                if (intInvoice != 0){
                    // add value to AMKOR pdf
                    AMKpdf.setInvoiceNo(lines[intInvoice]);
                    //System.out.println("Invoice:" + lines[intInvoice]);
                }
                if (intShipTo != 0){
                    // add value to AMKOR pdf
                    AMKpdf.setShipTo(lines[intShipTo] + "|" + lines[intShipTo+1]);
                    //System.out.println("ShipTo:" + lines[intShipTo] + "|" + lines[intShipTo+1]);
                }
                if (intMAWB != 0){
                    // add value to AMKOR pdf
                    AMKpdf.setMAWB(lines[intMAWB]);
                    //System.out.println("MAWB:" + lines[intMAWB]);
                }
                if (intCustomer != 0){
                    // add value to AMKOR pdf
                    AMKpdf.setCustomerPO(lines[intCustomer]);
                    //System.out.println("Customer:" + lines[intCustomer]);
                }
                if (desOfGods != ""){
                    // add value to AMKOR pdf
                    AMKpdf.setDescriptionOfGoods(desOfGods);
                    //System.out.println("Description:" + desOfGods);
                }
                // add value to AMKOR pdf
                AMKpdf.setPdfFileName(fileName);
                logger.info("Add values from Amkor PDF import");
            }
        }catch(IOException e){
            System.out.println("IOException:" + e);
            logger.error("IOException:" + e);
        }
        return AMKpdf;
    }
}
