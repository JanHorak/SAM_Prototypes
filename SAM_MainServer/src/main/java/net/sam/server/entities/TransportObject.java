/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.entities;

import java.util.Date;
import net.sam.server.enums.EnumKindOfMessage;

/**
 * Abstract TrasportObject class. 
 * This will be used for the simple transport and wrapping of JSONs
 * @author janhorak
 */
public abstract class TransportObject {
    
    private EnumKindOfMessage messageType;
    
    private String content;
    
    private int receiverId;
    
    private int senderId;
    
    private String others;
    
    private Date timestamp;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
    

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int recieverId) {
        this.receiverId = recieverId;
    }

    public EnumKindOfMessage getMessageType() {
        return messageType;
    }

    public void setMessageType(EnumKindOfMessage messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
}
