/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.client.clientmain;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author janhorak
 */
public class Client extends ClientServerCommunicationBase {

    private Socket serverSocket;
    
    public Socket getServerSocket(){
        return serverSocket;
    }

    public void connect() throws IOException {
        serverSocket = new Socket(IP, PORT);
    }
    
    public void disconnect() throws IOException{
        serverSocket.close();
    }

}
