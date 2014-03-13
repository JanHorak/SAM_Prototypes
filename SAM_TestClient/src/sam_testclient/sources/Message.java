/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.entities;

import net.sam.server.enums.EnumKindOfMessage;

/**
 *
 * @author janhorak
 */
public class Message extends TransportObject{
    
    public Message (EnumKindOfMessage kind, String content){
        this.setContent(content);
        this.setMessageType(kind);
    }
    
    @Override
    public String toString(){
        return this.getMessageType() + " " + this.getContent();
    }
}
