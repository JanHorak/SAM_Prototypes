/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author janhorak
 * Abstract Class for Client- Server Communication
 */
public abstract class ClientServerCommunicationBase {
    
    public static final String IP = "127.0.0.1";
    
    public static final String HOST = "localhost";
    
    public static final int PORT = 2222;
    
    public final int TIMEOUTTIMER = 60000;
    
    private final int BUFFERSIZE = 250;
    
    public void writeMessage(Socket receiverSocket, String message) throws IOException {
 	 PrintWriter printWriter =
 	    new PrintWriter(
 	 	new OutputStreamWriter(
 		    receiverSocket.getOutputStream()));
 	printWriter.print(message);
 	printWriter.flush();
    }
    
    public String readMessage(Socket socketSended) throws IOException {
 	BufferedReader bufferedReader =
 	    new BufferedReader(
 		new InputStreamReader(
 	  	    socketSended.getInputStream()));
 	char[] buffer = new char[BUFFERSIZE];
 	int amountTokens = bufferedReader.read(buffer, 0, BUFFERSIZE);
 	String message = new String(buffer, 0, amountTokens);
 	return message;
    }
}
