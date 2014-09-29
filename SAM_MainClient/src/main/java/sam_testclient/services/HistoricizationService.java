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
import java.util.TreeMap;
import sam_testclient.dao.DataAccess;
import sam_testclient.entities.Message;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author Jan
 */
@Deprecated
public abstract class HistoricizationService {

    /**
     * Creates a new logfile.
     *
     * @param buddyName
     * @param content
     * @param pathToFolder
     */
    private static String createLogFile(String buddyName, List<Message> messages, String pathToFolder) {
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
        String content = "";
        for (Message m : messages) {
            content += m.getContent();
        }
        Utilities.writeContentToFile(logFilePath, headerLine + "\n" + content, true);

        System.out.println("New Logfile created");
        return logFilePath;
    }

    public static String getContentFromHistMessagesByName(List<Message> messageList) {
        // Sorting
        TreeMap<String, Message> sorted = new Message.CompareMessageByTimeStampHelper().sortByValue((HashMap<String, Message>) messageList);
        String result = "";
        if (!messageList.isEmpty()) {
            for (Message m : sorted.values()) {
                result += m.getContent();
            }
        }
        return result;

    }

    /**
     * This method is part of the TimerTask. [DO NOT USE IT EXPLICITLY!!]
     */
    @Deprecated
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
    }

    private static String getCurrentTimeCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date()).replaceAll(":|-| ", "");
    }

    private static String getCurrentTimeCodeForMessage(Message m) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(m.getTimestamp()).replaceAll(":|-| ", "");
    }

    private static String startDate;

    private static String endDate;

    private static boolean archived;

    private String getStartDate() {
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

    public static String getNameFromMessage(Message m) {
        String buddyName = m.getContent().split(":")[1];
        buddyName = buddyName.split(" ")[1];
        return buddyName;
    }

    private static String getDataFilePathByName(String buddyName) {
        String folder = getHistFolderPathByName(buddyName);
        File[] temp = Utilities.getFilesOfPath(folder);
        return Utilities.getFilesFromFolder(temp, ".data");
    }

    private static String getHistFolderPathByName(String buddyName) {
        return "resources/buddies/" + buddyName + File.separator + "history/";
    }

    private static int compareTimeCodes(String code1, String code2) {
        BigInteger c1 = BigInteger.valueOf(Long.decode(code1));
        BigInteger c2 = BigInteger.valueOf(Long.decode(code2));
        return c1.compareTo(c2);
    }

    public static void createHistory(int buddyId, String buddyName) {
        new HistoryCreator(buddyId, buddyName).start();
    }

    public static class HistoryCreator extends Thread {

        private String buddyName;
        private int buddyId;

        public HistoryCreator(int buddyId, String buddyName) {
            this.buddyId = buddyId;
            this.buddyName = buddyName;
        }

        @Override
        public void run() {
            List<Message> allMessagesFromBuddy = DataAccess.getAllMessagesWhereBuddyIsInvolved(buddyId);
            createLogFile(buddyName, allMessagesFromBuddy, getHistFolderPathByName(buddyName));
        }

    }

}
