/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.ui.main.MainUI;

/**
 *
 * @author Jan
 */
public class FileSubmitService {

    // [Id, Amount]
    private static Map<Integer, Integer> partDistribution;

    // [Id, Limits]
    private static Map<Integer, Range> partRanges;

    // [Id, Bytes per Part]
    private static Map<Integer, byte[]> bytesPerPart;

    private static Map<String, WaitingService> serviceMap = new HashMap<String, WaitingService>();

    public static void startWaitingService(String id, String fileName, int parts, int timeOut) {
        WaitingService ws = new WaitingService(id, fileName, parts, timeOut);
        serviceMap.put(id, ws);
        ws.start();
    }

    private static synchronized void deleteWaitingService(String id) {
        System.out.println("Service with ID: " + id + " is deleted...");
        serviceMap.remove(id);
    }

    public static synchronized boolean addPartToService(String serviceID, Message m) {
        if (!serviceMap.containsKey(serviceID)) {
            System.out.println("No Service with this ID started. Message ignored.");
            return false;
        } else {
            WaitingService ws = serviceMap.get(serviceID);
            System.out.println("try to add...");
            ws.tryToAddMessage(m);
            return true;
        }
    }

    public static List<byte[]> getPartsOfFile(String path, int parts) {
        List<byte[]> byteList = new ArrayList<>();

        partDistribution = new HashMap<>();
        partRanges = new HashMap<>();
        bytesPerPart = new HashMap<>();

        File file = new File(path);
        byte[] globalByteList = null;
        try {
            globalByteList = IOUtils.toByteArray(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileSubmitService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileSubmitService.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(globalByteList.length);
        System.out.println(globalByteList.length / parts);
        createDistribution(globalByteList.length, parts);

        // cutting
        int globalCounter = 0;
        for (int i = 0; i < partRanges.size(); i++) {
            Range a = partRanges.get(i);
            System.out.println(a.toString());
            byte[] partByteArray;
            int byteCounter = 0;

            partByteArray = new byte[a.getSize()];

            while (byteCounter != partByteArray.length) {
                partByteArray[byteCounter] = globalByteList[globalCounter];
                byteCounter++;
                globalCounter++;
            }

            System.out.println(byteCounter);
            System.out.println(partByteArray.length);
            byteList.add(partByteArray);
        }

        System.out.println("TOTAL_" + countBytes(byteList));

        return byteList;
    }

    public static void createFile(List<byte[]> byteList, String path) {
        int globalSize = 0;

        for (byte[] bytes : byteList) {
            globalSize += bytes.length;
        }
        System.out.println(globalSize);
        byte[] newFile = new byte[globalSize];

        int globalcounter = 0;
        for (byte[] bytes : byteList) {
            for (int i = 0; i < bytes.length; i++) {
                newFile[globalcounter] = bytes[i];
                globalcounter++;
            }
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileSubmitService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fos.write(newFile);
        } catch (IOException ex) {
            Logger.getLogger(FileSubmitService.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(FileSubmitService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected static byte[] getBytesFromMessage(Message m) {
        String[] array = m.getContent().split(",");
        String[] array2 = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = array[i].replaceAll(" |\\[|\\]", "");
        }
        List<byte[]> resultList = new ArrayList<>();
        int[] intBuffer = new int[array2.length];
        for (int i = 0; i < array2.length; i++) {
            intBuffer[i] = Integer.decode(array2[i]);
        }
        byte[] result = new byte[intBuffer.length];
        for (int i = 0; i < intBuffer.length; i++) {
            result[i] = (byte) intBuffer[i];
        }

        return result;
    }

    public static List<Message> createFileMessages(int ownID, int receiverID, String filePath, String fileName, int amountParts, String serviceID) {
        List<byte[]> byteList = getPartsOfFile(filePath, amountParts);
        int counter = 0;
        List<Message> partList = new ArrayList<>(amountParts);
        for (byte[] b : byteList) {
            Message m = new Message(ownID, receiverID, EnumKindOfMessage.FILEPART, Arrays.toString(b), createCustomInformation(counter, fileName, b.length, serviceID));
            partList.add(m);
            counter++;
        }
        return partList;
    }

    private static String createCustomInformation(int partID, String fileName, int lenOfPart, String SID) {
        return partID + " " + fileName + " " + lenOfPart + " " + SID;
    }

    private static void createDistribution(int globalSize, int parts) {
        int modulo = globalSize % parts;
        int amountPerPart = globalSize / parts;
        int step = amountPerPart;
        int rest = modulo + step;
        System.out.println("% = " + modulo);
        System.out.println("amPP = " + amountPerPart);
        System.out.println("step = " + step);
        System.out.println("parts = " + parts);
        System.out.println("glSize = " + globalSize);
        // Calculate the Distribution
        for (int i = 0; i < parts; i++) {
            if (i == 0) {
                partDistribution.put(i, step);
            } else {
                if (i + 1 == parts) {
                    partDistribution.put(i, step * (i) + rest);
                } else {
                    partDistribution.put(i, step * (i + 1));
                }
            }

        }

        System.out.println(partDistribution.toString());
        int range = 0;
        for (int i = 0; i < parts; i++) {
            if (i == 0) {
                range = partDistribution.get(i);
                partRanges.put(i, new Range(i, range));
            } else {
                range = partRanges.get(i - 1).upperLimit;
                if (i + 1 != parts) {
                    partRanges.put(i, new Range(range + 1, partDistribution.get(i)));
                } else {
                    partRanges.put(i, new Range(range + 1, range + rest));
                }
            }

        }

        System.out.println(
                "Distribution calculated:");
        System.out.println(
                "Pattern: [PartID=Range[Start to End]");
        System.out.println(partRanges.toString());

    }

    private static int countBytes(List<byte[]> bytes) {
        int result = 0;
        for (byte[] b : bytes) {
            result += b.length;
        }
        return result;
    }

    protected static class Range {

        private int upperLimit;

        private int bottomLimit;

        public Range(int bottomLimit, int upperLimit) {
            this.upperLimit = upperLimit;
            this.bottomLimit = bottomLimit;
        }

        public int getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(int upperLimit) {
            this.upperLimit = upperLimit;
        }

        public int getBottomLimit() {
            return bottomLimit;
        }

        public void setBottomLimit(int bottomLimit) {
            this.bottomLimit = bottomLimit;
        }

        public int getSize() {
            return (upperLimit - bottomLimit);
        }

        @Override
        public String toString() {
            return "[" + this.bottomLimit + " to " + this.upperLimit + " | " + this.getSize() + "]";
        }

    }

    protected static class WaitingService extends Thread {

        private String name;

        private int parts;

        private boolean live = true;

        private boolean success = false;

        private String connectionID;

        private Message message;

        private List<Integer> receivedParts;

        private int nameBuffer;

        private List<byte[]> byteList = new ArrayList<>();

        private byte[] result;

        private long startTime;

        private long endTime;

        private synchronized void tryToAddMessage(Message m) {
            this.message = m;
            String[] meta = new String[3];
            meta = m.getOthers().split(" ");
            String partID = meta[0];
            String name = meta[1];
            String len = meta[2];
            String id = meta[3];

            if (!receivedParts.contains(Integer.decode(partID))) {
                System.out.println("-- Service does not contain the PartId: " + partID + ", current: " + receivedParts.toString() + ". Is added.");
                receivedParts.add(Integer.decode(partID));
                byteList.add(FileSubmitService.getBytesFromMessage(m));
                MainUI.filePartSubmitted();
                System.out.println(receivedParts.size() + " " + parts);
                if (receivedParts.size() == parts) {
                    doFinish();
                }
            }

        }

        protected String getNameOfFile() {
            return this.name;
        }

        private long currentTime;

        private long timeout;

        private boolean isTimeoutOccured() {
            currentTime = System.currentTimeMillis();
            System.out.println(currentTime + " --- " + endTime + " ---- " + startTime);
            return (currentTime > endTime);
        }

        public WaitingService(String connectionID, String name, int parts, int timeout) {
            this.name = name;
            this.parts = parts;
            this.timeout = (long) timeout;
            this.connectionID = connectionID;
            this.receivedParts = new ArrayList<>();
            this.byteList = new ArrayList<>();
            this.startTime = System.currentTimeMillis();
            this.endTime = startTime + timeout;
        }

        private void doFinish() {
            FileSubmitService.createFile(byteList, name);
            FileSubmitService.deleteWaitingService(connectionID);
            MainUI.filePartSubmitEnd();
            this.live = false;
        }

        private void kill() {
            System.out.println("Error: Timeout occured");
            MainUI.filePartSubmitEnd();
            this.live = false;
        }

        @Override
        public void run() {
            System.out.println("Waiting for incoming data...");
            while (live) {
                if (isTimeoutOccured()) {
                    FileSubmitService.deleteWaitingService(connectionID);
                    kill();
                }
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileSubmitService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("WaitingService is died.");
        }

    }
}
