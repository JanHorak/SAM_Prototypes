/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.servermain;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.entities.Member;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;



/**
 * This is the Mainthread of the Server.
 * @author janhorak
 */
public class ServerMainThread extends Thread {


    private ServerMainBean serverMainBean;
    private List<Member> userList;
    private int maxUsers;
    private List<Socket> socketList;
    private boolean active;
    private Server server;
    private Socket socket;
    private boolean live = true;
    private JTextArea area;
    private Logger logger;

    public ServerMainThread(Server server, int maxUsers, List<Member> userList, boolean active, JTextArea area) {
        this.maxUsers = maxUsers;
        this.userList = userList;
        this.active = active;
        this.server = server;
        this.area = area;
        serverMainBean = ServerMainBean.getInstance();
        logger = Logger.getLogger(ServerMainThread.class);
    }

    @Override
    public void run() {
        logger.info(Utilities.getLogTime()+" Server Main Thread started");
        socketList = new ArrayList<>();
        while (live) {
            if (userList.size() <= maxUsers) {
                socket = null;
                try {
                    logger.info(Utilities.getLogTime()+" Waiting for clients...");
                    socket = server.getServerSocket().accept();
                    socketList.add(socket);
                    //TODO: <- Start of the Communicationthread here
                    new CommunicationThread(socket, area).start();
                } catch (IOException ex) {
                    /*
                    The Problem is the Blocking- Method of the Serversocket.
                    The close()- Method of the Serversocket will 
                    throw an exception automaticly. We use this to cancel the
                    Thread.
                    */
                    logger.error("Timeout of serverAccept | Other Exception: "+ex);
                    live = false;
                }
            }
        }
        logger.warn(Utilities.getLogTime() +" Server is stopped");
    }

    /**
     * This method clears the Serversettings and prepares for shutdown.
     * 
     * @throws IOException 
     */
    public void kill() throws IOException {
        for (Member u : userList) {
            u.getSocket().close();
        }
        userList.clear();
        interrupt();
    }

    @Override
    public void interrupt() {
        try {
            server.getServerSocket().close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }
}
