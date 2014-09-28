/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.communication;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sam_testclient.beans.ClientMainBean;
import sam_testclient.entities.Buddy;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.sources.MessageWrapper;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class Client extends ClientServerCommunicationBase {

    private Socket serverSocket;

    public static int ownID = Utilities.generateRandomNumberBetween(9999, 9999999);

    private Map<Integer, String> buddyList;
    
    private ClientMainBean cmb;
    
    public Client(){
        cmb = ClientMainBean.getInstance();
    }

    public Map<Integer, String> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(Map<Integer, String> buddyList) {
        this.buddyList = buddyList;
    }

    public synchronized int getId() {
        return this.ownID;
    }

    public synchronized void setId(int id) {
        this.ownID = id;
    }

    public Socket getServerSocket() {
        return serverSocket;
    }

    public void connect() throws IOException {
        serverSocket = new Socket(IP, PORT);
    }

    public void writeMessage(String message) throws IOException {
        PrintWriter printWriter
                = new PrintWriter(
                        new OutputStreamWriter(
                                serverSocket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    /**
     * Returns the formated String of IDs for the Status request. The String is
     * the content of the Request message Like 1 3 For the Ids of Friends in the
     * own buddyList
     *
     * @return
     */
    private String formatStatusRequest() {
        String result = "";
        for (Buddy b : cmb.getBuddyList()) {
            result += b.getInternalID() + " ";
        }
        return result;
    }
    
    public void sendStatusRequest() {
        Message buddy_status = new Message(this.ownID, 0, EnumKindOfMessage.STATUS_REQUEST, formatStatusRequest(), "");
        String buddy_status_request = MessageWrapper.createJSON(buddy_status);
        try {
            writeMessage(buddy_status_request);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void createBuddyDir(String buddyName){
        String mainDir = "resources/buddies/"+buddyName;
        String media = mainDir+"/media";
        File videos = new File(media+"/videos");
        File images = new File(media+"/images");
        File history = new File(mainDir+"/history");
        List<File> folderList = new ArrayList<>();
        folderList.add(videos);
        folderList.add(images);
        folderList.add(history);
        for (File folder : folderList){
            folder.mkdirs();
        }
        System.out.println("Folders for new Buddy created.");
    }
}
