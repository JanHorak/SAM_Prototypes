/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.servermain;


import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import net.sam.server.entities.Member;


/**
 * This is the Mainthread of the Server.
 * @author janhorak
 */
public class ServerMainThread extends Thread {

    private int maxUsers;

    private List<Member> userList;

    private List<SocketChannel> channelList;

    private List<Socket> socketList;
    private boolean active;

    private Server server;

    private Socket socket;

    private boolean live = true;

    boolean stop = false;
    
    private JTextArea area;

    public ServerMainThread(Server server, int maxUsers, List<Member> userList, boolean active, JTextArea area) {
        this.maxUsers = maxUsers;
        this.userList = userList;
        this.active = active;
        this.server = server;
        this.area = area;
    }

    @Override
    public void run() {
        System.out.println("Thread startet...");
        socketList = new ArrayList<>();
        while (live) {
            if (userList.size() <= maxUsers) {
                socket = null;
                try {
                    System.out.println("Thread is waiting for Clients...");
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
                    live = false;
                }
            }
        }
        System.out.println("ServerThread stopped");
    }

    /**
     * This method clears the Serversettings and prepares for shutdown.
     * 
     * @throws IOException 
     */
    public void kill() throws IOException {
        for (Member u : userList) {
            u.getSocket().close();
            System.out.println(u.toString() + " is disconnected");
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
