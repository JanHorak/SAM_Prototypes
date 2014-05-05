/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.entities;

import java.io.Serializable;
import java.util.Date;
import sam_testclient.enums.EnumKindOfMessage;

/**
 *
 * @author janhorak
 */

public class Message extends TransportObject implements Serializable{

    private Handshake handshake;
 
    private Long id;

    public Message(int senderId, int recieverId, EnumKindOfMessage kind, String content, String others) {
        this.setContent(content);
        this.setMessageType(kind);
        this.setReceiverId(recieverId);
        this.setSenderId(senderId);
        this.setOthers(others);
        this.setTimestamp(new Date());
    }

    public Message(Handshake hs) {
        this.handshake = hs;
        this.setReceiverId(hs.getReceiverID());
        this.setSenderId(hs.getSenderID());
        this.setContent(hs.getContent());
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new Date());
    }
    
    public Handshake getHandshake(){
        if (this.handshake != null){
            return this.handshake;
        } 
        return null;
    }
    
    public boolean isHandshake(){
        return this.handshake != null;
    }

    public void setHandshake(Handshake handshake) {
        this.handshake = handshake;
    }
    

    @Override
    public String toString() {
        // @TODO: Use Stringbuilder for building returning String!
        
        if (this.getMessageType() == EnumKindOfMessage.HANDSHAKE) {
            return this.getTimestamp() + " Handshake: " + this.handshake.getSenderID() + " to " + this.handshake.getReceiverID() + 
                    " in Status: "+ this.handshake.getStatus() + " for Request: " + this.handshake.getReason() + " "
                    + this.handshake.getContent();
        } else {
            return this.getTimestamp() + ": " + this.getSenderId() + " to " + this.getReceiverId() + " "
                    + this.getMessageType() + " "
                    + this.getContent() + " "
                    + this.getOthers();
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
