/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.io.FileReader;
import java.util.List;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

/**
 * This is the method for looping msg files and extracting the attachments
 * @author Grdan Andreas
 */
public class MsgMail {

    /**
     * Loop through MSG files of specified directory
     * Find the files with corresponding ending
     * Call Attachment extracation
     * Delete the msg file after attachment extraction
     * @param directory
     * @param Regex 
     */
    public ArrayList<Integer> loopMsgAttFiles(String directory, String Regex) {
        ArrayList<Integer> msgatt = new ArrayList<Integer>();
        // create new object
        LoopDirectory lopdir = new LoopDirectory();
        MsgMail msgmail = new MsgMail();
        // loop directory with all msg files
        File[] fileLocations = lopdir.loopDirectorySpecific(directory, ".msg", Regex);
        // how many files did we found?
        int mf = 0;
        // how many attachments
        int att = 0;
        //check if files existing
        if(fileLocations.length != 0){
            // loop through files
            for (File file : fileLocations) {
                // count the file numbers
                mf = mf + 1;
                // read all msg files and extract the attachment
                int attcount = msgmail.MsgAttExtractorParser(file.toString(), directory);
                // add all attachments to the count
                att = att + attcount;
                // call delete file mathod
                DeleteFile fileDel = new DeleteFile();
                fileDel.deleteSpecifiedFile(file.toString());
            }
        } else {
            System.out.println("No .msg files found.");
        }
        // add the number of msg files to the arraylist
        msgatt.add(mf);
        // add the number of attachments to the arraylist
        msgatt.add(att);
        // return the number of msg files and number of attachments
        return msgatt;
    }

    /**
     * MSGparser, get attachment from outlook msg file
     * @param fileLocation
     * @param directory
     * @return Integer, number of attachments extracted
     */
    public Integer MsgAttExtractorParser(String fileLocation, String directory) {
        try {
            // create instance of Message
            MAPIMessage mapiMessage = new MAPIMessage(fileLocation);
            // get the From mail address
            String displayFrom = mapiMessage.getDisplayFrom();
            // get attachments from Mail
            AttachmentChunks attachments[] = mapiMessage.getAttachmentFiles();
            // hom many attachments do we have?
            int att = 0;
            // check if attachments available
            if(attachments.length > 0) {
                // loop through all attachments
                for (AttachmentChunks a : attachments) {
                    // add the attachment to the count
                    att = att + 1;
                    // get the attachment name
                    String attachname=a.attachLongFileName.toString();
                    // extract attachment
                    File f = new File(directory, attachname);
                    OutputStream fileOut = null;
                    try {
			fileOut = new FileOutputStream(f);
			fileOut.write(a.attachData.getValue());
                        System.out.println("Extracted from .msg file: " + fileLocation + ", attachment: " + attachname);
                    } finally {
			if (fileOut != null) {
                            fileOut.close();
			}
                    }
                }
            }else{
                // we have no attachments
            }
            // return number of attachments
            return att;
        } catch (ChunkNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
