/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import net.sam.entities.User;

/**
 *
 * @author janhorak
 */
public class Server extends ClientServerCommunicationBase{
    
    private ServerSocket serverSocket;
    
    private final int MAXUSERS = 5;
    
    private List<User> userList;
    
    private ServerMainThread smt;
    
    public Server(){
 	try {
            userList = new ArrayList<>(MAXUSERS);
 	    this.startUpServer();
  	} catch (IOException e) {
  	    e.printStackTrace();
  	} 
    }
    
    private void startUpServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.serverSocket.setSoTimeout(TIMEOUTTIMER);
        System.out.println("Port registered...");
        System.out.println("waiting for Client...");
        waiting4Clients(serverSocket);
    }
    
    public void waiting4Clients(ServerSocket serverSocket) {
 	smt = new ServerMainThread(this, MAXUSERS, userList, true);
        smt.start();
    }
    
    public ServerSocket getServerSocket(){
        return this.serverSocket;
    }
    
    public int getAmountClients(){
        return this.userList.size();
    }
    
    public int getMaxUsers(){
        return this.MAXUSERS;
    }
    
    public void shutDown() throws IOException{
        smt.kill();
    }
    

}
