/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import sam_testclient.utilities.Utilities;

/**
 * CommunitactionThread of the Server. This thread manages the main connections
 * and the evaluation of the messages
 *
 * @author janhorak
 */
public class CommunicationThread extends Thread {

    private boolean live = true;
    private final JTextArea area;
    private int id;
    private Client client;
    private Map<Integer, String> buddyList;
    private Map<Integer, Boolean> buddy_statusList;
    private JList list_buddies;

    public CommunicationThread(Client c, JTextArea area, JList list_buddies, int id) {
        this.client = c;
        this.area = area;
        this.id = id;
        this.list_buddies = list_buddies;
        buddyList = (Map<Integer, String>) FileManager.deserialize("buddyList.data");
        buddy_statusList = new HashMap<Integer, Boolean>();
        c.setBuddyList(buddyList);
//        updateUI();
    }

    /**
     * Note: ID is passed temporary in the prototype!! It has to be outsourced
     * in a Bean or singleton or something else
     */
    @Override
    public void run() {
        System.out.println("Thread started!");

        while (live) {
            if (!this.client.getServerSocket().isClosed()) {
                Message m = null;
                try {
                    // JSON handling
                    m = MessageWrapper.JSON2Message(readMessage());
                    System.out.println("recieved: " + m.toString());
                } catch (IOException ex) {

                }

                if (m.getMessageType() == EnumKindOfMessage.MESSAGE
                        || m.getMessageType() == EnumKindOfMessage.SYSTEM) {
                    area.append("\n" + m.getContent());
                }
                if (m.getMessageType() == EnumKindOfMessage.LOGIN_RESPONSE) {
                    this.client.setId(m.getRecieverId());
                    Message buddy_status = new Message(client.getId(), 0, EnumKindOfMessage.STATUS_REQUEST, formatStatusRequest(), null);
                    String buddy_status_request = MessageWrapper.createJSON(buddy_status);
                    try {
                        client.writeMessage(buddy_status_request);
                    } catch (IOException ex) {
                        Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (m.getMessageType() == EnumKindOfMessage.BUDDY_RESPONSE) {
                    buddyList.put(Integer.parseInt(m.getContent()), m.getOthers());
                    System.out.println("Buddy added!");
                    FileManager.serialize(buddyList, "buddyList.data");
                    area.append("\n" + new SimpleDateFormat("HH:mm").format(new Date()).toString() + " Server: Buddy added!");
                    updateUI();
                }

                if (m.getMessageType() == EnumKindOfMessage.STATUS_RESPONSE) {
                    buddy_statusList = Utilities.getOnlineMap(m.getContent());
                    updateUI();
                }
                
                try {
                    sleep(500);
                } catch (InterruptedException ex) {

                }
            } else {

                live = false;
            }

        }
    }

    /**
     * Method for reading the Inputstream of the Socket
     *
     * @return - Content of the stream
     * @throws IOException
     */
    private String readMessage() throws IOException {
        BufferedReader bufferedReader
                = new BufferedReader(
                        new InputStreamReader(
                                this.client.getServerSocket().getInputStream()));
        char[] buffer = new char[250];
        int amountTokens = bufferedReader.read(buffer, 0, 250);
        String message = new String(buffer, 0, amountTokens);
        return message;
    }

    //@TODO: Has to be outsourced!!
    public void updateUI() {
        DefaultListModel lm = new DefaultListModel();
        if (!this.buddyList.isEmpty()) {
            for (Integer i : buddyList.keySet()) {
                String status = buddy_statusList.get(i) ? "online" : "offline";
                lm.addElement(status + ": "+ buddyList.get(i));
            }
        } else {
            lm.addElement("You have no Buddies!!\nPoor!");
        }
        this.list_buddies.setModel(lm);
    }

    //@TODO: Has to be outsourced!!
    private String formatStatusRequest() {
        String result = "";
        for (Object k : buddyList.keySet().toArray()) {
            result += k.toString()+" ";
        }
        return result;
    }

}
