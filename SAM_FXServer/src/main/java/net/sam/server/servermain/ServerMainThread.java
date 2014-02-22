/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.sam.server.servermain;


import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.net.sam.server.entities.Member;


/**
 * This is the Mainthread of the Server.
 * @author janhorak
 */
public class ServerMainThread extends Thread {

    private int maxUsers;

    private List<Member> userList;

    private List<SocketChannel> channelList;

    private boolean active;

    private Server server;

    private Socket socket;

    private boolean live = true;

    boolean stop = false;

    public ServerMainThread(Server server, int maxUsers, List<Member> userList, boolean active) {
        this.maxUsers = maxUsers;
        this.userList = userList;
        this.active = active;
        this.server = server;
    }

    @Override
    public void run() {
        int id = 0; //<- Has to be replaced for autogenerate from hibernate
        System.out.println("Thread startet...");

        while (live) {
            if (userList.size() <= maxUsers) {
                socket = null;
                try {
                    System.out.println("Thread is waiting for Clients...");
                    socket = server.getServerSocket().accept();
                    Member u = new Member();
                    u.setUserID(id);
                    u.setSocket(socket);
                    userList.add(u); 
                    //TODO: <- Do some Database- actions (adding etc)
                    System.out.println("Added: " + u.toString());
                    //TODO: <- Start of the Communicationthread here
                    id++;
                } catch (IOException ex) {
                    /*
                    The Problem is the Blocking- Method of the Serversocket.
                    The close()- Method of the Serversocket will 
                    throw an exception automaticly. We use this to cancel the
                    Thread.
                    */
                    live = false;
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ServerMainThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
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
