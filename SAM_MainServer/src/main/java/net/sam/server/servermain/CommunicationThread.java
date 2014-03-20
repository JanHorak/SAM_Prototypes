/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.servermain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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
                if (m.getMessageType() == EnumKindOfMessage.REGISTER) {
                    Member newMember = new Member();
                    if (m.getSenderId() == 0) {
                        newMember.setName(m.getContent());
                        newMember.setPassword(m.getOthers());
                        newMember.setActive(true);
                        logger.info(Utilities.getLogTime() + " New Member registered");
                        logger.info(Utilities.getLogTime()+ " "+ newMember.toString());
                    }
                    DataAccess.registerUser(newMember);
                    area.append("\n"+Utilities.getLogTime()+ " User registered:");
                    area.append("\n"+Utilities.getLogTime()+ " "+ newMember.toString());
                    area.append("\n");
                    sb.addMember_registered(newMember);
                }

                if (m.getMessageType() == EnumKindOfMessage.LOGIN) {
                    if (DataAccess.login(m.getOthers())) {
                        Member member = DataAccess.getMemberByName(m.getContent());
                        sb.addMember_login(member);
                        area.append("\n"+Utilities.getLogTime()+ " "+member.getName()+ "logged in");
                        logger.info(Utilities.getLogTime() + " Member is logged in");
                    } else {
                        area.append("\n"+Utilities.getLogTime()+ " User logged tried to log in and failed:");
                        area.append("\n"+Utilities.getLogTime()+ " "+ m.toString());
                        area.append("\n");
                    }
                }

                if (m.getMessageType() == EnumKindOfMessage.LOGOUT) {
                    area.append("\n"+Utilities.getLogTime()+ " Logoutsignal recieved!");
                    area.append("\n"+Utilities.getLogTime()+ " "+ m.toString());
                    area.append("\n");
                    Member me = new Member();
                    me.setName(m.getContent());
                    sb.logoutMember(me);
                    try {
                        this.socket.close();
                    } catch (IOException ex) {
                        logger.error("Error at closing client-Socket: " +ex);
                    }
                }

                if (m.getMessageType() == EnumKindOfMessage.MESSAGE) {
                    // 0 indicates message to server
                    if (m.getRecieverId() == 0) {
                        area.append(m.getContent());
                    }
                }

                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    logger.error(Utilities.getLogTime() + " Thread is interrupted! " + ex);
                }
            }else{
                logger.info(Utilities.getLogTime()+ " Socket of Client is closed!");
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

    private void forwardMessage() {

    }

    private void broadcast() {

    }
}
