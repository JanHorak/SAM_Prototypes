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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import sam_testclient.beans.ClientMainBean;
import sam_testclient.entities.Message;
import sam_testclient.sources.FileManager;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author Jan
 */
public abstract class HistoricizationService {

    /**
     * Creates a new logfile.
     *
     * @param buddyName
     * @param content
     * @param pathToFolder
     */
    private static String createLogFile(String buddyName, Map<String, Message> messages, String pathToFolder) {
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
        for (Message m : messages.values()) {
            content += m.getContent() + "\n";
        }
        Utilities.writeContentToFile(logFilePath, headerLine + "\n" + content, true);

        System.out.println("New Logfile created");
        return logFilePath;
    }

    public static String getContentFromHistMessagesByName(String buddyName) {
        loadCurrentHistoryInMemory();
        Map<String, Message> messages = ClientMainBean.getInstance().getCurrentHistoryMap().get(buddyName);
        
        // Sorting
        TreeMap<String, Message> sorted = new Message.CompareMessageByTimeStampHelper().sortByValue((HashMap<String, Message>) messages);
        String result = "";
        if (!messages.isEmpty()) {
            for (Message m : sorted.values()) {
                result += m.getContent();
            }
        }
        return result;

    }

    /**
     * Creates a new and empty logfile out of the passed old one.
     *
     * @param pathToFolder
     */
    private static void createNewDataFile(String pathToFolder) {
        startDate = getCurrentTimeCode();
        String path = "_" + startDate + ".data";
        Map<String, Message> messageList = new HashMap<>();
        FileManager.serialize(messageList, pathToFolder + path);
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

    public static void createEmptyHistFile(String buddyName) {
        createNewDataFile(getHistFolderPathByName(buddyName));
    }

    public static void loadCurrentHistoryInMemory() {

        Map<String, Map<String, Message>> currentHistory = new ConcurrentHashMap<>();

        String pathToFolder = "resources/buddies/";
        if (new File(pathToFolder).exists()) {
            // getting all files from dir
            File[] folder = Utilities.getFilesOfPath(pathToFolder);

            String[] path;
            for (int i = 0; i < folder.length; i++) {
                File f = folder[i];
                Map<String, Message> desList = new HashMap<>();
                String pattern = Pattern.quote(System.getProperty("file.separator"));
                String[] nameArray = f.getAbsolutePath().split(pattern);
                String name = nameArray[nameArray.length - 1];
                String testFolder = f.getAbsolutePath() + File.separator + "history";
                File[] temp = Utilities.getFilesOfPath(testFolder);
                String presentDataFilePath = Utilities.getFilesFromFolder(temp, ".data");
                if (!presentDataFilePath.isEmpty()) {
                    desList = (Map<String, Message>) FileManager.deserialize(presentDataFilePath);
                } else {
                    createNewDataFile(testFolder);
                    i--;
                }

                currentHistory.put(name, desList);
            }
            System.out.println("CURRENT HISTORY: "+currentHistory.toString());
            ClientMainBean.getInstance().setCurrentHistoryMap(currentHistory);
        }

    }

    public static void addMessageToCurrentHistory(Message m, boolean ownMessage) {
        loadCurrentHistoryInMemory();

        String sender = getNameFromMessage(m);
//        // Replacing buddy througth "Me" if the message is from the client itself
        if (ownMessage) {
            m.setContent(m.getContent().replaceAll(sender, "Me"));
        }

        Map<String, Message> currentHistory = ClientMainBean.getInstance().getCurrentHistoryMap().get(sender);
        if (currentHistory.size() > Integer.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "histBorder"))) {

            // get old one
            File oldOne = new File(getDataFilePathByName(sender));
            // create Log
            String logFilePath = createLogFile(sender, currentHistory, getHistFolderPathByName(sender));
            File logFile = new File(logFilePath);

            List<File> fileList = new ArrayList<>();
            fileList.add(oldOne);
            fileList.add(logFile);
            Utilities.generateZip(getHistFolderPathByName(sender).concat(getCurrentTimeCode() + ".zip"), fileList);
            oldOne.delete();
            createNewDataFile(getHistFolderPathByName(sender));
            Utilities.getFilesFromFolder(getHistFolderPathByName(sender), ".log").get(0).delete();

        } else {
            
            currentHistory.put(m.getId(), m);
            System.out.println("Hist: "+ currentHistory.get(m.getId()).toString() + " replaced by " + m.toString());
            serializeSpecificHistory(currentHistory, sender);
            System.out.println("New Entry added: " + m.getContent());
        }
    }

    public static void serializeCurrentHistory() {
        for (String buddy : ClientMainBean.getInstance().getCurrentHistoryMap().keySet()) {
            serializeSpecificHistory(ClientMainBean.getInstance().getCurrentHistoryMap().get(buddy), buddy);
        }
    }

    public static void serializeSpecificHistory(Map<String, Message> currentHistory, String sender) {
        FileManager.serialize(currentHistory, getDataFilePathByName(sender));
    }

    public static void createNewDataFileForBuddy(String name) {
        createNewDataFile(getHistFolderPathByName(name));
        System.out.println("new FileCreated");
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

    public static boolean isHistoryPresent(String buddyName) {
        boolean result = ClientMainBean.getInstance().getCurrentHistoryMap().containsKey(buddyName);
        System.out.println(result);
        return result;
    }

    @Deprecated
    public static void updateStatusInHistory(Message m) {

        String sender = getNameFromMessage(m);
        Map<String, Map<String, Message>> currentHist = ClientMainBean.getInstance().getCurrentHistoryMap();
        String searchedTimeCode = getCurrentTimeCodeForMessage(m);

        // Get all Zips
        List<File> allZips = Utilities.getFilesFromFolder(getHistFolderPathByName(sender), ".zip");

        for (File f : allZips) {

        }

    }

    private static int compareTimeCodes(String code1, String code2) {
        BigInteger c1 = BigInteger.valueOf(Long.decode(code1));
        BigInteger c2 = BigInteger.valueOf(Long.decode(code2));
        return c1.compareTo(c2);
    }

    
    
}
