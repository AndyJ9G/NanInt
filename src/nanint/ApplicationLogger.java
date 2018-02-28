/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import org.apache.log4j.*;

/**
 * This is the method for logging
 * @author grdana
 */
public class ApplicationLogger {
    private static Logger logger = null;
 
    private ApplicationLogger(){
        super();
    }
 
    public static Logger getInstance(){
        if (logger == null)
            initLogger();
        return logger;
    }
 
    private static void initLogger(){
        try{
            logger = Logger.getLogger(ApplicationLogger.class);
            PatternLayout appenderLayout = new PatternLayout();
            appenderLayout.setConversionPattern("%d %p - %m%n");
            FileAppender appender = new FileAppender(appenderLayout, "log.txt");
            logger.addAppender(appender);
            logger.setLevel(org.apache.log4j.Level.ALL);
        } catch (Exception ex){
            logger.error("Unknown exception: " + ex.getLocalizedMessage());
        }
    }
}   
