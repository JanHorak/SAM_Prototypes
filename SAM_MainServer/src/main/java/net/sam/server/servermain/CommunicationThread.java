/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.servermain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JTextArea;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.entities.Member;
import net.sam.server.entities.Message;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.manager.DataAccess;
import net.sam.server.manager.MessageWrapper;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 * CommunitactionThread of the Server. This thread manages the main connections
 * and the evaluation of the messages
 *
 * @author janhorak
 */
public class CommunicationThread extends Thread {

    private final Socket socket;
    private boolean live = true;
    private final JTextArea area;
    private ServerMainBean sb;
    private Logger logger;

    public CommunicationThread(Socket socket, JTextArea area) {
        this.socket = socket;
        this.area = area;
        sb = ServerMainBean.getInstance();
        logger = Logger.getLogger(CommunicationThread.class);
    }

    @Override
    public void run() {
        logger.info(Utilities.getLogTime() + " Client is connected at: " + socket.toString());
        while (live) {
            if (!this.socket.isClosed()) {
                Message m = null;
                try {
                    // JSON handling
                    m = MessageWrapper.JSON2Message(readMessage());
                } catch (IOException ex) {
                    logger.error(Utilities.getLogTime() + " Error at MessageWrapper");
                }

                // Cases for EnumKinds --------------------
                /*
                 ====================== REGISTER =====================
                 */
                if (m.getMessageType() == EnumKindOfMessage.REGISTER) {
                    Member newMember = new Member();
                    if (m.getSenderId() == 0) {
                        newMember.setName(m.getContent());
                        newMember.setPassword(m.getOthers());
                        newMember.setActive(true);
                        logger.info(Utilities.getLogTime() + " New Member registered");
                        logger.info(Utilities.getLogTime() + " " + newMember.toString());
                    }
                    DataAccess.registerUser(newMember);
                    area.append("\n" + Utilities.getLogTime() + " User registered:");
                    area.append("\n" + Utilities.getLogTime() + " " + newMember.toString());
                    area.append("\n");
                    sb.addMember_registered(newMember);
                }

                /*
                 ====================== LOGIN =====================
                 */
                if (m.getMessageType() == EnumKindOfMessage.LOGIN) {
                    if (DataAccess.login(m.getOthers())) {
                        Member member = DataAccess.getMemberByName(m.getContent());
                        sb.addMember_login(member, socket);

                        sendLoginResponseMessage(member);
                        area.append("\n" + Utilities.getLogTime() + " " + member.getName() + "logged in");
                        logger.info(Utilities.getLogTime() + " Member is logged in");
                    } else {
                        area.append("\n" + Utilities.getLogTime() + " User logged tried to log in and failed:");
                        area.append("\n" + Utilities.getLogTime() + " " + m.toString());
                        area.append("\n");
                    }
                }

                /*
                 ====================== LOGOUT =====================
                 */
                if (m.getMessageType() == EnumKindOfMessage.LOGOUT) {
                    area.append("\n" + Utilities.getLogTime() + " Logoutsignal recieved!");
                    area.append("\n" + Utilities.getLogTime() + " " + m.toString());
                    area.append("\n");
                    Member me = new Member();
                    me.setName(m.getContent());
                    sb.logoutMember(me);
                    try {
                        this.socket.close();
                    } catch (IOException ex) {
                        logger.error("Error at closing client-Socket: " + ex);
                    }
                }

                /*
                 ====================== BUDDY-REQUEST =====================
                 */
                // Note: This is prototyped! We need to do a handshake!!
                // @TODO: HANDSHAKE! -> ISSUE: #27
                if (m.getMessageType() == EnumKindOfMessage.BUDDY_REQUEST) {
                    Member memberRequest = DataAccess.getMemberByName(m.getContent());
                    String json = "";
                    if (memberRequest != null) {
                        try {
                            Message response = new Message(0, m.getSenderId(), EnumKindOfMessage.BUDDY_RESPONSE,
                                    String.valueOf(memberRequest.getUserID()), m.getContent());
                            json = MessageWrapper.createJSON(response);
                            writeMessage(sb.returnCommnunicationChannel(m.getSenderId()), json);
                            logger.debug("Response sent: " + response.toString());
                        } catch (IOException ex) {
                            logger.error("Error at Memberrequest (member was found): " + ex);
                        }
                    } else {
                        try {
                            Message response = new Message(0, m.getSenderId(), EnumKindOfMessage.SYSTEM,
                                    "Server: Member not found", "");
                            json = MessageWrapper.createJSON(response);
                            writeMessage(sb.returnCommnunicationChannel(m.getSenderId()), json);
                            logger.debug("Response sent: " + response.toString());
                        } catch (IOException ex) {
                            logger.error("Error at Memberrequest: " + ex);
                        }
                    }
                }
                
                /*
                 ====================== BUDDY-STATUS-REQUEST =====================
                 */
                if (m.getMessageType() == EnumKindOfMessage.STATUS_REQUEST){
                    String[] requestList = m.getContent().split(" ");
                    Map<Integer, Boolean> buddy_online_Response = sb.getOnlineStatusOfMemberById(requestList);
                    Message statusResponse = new Message(0, m.getSenderId(), EnumKindOfMessage.STATUS_RESPONSE, buddy_online_Response.toString(), "");
                    try {
                        this.writeMessage(sb.returnCommnunicationChannel(m.getSenderId()), MessageWrapper.createJSON(statusResponse));
                    } catch (IOException ex) {
                        logger.error("Error @ Status-Response: " + ex);
                    }
                }

                /*
                 ====================== SIMPLE MESSAGE =====================
                 */
                if (m.getMessageType() == EnumKindOfMessage.MESSAGE) {
                    // 0 indicates message to server
                    if (m.getRecieverId() == 0) {
                        area.append(m.getContent());
                    } else {
                        forwardMessage(m);
                        logger.info("Message forwarded: " + m.toString());
                    }
                }

                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    logger.error(Utilities.getLogTime() + " Thread is interrupted! " + ex);
                }
            } else {
                logger.info(Utilities.getLogTime() + " Socket of Client is closed!");
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
                                socket.getInputStream()));
        char[] buffer = new char[250];
        int amountTokens = bufferedReader.read(buffer, 0, 250);
        String message = new String(buffer, 0, amountTokens);
        return message;
    }

    private void writeMessage(Socket socket, String message) throws IOException {
        PrintWriter printWriter
                = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    private void forwardMessage(Message m) {
        if (sb.isMemberOnline(m.getRecieverId())) {
            Member sender = sb.getMemberById(m.getSenderId());
            m.setContent(formatMessage(sender.getName(), m.getContent()));
            String json = MessageWrapper.createJSON(m);
            try {
                writeMessage(sb.returnCommnunicationChannel(m.getRecieverId()),
                        json);
            } catch (IOException ex) {
                logger.error("Cannot get Communicationchannel! " + ex);
            }
        } else {
            logger.warn("Member is not online!");
            // @TODO: Write in messagebuffer
        }
    }
    
    

    private void broadcast(Message m) {
        m.setContent(formatMessage("BroadCast", m.getContent()));
        for (Socket s : sb.getAllSockets()){
            try {
                writeMessage(s, MessageWrapper.createJSON(m));
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String formatMessage(String sender, String content) {
        return Utilities.getTime() + " " + sender + ": " + content;
    }

    private void sendLoginResponseMessage(Member me) {
        // Get right MemberID
        Message m = new Message(0, me.getUserID(), EnumKindOfMessage.LOGIN_RESPONSE, String.valueOf(me.getUserID()), "");
        try {
            writeMessage(sb.returnCommnunicationChannel(me.getUserID()), MessageWrapper.createJSON(m));
            logger.debug("LoginResponse is sended: " + m.toString());
        } catch (IOException ex) {
            logger.error("Cannot send LoginResponse! " + ex);
        }
    }

}
