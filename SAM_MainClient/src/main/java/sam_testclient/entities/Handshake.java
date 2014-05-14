/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import sam_testclient.enums.EnumHandshakeReason;
import sam_testclient.enums.EnumHandshakeStatus;




/**
 * This class is used for the {@code Handshake} of Clients
 * or between Client and Server.
 * The {@link #status} controls what are the next steps for the handshake. 
 * For example if the Server should save the request for a sent file in the
 * database.
 * 
 * @author janhorak
 */
@Entity
public class Handshake implements Serializable{

    private int senderID;
    
    private int receiverID;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Enumerated(EnumType.STRING)
    private EnumHandshakeStatus status;
    
    @Enumerated(EnumType.STRING)
    private EnumHandshakeReason reason;
    
    private boolean answer;
    
    private String content;

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public EnumHandshakeStatus getStatus() {
        return status;
    }

    public void setStatus(EnumHandshakeStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EnumHandshakeReason getReason() {
        return reason;
    }

    public void setReason(EnumHandshakeReason reason) {
        this.reason = reason;
    }

    /**
     * Defines the answer of the handshake.
     * For example answer will be false if the client denies a buddy-request
     * @return - answer
     */
    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        return this.id + " " + this.reason + " " + this.senderID + " " + this.receiverID + " "+
                this.status + " " +this.answer+" "+ this.content;
    }
    
    
    
} 

