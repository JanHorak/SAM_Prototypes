/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.servermain;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JTextArea;
import net.sam.server.entities.Member;
import net.sam.server.interfaces.ClientServerCommunicationBase;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class Server implements ClientServerCommunicationBase {

    private ServerSocket serverSocket;

    private final int MAXUSERS = 5;

    private List<Member> userList;

    private ServerMainThread smt;

    private JTextArea messageArea;

    private ExecutorService executorPool;
    private ExecutorService serverMainPool;
    private Logger logger;

    public Server(JTextArea messageArea) {
        logger = Logger.getLogger(Server.class);
        this.messageArea = messageArea;
        try {
            userList = new ArrayList<Member>(MAXUSERS);
            this.startUpServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startUpServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.serverSocket.setSoTimeout(0); // Timeout of 0 -> infinite
        // Use Timeout from Base, normally

        executorPool = Executors.newSingleThreadExecutor();
        serverMainPool = Executors.newFixedThreadPool(MAXUSERS);

        waiting4Clients(serverSocket);
    }

    private void waiting4Clients(ServerSocket serverSocket) {
        smt = new ServerMainThread(this, MAXUSERS, userList, true, messageArea, serverMainPool);
        executorPool.execute(smt);
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public int getAmountClients() {
        return this.userList.size();
    }

    public int getMaxUsers() {
        return this.MAXUSERS;
    }

    public void shutDown() throws IOException {
        logger.warn(Utilities.getLogTime() + " Shutdown intitiated...");
        smt.interrupt();
        executorPool.shutdownNow();
        serverMainPool.shutdownNow();
        serverSocket.close();
        logger.info(Utilities.getLogTime() + " Shutdown completed");
    }

    public List<Member> getUserList() {
        return this.userList;
    }

}
