/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import sam_testclient.beans.ClientMainBean;
import sam_testclient.entities.Handshake;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumHandshakeReason;
import sam_testclient.enums.EnumHandshakeStatus;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.exceptions.NotAHandshakeException;
import sam_testclient.services.HistoricizationService;
import sam_testclient.sources.FileManager;
import sam_testclient.sources.MessageWrapper;
import sam_testclient.ui.dialogs.BuddyRequestDialog;
import sam_testclient.ui.dialogs.FileRequestDialog;
import sam_testclient.ui.main.MainUI;
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
    private JList list_buddies;
    private MainUI ui;
    private ClientMainBean cmb;

    public CommunicationThread(Client c, MainUI ui, int id) {
        this.ui = ui;
        this.client = c;
        this.area = ui.getArea();
        this.id = id;
        this.list_buddies = ui.getBuddyList();
        cmb = ClientMainBean.getInstance();
//        c.setBuddyList(cmb.getBuddyList());
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
                    String s = readMessage();
                    System.out.println(s);
                    m = MessageWrapper.JSON2Message(s);
                    System.out.println("received: " + m.toString());
                } catch (IOException ex) {

                }

                if (m.getMessageType() == EnumKindOfMessage.MESSAGE
                        || m.getMessageType() == EnumKindOfMessage.SYSTEM) {
                    area.append("\n" + m.getContent());
                    if (cmb.getSettings().isSaveLocaleHistory()){
                        HistoricizationService.historizeMessage(m, false);
                    }
                }
                if (m.getMessageType() == EnumKindOfMessage.LOGIN_RESPONSE) {
                    client.setId(Integer.decode(m.getContent()));
                    client.sendStatusRequest();
                    area.append(Utilities.getLogTime() + " logged in\n");
                }
                if (m.getMessageType() == EnumKindOfMessage.BUDDY_RESPONSE) {
                    // Buddy founded

                    cmb.getBuddyList().put(Integer.parseInt(m.getContent()), m.getOthers());
                    // Asking for Status of Buddies
                    client.sendStatusRequest();
                    // Save the new buddy
                    FileManager.serialize(cmb.getBuddyList(), "buddyList.data");
                    this.client.setBuddyList(cmb.getBuddyList());
                    area.append("\n" + new SimpleDateFormat("HH:mm").format(new Date()) + " Server: Buddy added!");
                }

                if (m.getMessageType() == EnumKindOfMessage.HANDSHAKE) {
                    Handshake handshake = null;
                    try {
                        handshake = m.getHandshake();
                    } catch (NotAHandshakeException ex) {
                        Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (handshake.getReason() == EnumHandshakeReason.REGISTER){
                        if (handshake.getStatus() == EnumHandshakeStatus.START){
                            String secret = m.getContent();
                            String result = Utilities.calculateSecret(secret);
                            Message message = new Message(this.client.getId(), 0, EnumKindOfMessage.REGISTER, ui.getName(), ui.getPW());
                            Handshake hs = new Handshake(500, EnumHandshakeStatus.END, EnumHandshakeReason.REGISTER, live, result);
                            message.setHandshake(hs);
                            try {
                                client.writeMessage(MessageWrapper.createJSON(message));
                            } catch (IOException ex) {
                                Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    
                    if (handshake.getReason() == EnumHandshakeReason.BUDDY_REQUEST) {
                        if (handshake.getStatus() == EnumHandshakeStatus.START) {
                            area.append(Utilities.getLogTime() + "Server: Buddyrequest received\n");
                            new BuddyRequestDialog(ui, m).setVisible(true);
                        }
                        if (handshake.getStatus() == EnumHandshakeStatus.END) {
                            // If the message is not coming from server then it is from the buddy
                            if (m.getReceiverId() != 0 && handshake.isAnswer()) {
                                cmb.getBuddyList().put(m.getSenderId(), handshake.getContent());
                                client.createBuddyDir(handshake.getContent());
                                FileManager.serialize(cmb.getBuddyList(), "buddyList.data");
                                area.append(Utilities.getLogTime() + " " + handshake.getContent() + " is added to buddylist \n");
                                client.sendStatusRequest();
                            } else {
//                            // The client doesn't have to be noticed
//                            area.append(Utilities.getLogTime() + " Server: " + "Client don't want to be your friend :P!\n");
                            }

                        }
                    }
                    if (handshake.getReason() == EnumHandshakeReason.FILE_REQUEST) {
                        System.out.println("RECEIVED: " + m.toString());
                        if (handshake.getStatus() == EnumHandshakeStatus.START) {
                            area.append(Utilities.getLogTime() + "Server: Buddyrequest received\n");
                            new FileRequestDialog(ui, m).setVisible(true);
                        }
                        if (handshake.getStatus() == EnumHandshakeStatus.END) {
                            if (handshake.isAnswer() && !m.hasFile()) {
                                Message me = new Message(handshake, cmb.getLastFile());
                                me.setSenderId(this.client.getId());
                                me.setReceiverId(m.getSenderId());
                                try {
                                    client.writeMessage(MessageWrapper.createJSON(me));
                                } catch (IOException ex) {
                                    Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (handshake.isAnswer() && m.hasFile()) {
                                ui.showSaveDialog(m);
                            }
                            if (!handshake.isAnswer()) {
                                area.append(Utilities.getLogTime() + "File-request denied\n");
                            }
                        }

                    }
                    if (handshake.getStatus() == EnumHandshakeStatus.WAITING) {
//                        // The client doesn't have to be noticed
//                        area.append(Utilities.getLogTime() + " Server: " + m.getContent() + " No answer from the client.\n"
//                                + "Maybe he is offline. The request will be sent later again.\n");
                    }
                }

                if (m.getMessageType() == EnumKindOfMessage.STATUS_RESPONSE) {
                    cmb.setBuddy_statusList(Utilities.getOnlineMap(m.getContent()));
                    updateUI();
                }

                try {
                    /**
                     * If the Server sends the messages from the buffer to the
                     * client, he has to "listen quickly". So the Time for sleep
                     * has to be ca. 50ms
                     *
                     * -> If occurs MalFormedExceptions for JSONs they are
                     * really unexpected it is possible that the client cannot
                     * listen as quick as needed
                     */
                    sleep(50);
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
        if (!this.cmb.getBuddyList().isEmpty()) {
            Iterator it = cmb.getBuddyList().keySet().iterator();
            while (it.hasNext()) {
                int number = (int) it.next();
                System.out.println(number);
                String status = cmb.getBuddy_statusList().get(number) ? "online" : "offline";
                lm.addElement(status + ": " + cmb.getBuddyList().get(number));
            }
        } else {
            lm.addElement("You have no Buddies!!\nPoor!");
        }
        this.list_buddies.setModel(lm);
    }
}
