/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.entities;

import net.sam.server.enums.EnumKindOfMessage;

/**
 * Abstract TrasportObject class. 
 * This will be used for the simple transport and wrapping of JSONs
 * @author janhorak
 */
public abstract class TransportObject {
    
    private EnumKindOfMessage messageType;
    
    private String content;

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
    
    
}
