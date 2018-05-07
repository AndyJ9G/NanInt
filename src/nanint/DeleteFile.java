/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the method for file deletion
 * @author Grdan Andreas
 */
public class DeleteFile {
    
    //private static final Logger logger = ApplicationLogger.getInstance();
    private static Logger logger = LogManager.getLogger(DeleteFile.class.getName());
    /**
     * Delete the file based on the given path as string
     * @param filePath 
     */
    public void deleteSpecifiedFile(String filePath) {
        logger.info("Delete File :" + filePath);
        // delete the file
        try {
            // get file from filename
            File fileObject = new File(filePath);
            // delete the file
            if(fileObject.delete()) {
                System.out.println("File: " + fileObject.toString() + " deleted from filePath: " + filePath );
            }
        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }    
}
