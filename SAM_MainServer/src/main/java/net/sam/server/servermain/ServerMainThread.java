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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JTextArea;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.entities.Member;
import net.sam.server.security.SecurityThread;
import net.sam.server.services.ContainerService;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 * This is the Mainthread of the Server.
 *
 * @author janhorak
 */
public class ServerMainThread implements Runnable{

    private ServerMainBean serverMainBean;
    private List<Member> userList;
    private int maxUsers;
    private List<Socket> socketList;
    private List<CommunicationThread> commList;
    private boolean active;
    private Server server;
    private Socket socket;
    private boolean live = true;
    private JTextArea area;
    private Logger logger;
    private SecurityThread sct;
    private ExecutorService executorPool;

    public ServerMainThread(Server server, int maxUsers, List<Member> userList, 
            boolean active, JTextArea area, ExecutorService serverMainPool) {
        this.executorPool = serverMainPool;
        this.maxUsers = maxUsers;
        this.userList = userList;
        this.active = active;
        this.server = server;
        this.area = area;
        this.commList = new ArrayList<>();
        serverMainBean = ContainerService.getBean(ServerMainBean.class);
        logger = Logger.getLogger(ServerMainThread.class);
        sct = new SecurityThread();
        executorPool.execute(sct);
    }

    @Override
    public void run() {
        logger.info(Utilities.getLogTime() + " Server Main Thread started");
        socketList = new ArrayList<>();
        while (live) {
            if (userList.size() <= maxUsers) {
                socket = null;
                try {
                    logger.info(Utilities.getLogTime() + " Waiting for clients...");
                    socket = server.getServerSocket().accept();
                    socketList.add(socket);
                    CommunicationThread cmt = new CommunicationThread(socket, area);
                    commList.add(cmt);
                    executorPool.execute(cmt);
                } catch (IOException ex) {
                    /*
                     The Problem is the Blocking- Method of the Serversocket.
                     The close()- Method of the Serversocket will 
                     throw an exception automaticly. We use this to cancel the
                     Thread.
                     */
                    logger.error("Timeout of serverAccept | Other Exception: " + ex);
                    live = false;
                }
            }
        }
        logger.warn(Utilities.getLogTime() + " Server is stopped");
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

    public void interrupt() {
        for (Socket s : socketList){
            try {
                s.close();
            } catch (IOException ex) {
                logger.error(Utilities.getLogTime() + " Socket of Client can not be closed! " + ex);
            }
        }
        executorPool.shutdownNow();
    }
}
