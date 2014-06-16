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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JTextArea;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.entities.Handshake;
import net.sam.server.entities.Member;
import net.sam.server.entities.Message;
import net.sam.server.entities.MessageBuffer;
import net.sam.server.enums.EnumHandshakeReason;
import net.sam.server.enums.EnumHandshakeStatus;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.exceptions.NotAHandshakeException;
import net.sam.server.manager.DataAccess;
import net.sam.server.manager.MessageWrapper;
import net.sam.server.security.DoSGuard;
import net.sam.server.services.ContainerService;
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
    private DoSGuard guard_dos;

    public CommunicationThread(Socket socket, JTextArea area) {
        this.socket = socket;
        this.area = area;
        sb = ContainerService.getBean(ServerMainBean.class);
        guard_dos = DoSGuard.getInstance();
        logger = Logger.getLogger(CommunicationThread.class);
    }

    @Override
    public void run() {
        logger.info(Utilities.getLogTime() + " Client is connected at: " + socket.toString());

        if (guard_dos.isConnectedSocketHarmless(socket.getInetAddress())) {
            while (live) {
                if (!this.socket.isClosed()) {
                    Message m = null;
                    try {
                        // JSON handling
                        m = MessageWrapper.JSON2Message(readMessage());
                    } catch (IOException ex) {
                        logger.error(Utilities.getLogTime() + " Error at MessageWrapper");
                    }

                    /**
                     * If the Message is still null this indicates a serverside
                     * shutdown.
                     */
                    if (m != null) {

                        // Cases for EnumKinds --------------------
                    /*
                         ====================== REGISTER =====================
                         */
                        /**
                         * This part defines the Register- Action.
                         *
                         * If the received Message contains the RESGISTER- Flag
                         * and have the Sender- ID 0 (On the one hand is the 0
                         * the serverID, on the other an indicator for not
                         * registered, or not logged in clients), the server has
                         * to try to register the client.
                         *
                         * The content is the name of the client, the other-
                         * part is the hashed password. The server tests if a
                         * client is already registered with the incoming name
                         * and adds the client to the registered- List (internal
                         * for quick access). Else the server saves the new
                         * client.
                         */
                        // @TODO: Improvement of REGISTER- Part
                        if (m.getMessageType() == EnumKindOfMessage.REGISTER) {
                            Member newMember = new Member();
                            if (m.getSenderId() == 0) {
                                newMember.setName(m.getContent());
                                newMember.setPassword(m.getOthers());
                                newMember.setActive(true);
                                System.out.println(m.toString());
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
                        /**
                         * This part defines the Login- Action.
                         *
                         * If the server gets a JSON with LOGIN- Flag he tests
                         * if a member exists and test the password. The Name of
                         * the user is send in the content- and the password in
                         * the other- part.
                         *
                         * If the login was ok, the server adds the member to
                         * the logged in- List (in ServerMainBean) with the
                         * corresponding socket.
                         *
                         * After this the server looking for the right MemberId
                         * in the Database and send the Login- Response with the
                         * id back to the client which applies the id.
                         *
                         * The Server has to test, if there are Messages for the
                         * client. If there are messages the server get them
                         * from the database, send them to the client and delete
                         * them from the buffer.
                         */
                        if (m.getMessageType() == EnumKindOfMessage.LOGIN) {
                            // Check if the user is not already online
                            if (!sb.isMemberOnline(m.getContent())) {
                                // Check login- data
                                Member me = DataAccess.getMemberByName(m.getContent());
                                if (DataAccess.login(me)) {
                                    Member member = DataAccess.getMemberByName(m.getContent());
                                    sb.addMember_login(member, socket);

                                    sendLoginResponseMessage(member);
                                    area.append("\n" + Utilities.getLogTime() + " " + member.getName() + " logged in");
                                    logger.info(Utilities.getLogTime() + " Member is logged in");

                                    // Get data from Buffer
                                    List<MessageBuffer> messageBufferList = DataAccess.getAllMessagesFromBuffer(member.getUserID());
                                    if (!messageBufferList.isEmpty()) {
                                        for (MessageBuffer mw : messageBufferList) {
                                            Message message = mw.getMessage();

                                            if (message.isHandshake()) {
                                                message = Message.cleanUpHandshake(message);
                                            }
                                            logger.debug("Message from Buffer for User " + m.getSenderId() + ": " + message.toString());
                                            forwardMessage(message);
                                        }
                                        DataAccess.dropMessagesFromBuffer(member.getUserID());
                                    } else {
                                        logger.info(Utilities.getLogTime() + " No Messages in Buffer for this Member");
                                    }

                                } else {
                                    area.append("\n" + Utilities.getLogTime() + " User logged tried to log in and failed:");
                                    area.append("\n" + Utilities.getLogTime() + " " + m.toString());
                                    area.append("\n");
                                }

                                // Member is already logged in 
                            } else {
                                Message hint = new Message(0, m.getSenderId(), EnumKindOfMessage.SYSTEM, "Login failed (1001)", "");
                                try {
                                    guard_dos.increaseLevelForSocket(socket.getInetAddress());
                                    this.writeMessage(socket, MessageWrapper.createJSON(hint));
                                    socket.close();
                                    this.live = false;
                                } catch (IOException ex) {
                                    logger.error("IO Exception: " + ex);
                                }
                            }

                        }

                        /*
                         ====================== LOGOUT =====================
                         */
                        /**
                         * This part defines the LOGOUT- part of the client.
                         *
                         * If the Signal for the logout is received, the server
                         * has to clean up the own quick- Memory
                         * (logoutMember(Member m) -> See for more Info)
                         *
                         */
                        // @TODO: Improvement!!
                        if (m.getMessageType() == EnumKindOfMessage.LOGOUT) {
                            area.append("\n" + Utilities.getLogTime() + " Logoutsignal recieved!");
                            area.append("\n " + "Logged out" + m.getContent());
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
                         ====================== STATUS- REQUEST =====================
                         */

                        /**
                         * This defines the behavior of the Status- Requests of the
                         * Client.
                         * 
                         * Because the request can be send after the first login
                         * (with no buddies) the status can be empty. So it has
                         * to be verified that the content is not empty - in
                         * this case the content of the message is a simple
                         * <code>toString()</code> - Method of the loaded
                         * buddyList of the client. Because this will be loaded
                         * as List the content has the form: 3 5 for the status-
                         * request of the buddies 3 and 5.
                         *
                         * Because it is important to reduce the sent messages
                         * the server bundles the incoming request in a
                         * <code>Map<Integer, Boolean></code> with the Id and
                         * the status [online = true, offline = false]. This map
                         * will be sent back to the client. The content will has
                         * the pattern: {id1=boolean1, id2=boolean2,...} like:
                         * {3=true, 5=false} This has to be interpreted by the
                         * client.
                         */
                        if (m.getMessageType() == EnumKindOfMessage.STATUS_REQUEST) {
                            logger.info(Utilities.getLogTime() + " Status Request from User " + m.getSenderId() + " received");
                            if (!m.getContent().isEmpty()) {
                                String[] requestList = m.getContent().split(" ");
                                Map< Integer, Boolean> buddy_online_Response = sb.getOnlineStatusOfMemberById(requestList);
                                Message statusResponse = new Message(0, m.getSenderId(), EnumKindOfMessage.STATUS_RESPONSE, buddy_online_Response.toString(), "");
                                try {
                                    this.writeMessage(sb.returnCommnunicationChannel(m.getSenderId()), MessageWrapper.createJSON(statusResponse));
                                } catch (IOException ex) {
                                    logger.error("Error @ Status-Response: " + ex);
                                }
                            }
                        }

                        /*
                         ====================== SIMPLE MESSAGE =====================
                         */
                        /**
                         * This part defines the SIMPLE MESSAGE from the client.
                         *
                         * The incoming Message can be divided into two kinds:
                         * -> with server as receiver -> with other client as
                         * receiver
                         *
                         * If the server is the receiver it will has the id 0
                         * and will be printed at the textarea in the UI.
                         *
                         * If the server is not the receiver it will be
                         * forwarded to other clients. [Hint: if the message has
                         * to be written in the messagebuffer, because the
                         * client is not online, is placed in the forward-
                         * method]
                         *
                         */
                        if (m.getMessageType() == EnumKindOfMessage.MESSAGE) {
                            logger.info(Utilities.getLogTime() + " Message received");
                            // 0 indicates message to server
                            if (m.getReceiverId() == 0) {
                                area.append(m.getContent());
                            } else {
                                logger.info(Utilities.getLogTime() + " Message not for server");
                                forwardMessage(m);
                            }
                        }

                        /*
                         ====================== HANDSHAKES =====================
                         */
                        /**
                         * This part defines the handling of HANDSHAKES.
                         *
                         * Every Handshakehas two important states: the START
                         * and the END. In every kind of Handshake between the
                         * clients both states have to be implemented.
                         *
                         * REQUESTKINDS ------------ -> BUDDY_REQUEST If the
                         * Server receives a START of a Buddy- Request Handshake
                         * he has to find the ID of the requested user and
                         * modify the data of the Handshake, too. Then the
                         * Handshake has to be forwarded. In the END he has to
                         * handle if the response is positive or not. If not the
                         * Server sends the response to the requesting client
                         * and replaces the sender-ID by the server-ID.
                         */
                        if (m.getMessageType() == EnumKindOfMessage.HANDSHAKE) {
                            Handshake handshake = null;
                            try {
                                handshake = m.getHandshake();
                            } catch (NotAHandshakeException ex) {
                                logger.error("Error in received Handhake: " + ex);
                            }

                            // Handshake for Buddy-Request
                            if (handshake.getReason() == EnumHandshakeReason.BUDDY_REQUEST) {
                                logger.info(Utilities.getLogTime() + " Handshake received");
                                logger.info(m.toString());
                                // If the status is Start
                                if (handshake.getStatus() == EnumHandshakeStatus.START) {

                                    // Get unknwon ID of the receiver by requested Name
                                    int recieverID = sb.getRegisteredMemberIdByName(handshake.getContent()).getUserID();

                                    // Replace the ServerID with the real ID of the Receiver
                                    m.setReceiverId(recieverID);

                                    // Forwarding
                                    forwardMessage(m);
                                }

                                if (handshake.getStatus() == EnumHandshakeStatus.END) {
                                    if (handshake.isAnswer()) {
                                        forwardMessage(m);
                                    } else {
                                        m.setSenderId(0);
                                        forwardMessage(m);
                                    }
                                }
                            }
                            if (handshake.getReason() == EnumHandshakeReason.FILE_REQUEST){
                                logger.info(Utilities.getLogTime() + " Handshake received");
                                logger.info(m.toString());
                                // If the status is Start
                                forwardMessage(m);
                            }
                        }
                    } else {
                        socketIsClosedHandler();

                    }

                    try {
                        sleep(500);
                    } catch (InterruptedException ex) {
                        logger.error(Utilities.getLogTime() + " Thread is interrupted! " + ex);
                    }
                } else {
                    socketIsClosedHandler();
                }
            }
        } else {
            logger.warn(Utilities.getLogTime() + " Socket is classified as threat! Connection refused!");
        }

    }

    private void socketIsClosedHandler() {
        logger.info(Utilities.getLogTime() + " Socket of Client is closed!");
        live = false;
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

    /**
     * Simple write- method for messages.
     *
     * @param socket
     * @param message
     * @throws IOException
     */
    private void writeMessage(Socket socket, String message) throws IOException {
        PrintWriter printWriter
                = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    /**
     * Forwards a simple message to the receiver.
     *
     * @param m
     */
    private void forwardMessage(Message m) {
        if (sb.isMemberOnline(m.getReceiverId())) {
            if (m.getMessageType() == EnumKindOfMessage.MESSAGE) {
                Member sender = sb.getLoggedInMemberById(m.getSenderId());
                m.setContent(formatMessage(sender.getName(), m.getContent()));
            }
            String json = MessageWrapper.createJSON(m);
            try {
                writeMessage(sb.returnCommnunicationChannel(m.getReceiverId()),
                        json);
            } catch (IOException ex) {
                logger.error("Cannot get Communicationchannel! " + ex);
            }
        } else {
            logger.warn("Member is not online - Save Message in Buffer!");
            DataAccess.saveMessageInBuffer(m);
            logger.debug(Utilities.getLogTime() + " Message is saved in Buffer\n" + m.toString());
        }
    }

    private void broadcast(Message m) {
        m.setContent(formatMessage("BroadCast", m.getContent()));
        for (Socket s : sb.getAllSockets()) {
            try {
                writeMessage(s, MessageWrapper.createJSON(m));

            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CommunicationThread.class
                        .getName()).log(Level.SEVERE, null, ex);
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
            logger.debug("LoginResponse is sent: " + m.toString());
        } catch (IOException ex) {
            logger.error("Cannot send LoginResponse! " + ex);
        }
    }

}
