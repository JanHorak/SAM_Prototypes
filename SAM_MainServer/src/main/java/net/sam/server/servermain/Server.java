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
import javax.swing.JTextArea;
import net.sam.server.entities.Member;



/**
 *
 * @author janhorak
 */
public class Server implements ClientServerCommunicationBase{
    
    private ServerSocket serverSocket;
    
    private final int MAXUSERS = 5;
    
    private List<Member> userList;
    
    private ServerMainThread smt;
    
    private JTextArea messageArea;
    
    public Server(JTextArea messageArea){
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
        this.serverSocket.setSoTimeout(TIMEOUTTIMER);
        System.out.println("Port registered...");
        System.out.println("waiting for Client...");
        waiting4Clients(serverSocket);
    }
    
    private void waiting4Clients(ServerSocket serverSocket) {
 	smt = new ServerMainThread(this, MAXUSERS, userList, true, messageArea);
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
    
    public List<Member> getUserList(){
        return this.userList;
    }
    

}
