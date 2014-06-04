/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
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
public class Handshake implements Serializable{
    
    public Handshake(){
        
    }
    
    public Handshake(long id, EnumHandshakeStatus status, EnumHandshakeReason reason,
            boolean answer, String content){
        this.id = id;
        this.status = status;
        this.reason = reason;
        this.answer = answer;
        this.content = content;
    }
    
    private long id;
    
    @NotNull
    private EnumHandshakeStatus status;
    
    @NotNull
    private EnumHandshakeReason reason;
    
    @NotNull
    private boolean answer;
    
    private Message owner;
    
    @NotNull
    private String content;

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
        return this.id + " " + this.reason + " " +
                this.status + " " +this.answer+" "+ this.content;
    }

    public Message getOwner() {
        return owner;
    }

    public void setOwner(Message owner) {
        this.owner = owner;
    }
} 

