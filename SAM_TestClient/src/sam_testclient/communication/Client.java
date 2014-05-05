/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.communication;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author janhorak
 */
public class Client extends ClientServerCommunicationBase {

    private Socket serverSocket;

    private int ownID = 99999;

    private Map<Integer, String> buddyList;

    public Map<Integer, String> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(Map<Integer, String> buddyList) {
        this.buddyList = buddyList;
    }

    public synchronized int getId() {
        return this.ownID;
    }

    public synchronized void setId(int id) {
        this.ownID = id;
    }

    public Socket getServerSocket() {
        return serverSocket;
    }

    public void connect() throws IOException {
        serverSocket = new Socket(IP, PORT);
    }

    public void writeMessage(String message) throws IOException {
        PrintWriter printWriter
                = new PrintWriter(
                        new OutputStreamWriter(
                                serverSocket.getOutputStream()));
        printWriter.print(message);
        printWriter.flush();
    }

    public int getIdFromBuddy(String name) {
        int id = 0;
        for (Map.Entry entry : this.buddyList.entrySet()) {
            if (name.equals(entry.getValue())) {
                id = (int) entry.getKey();
                break;
            }
        }
        return id;
    }
}
