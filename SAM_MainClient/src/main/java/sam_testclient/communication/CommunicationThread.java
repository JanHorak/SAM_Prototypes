/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import sam_testclient.beans.ClientMainBean;
import sam_testclient.entities.Handshake;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumHandshakeReason;
import sam_testclient.enums.EnumHandshakeStatus;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.enums.EnumMessageStatus;
import sam_testclient.exceptions.NotAHandshakeException;
import sam_testclient.services.FileSubmitService;
import sam_testclient.services.HistoricizationService;
import sam_testclient.sources.FileManager;
import sam_testclient.sources.MessageWrapper;
import sam_testclient.ui.dialogs.BuddyRequestDialog;
import sam_testclient.ui.dialogs.FileRequestDialog;
import sam_testclient.ui.main.MainUI;
import sam_testclient.utilities.Utilities;

/**
 * CommunitactionThread of the Client. This thread manages the main connections
 * and the evaluation of the messages
 *
 * @author janhorak
 */
public class CommunicationThread extends Thread {

    private boolean live = true;
    private final JTextArea area;
    private int id;
    private static Client client;
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

                if (m.getMessageType() == EnumKindOfMessage.MESSAGE) {

                    // Received- Message back to sender
                    fireStatusMessage(m, EnumMessageStatus.RECEIVED);
                    m.setMessageStatus(EnumMessageStatus.RECEIVED);
                    
                    // Updateing the status
                    cmb.updateMessageStatus(m);
                    
                    
                    // UI (ReadMessage- Handling)
                    ui.distributeMessageToAreas(m);

                    // Hist
                    if (cmb.getSettings().isSaveLocaleHistory()) {
                        HistoricizationService.addMessageToCurrentHistory(m, false);
                    }
                    
                }
                                
                if (m.getMessageType() == EnumKindOfMessage.MESSAGE_STATUS) {
                    cmb.updateMessageStatus(m);
                }

                if (m.getMessageType() == EnumKindOfMessage.SYSTEM) {
                    area.append("\n" + m.getContent());
                }

                if (m.getMessageType() == EnumKindOfMessage.LOGIN_RESPONSE) {
                    client.setId(Integer.decode(m.getContent()));
                    client.sendStatusRequest();
                    area.append(Utilities.getLogTime() + " logged in\n");
                }

                if (m.getMessageType() == EnumKindOfMessage.FILEPART) {
                    String[] meta = m.getOthers().split(" ");
                    System.out.println(meta[3]);
                    FileSubmitService.addPartToService(meta[3], m);
                }

                if (m.getMessageType() == EnumKindOfMessage.HANDSHAKE) {
                    Handshake handshake = null;
                    try {
                        handshake = m.getHandshake();
                    } catch (NotAHandshakeException ex) {
                        Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (handshake.getReason() == EnumHandshakeReason.REGISTER) {
                        if (handshake.getStatus() == EnumHandshakeStatus.START) {
                            String secret = m.getContent();
                            String result = Utilities.calculateSecret(secret);
                            Message message = new Message(this.client.getId(), 0, EnumKindOfMessage.REGISTER, ui.tf_memberName.getText(), ui.getPW());
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
                                cmb.initCurrentHistoryMap(handshake.getContent());
                                client.createBuddyDir(handshake.getContent());
                                HistoricizationService.createEmptyHistFile(handshake.getContent());
                                FileManager.serialize(cmb.getBuddyList(), "resources/buddyList.data");
                                area.append(Utilities.getLogTime() + " " + handshake.getContent() + " is added to buddylist \n");
                                client.sendStatusRequest();
                            }

                        }
                    }
                    if (handshake.getReason() == EnumHandshakeReason.FILE_REQUEST) {
                        System.out.println("RECEIVED: " + m.toString());
                        if (handshake.getStatus() == EnumHandshakeStatus.START) {
                            area.append(Utilities.getLogTime() + "Server: FileRequest received\n");
                            new FileRequestDialog(ui, m).setVisible(true);
                        }
                        if (handshake.getStatus() == EnumHandshakeStatus.END) {
                            if (handshake.isAnswer() && !m.hasFile()) {
                                int parts = Integer.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "waitingServiceParts"));
                                String path = cmb.getLastFile().getFilePath();
                                String fileName = cmb.getLastFile().getFileName();
                                ui.prepareFileProgressbar(fileName, String.valueOf(parts));
                                List<Message> partList = FileSubmitService.createFileMessages(client.getId(), m.getSenderId(), path, fileName, parts, cmb.getLastFile().getId());
                                for (Message me : partList) {
                                    try {
                                        client.writeMessage(MessageWrapper.createJSON(me));
                                        MainUI.filePartSubmitted();
                                        sleep(1000);
                                    } catch (IOException | InterruptedException ex) {
                                        Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                cmb.setLastFile(null);
                                MainUI.filePartSubmitEnd();
                            }
                            if (!handshake.isAnswer()) {
                                area.append(Utilities.getLogTime() + "File-request denied\n");
                            }
                        }

                    }
                    if (handshake.getStatus() == EnumHandshakeStatus.WAITING) {
                    }
                }

                if (m.getMessageType() == EnumKindOfMessage.STATUS_RESPONSE) {
                    cmb.setBuddy_statusList(Utilities.getOnlineMap(m.getContent()));
                    updateUI();
                    ui.initTabPane(cmb.getBuddyList());
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
        int maxSize = Integer.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "messageMaxSize"));
        BufferedReader bufferedReader
                = new BufferedReader(
                        new InputStreamReader(
                                this.client.getServerSocket().getInputStream()));
        char[] buffer = new char[maxSize];
        int amountTokens = bufferedReader.read(buffer, 0, maxSize);
        String message = new String(buffer, 0, amountTokens);
        return message;
    }

    //@TODO: Has to be outsourced!!
    public void updateUI() {
        DefaultListModel lm = new DefaultListModel();
        if (!this.cmb.getBuddyList().isEmpty()) {
            Iterator it_value = cmb.getBuddy_statusList().values().iterator();
            Iterator it_id = cmb.getBuddy_statusList().keySet().iterator();
            while (it_value.hasNext()) {
                String content = (String) it_value.next();
                String[] contentArray = content.split(" ");
                String id = String.valueOf(it_id.next());
                String nameOfBuddy = cmb.getBuddyList().get(Integer.valueOf(id));
                String active = contentArray[0];
                String date = "";
                if (contentArray.length == 3) {
                    date = contentArray[1].concat(" " + contentArray[2]);
                }
                String status = Boolean.valueOf(active) ? "online" : "offline. Last seen at " + date;
                lm.addElement(nameOfBuddy + ": " + status);
            }
        } else {
            lm.addElement("You have no Buddies!!\nPoor!");
        }
        this.list_buddies.setModel(lm);
    }

    public static void fireStatusMessage(Message m, EnumMessageStatus status) {
        Message buffer = new Message(m.getSenderId(), m.getReceiverId(), EnumKindOfMessage.MESSAGE_STATUS, m.getId(), "");
        buffer.setMessageStatus(status);
        buffer.setReceiverId(buffer.getSenderId());
        buffer.setSenderId(client.getId());
        try {
            client.writeMessage(MessageWrapper.createJSON(buffer));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
