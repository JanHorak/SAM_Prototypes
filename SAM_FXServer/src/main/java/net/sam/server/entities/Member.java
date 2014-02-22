package main.java.net.sam.server.entities;

import java.io.Serializable;
import java.net.Socket;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author janhorak
 */
@Entity
@Table(name = "Member")
public class Member implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int memberID;
    
    @Transient
    private Socket socket;

    @NotNull
    private String name;
    
    @NotNull
    private String password;
    
    @NotNull
    private boolean active;

    public int getUserID() {
        return memberID;
    }

    public void setUserID(int userID) {
        this.memberID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectData\n");
        sb.append("ID: " +this.memberID+"\n");
        sb.append("Name: " + this.name+"\n");
        sb.append("Active: " + this.active+"\n");
        sb.append("Socket: " + this.socket.toString());
        return sb.toString();
    }
    
    
    
}
