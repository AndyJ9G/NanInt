/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.io.File;
import java.io.FilenameFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the method for looping of file from given directory
 * @author Grdan Andreas
 */
public class LoopDirectory {
    
    //private static final Logger logger = ApplicationLogger.getInstance();
    private static Logger logger = LogManager.getLogger(LoopDirectory.class.getName());

    /**
     * Loop through specified directory
     * Find the files with corresponding ending
     * @param directory
     * @param fileEnding
     * @param fileRegex
     * @return Array of Files
     */
    public File[] loopDirectorySpecific (String directory, String fileEnding, String fileRegex) {
        logger.info("Loop through all files in directory: " + directory + ", with Regex: " + fileRegex + ", with file-ending: " + fileEnding);
        // initilize the directory
        File dir = new File(directory);
        // list files and use FilenameFilter
        File[] files = dir.listFiles(new FilenameFilter() {
            // use FilenameFilter to filter csv
            public boolean accept(File dir, String name) {
                return name.contains(fileRegex) && name.toLowerCase().endsWith(fileEnding);
            }
        });
        System.out.println("Loop through all files in directory: " + directory + ", with Regex: " + fileRegex + ", with file-ending: " + fileEnding);
        return files;
    }    
}
