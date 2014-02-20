package net.sam.server.entities;

import java.net.Socket;

/**
 *
 * @author janhorak
 */
public class User {
    
    private int id;
    
    private Socket socket;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public String toString(){
        return "ID: "+this.id + " @ " + this.socket.toString();
    }
    
    
    
}
