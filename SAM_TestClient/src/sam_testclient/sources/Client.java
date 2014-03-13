/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
    
    public void writeMessage(String message) throws IOException {
 	 PrintWriter printWriter =
 	    new PrintWriter(
 	 	new OutputStreamWriter(
 		    serverSocket.getOutputStream()));
 	printWriter.print(message);
 	printWriter.flush();
    }

}
