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
import javax.swing.JTextArea;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.entities.Member;
import net.sam.server.utilities.Utilities;


/**
 * This is the Mainthread of the Server.
 * @author janhorak
 */
public class ServerMainThread extends Thread {


    ServerMainBean serverMainBean;

    private List<Member> userList;
    
    private int maxUsers;

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
        serverMainBean = ServerMainBean.getInstance();
        System.out.println(serverMainBean.getMaxUsers());
    }

    @Override
    public void run() {
        System.out.println(Utilities.getLogTime()+ " Thread startet...");
        socketList = new ArrayList<>();
        while (live) {
            if (userList.size() <= maxUsers) {
                socket = null;
                try {
                    System.out.println(Utilities.getLogTime()+ " Thread is waiting for Clients...");
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
        System.out.println(Utilities.getLogTime()+ " ServerThread stopped");
    }

    /**
     * This method clears the Serversettings and prepares for shutdown.
     * 
     * @throws IOException 
     */
    public void kill() throws IOException {
        for (Member u : userList) {
            u.getSocket().close();
            System.out.println(Utilities.getLogTime()+ " "+u.toString() + " is disconnected");
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
