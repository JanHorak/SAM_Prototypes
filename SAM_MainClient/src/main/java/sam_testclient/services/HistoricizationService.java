/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.services;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sam_testclient.entities.Message;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author Jan
 */
public abstract class HistoricizationService {

    /**
     * Hist- Method for simple Messanges.
     *
     * @param m
     * @param ownMessage flag, if the message is from the client itself
     */
    public static void historizeMessage(Message m, boolean ownMessage) {
        System.out.println(m.toString());
        // Extract buddy- name
        String buddyName = m.getContent().split(":")[1];
        buddyName = buddyName.split(" ")[1];

        // Replacing buddy througth "Me" if the message is from the client itself
        if (ownMessage) {
            m.setContent(m.getContent().replaceAll(buddyName, "Me"));
        }

        // aiming directory
        String pathToFolder = "resources/buddies/" + buddyName + "/history";

        // getting all files from dir
        File[] folder = Utilities.getFilesOfPath(pathToFolder);

        if (folder.length != 0) {
            // get the present logfile (Its is possible that zips are present)
            String presentLogFilePath = Utilities.getlogFileFromFolder(folder, ".log");

            // Test if Filesize and content will be higher than border
            long summery1 = Utilities.getSizeOfFileContent(presentLogFilePath) + Utilities.getSizeOfFileContent(m.getContent());
            long summery2 = Long.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "histBorder"));
            if (summery1 >= summery2) {
                // create new File for this buddy
                createNewFile(buddyName, m.getContent(), pathToFolder);

                // add it to List
                List<String> fileList = new ArrayList<String>();
                fileList.add(presentLogFilePath);

                // compress the old log file
                Utilities.generateZip(presentLogFilePath.replaceAll(".log", ".zip"), fileList);

                // delete old log file
                new File(presentLogFilePath).delete();
                System.out.println("replaced");
            } else {
                // add the content of the message to the present log file
                Utilities.writeContentToFile(presentLogFilePath, m.getContent(), true);
                System.out.println("added");
            }
        } else {
            // create a new logfile and add the content of the message (it is needed because 
            // the last action can cause a compression of the logfile
            createNewFile(buddyName, m.getContent(), pathToFolder);
            System.out.println("new added");
        }

    }

    /**
     * Creates a new logfile.
     * 
     * @param buddyName
     * @param content
     * @param pathToFolder
     */
    private static void createNewFile(String buddyName, String content, String pathToFolder) {
        archived = false;
        
        // creates a new timecode
        startDate = getCurrentTimeCode();
        
        // creates a new endDate
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        endDate = sdf.format(c.getTime()).replaceAll(":|-| ", "");

        // aiming the new path
        String logFilePath = pathToFolder + "/" + buddyName + "_" + startDate + ".log";

        // create header
        String headerLine = "# HISTORYFILE\n" + "#Buddy: " + buddyName + "\n#Started: " + startDate + "\n#Until Historicization: " + endDate + "\n#Archived: " + archived + "\n#--------------------\n";

        // write all in new file
        Utilities.writeContentToFile(logFilePath, headerLine + "\n" + content, true);
    }

    /**
     * Creates a new and empty logfile out of the passed old one.
     * @param pathToFolder 
     */
    private static void createNewFile(String pathToFolder) {
        // @Todo: Get Buddy- name!!
        archived = false;
        startDate = getCurrentTimeCode();
        String pathPart = pathToFolder.split("_\\d")[0].concat("_" + startDate + ".log");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        endDate = sdf.format(c.getTime()).replaceAll(":|-| ", "");
        String headerLine = "# HISTORYFILE\n" + "#Buddy: " + "[History]" + "\n#Started: " + startDate + "\n#Until Historicization: " + endDate + "\n#Archived: " + archived + "\n#--------------------\n";

        Utilities.writeContentToFile(pathPart, headerLine, true);
    }

    /**
     * This method is part of the TimerTask.
     * [DO NOT USE IT EXPLICITLY!!]
     */
    public static void doHistoricizationIfItsNeeded() {
        // calculate current Timecode
        String currentTimeCode = getCurrentTimeCode();
        
        // create help- attributes
        List<File> allLogFiles = new ArrayList<File>();
        Map<File, Boolean> histMap = new HashMap<File, Boolean>();

        // ***** Start extraction of Files *****
        
        // aim all folder in ~/resources/buddies/  [getting names of the buddies]
        File[] files = Utilities.getFilesOfPath("resources/buddies/");
        for (int i = 0; i < files.length; i++) {
            
            // aim all folders in ~/<buddyname>/history/ [getting all files in history- Dir]
            String newPath = files[i].getAbsolutePath().concat("\\history\\");
            File[] files_intern = Utilities.getFilesOfPath(newPath);
            File logfile = null;
            
            // extract the log- file [add it to the logfile- list]
            for (int y = 0; y < files_intern.length; y++) {
                if (files_intern[y] != null) {
                    if (files_intern[y].getAbsolutePath().endsWith("log")) {
                        logfile = new File(files_intern[y].getAbsolutePath());
                        allLogFiles.add(logfile);
                        break;
                    }
                }
            }
        }
        // *****  Fileextraction completed  *****
        
        // go through all extracted logfiles
        for (File f : allLogFiles) {
            // get timecode of single logfile
            String timeCode = Utilities.readLineInFile(4, f.getAbsolutePath()).substring(22);

            
            BigInteger current = new BigInteger(currentTimeCode);
            BigInteger fileCode = new BigInteger(timeCode);
            
            // if the code in the logfile is less than the current it has to be compressed
            if ((fileCode.compareTo(current)) == -1) {
                // notice it
                histMap.put(f, Boolean.TRUE);
            } else {
                histMap.put(f, Boolean.FALSE);
            }
        }

        // Compress and cleanup
        for (File f : histMap.keySet()) {
            if (histMap.get(f)) {
                List<String> fileList = new ArrayList<String>();
                fileList.add(f.getAbsolutePath());
                Utilities.generateZip(f.getAbsolutePath().replaceAll(".log", ".zip"), fileList);
                createNewFile(f.getAbsolutePath());
                f.delete();
                System.out.println("Zip generated");
            }
        }

    }

    private static String getCurrentTimeCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date()).replaceAll(":|-| ", "");
    }

    private static String startDate;

    private static String endDate;

    private static boolean archived;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public String toString() {
        return this.startDate + " " + this.endDate + " " + this.archived;
    }

}
