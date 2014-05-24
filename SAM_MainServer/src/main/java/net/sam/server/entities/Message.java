/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import net.sam.server.enums.EnumKindOfMessage;

/**
 *
 * @author janhorak
 */

@Entity
@net.sam.server.validation.Message
public class Message extends TransportObject implements Serializable{

    protected Message() {}
    
    public Message(int senderId, int receiverId, EnumKindOfMessage kind, String content, String others) {
        this.setContent(content);
        this.setMessageType(kind);
        this.setReceiverId(receiverId);
        this.setSenderId(senderId);
        this.setOthers(others);
        this.setTimestamp(new Date());
    }

    public Message(Handshake hs) {
        this.handshake = hs;
        this.setReceiverId(hs.getReceiverID());
        this.setSenderId(hs.getSenderID());
        this.setContent(hs.getContent());
        this.setOthers("Not in use (because Handshake)");
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new Date());
    }
    
    public Message (Handshake hs, MediaFile mf){
        this.handshake = hs;
        this.setReceiverId(hs.getReceiverID());
        this.setSenderId(hs.getSenderID());
        this.setContent(hs.getContent());
        this.setOthers("Not in use (because Handshake)");
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new Date());
    }
    
    /**
     * This method is cleaning up the message which comes from the database.
     * Because the returning object is modified by the persistence provider
     * which adds some additional informations it is not possible to map
     * the plain returning object to the used JSON- Format.
     * 
     * This method creates a new {@link Handshake} Object, copies the 
     * important values and returns a new clean {@link Message} Object.
     * @param ms
     * @return Cleaned up Message
     */
    public static Message cleanUpHandshake(Message ms){
        Handshake cleanedUpHs = new Handshake();
        cleanedUpHs.setId(ms.getHandshake().getId());
        cleanedUpHs.setAnswer(ms.getHandshake().isAnswer());
        cleanedUpHs.setReason(ms.getHandshake().getReason());
        cleanedUpHs.setSenderID(ms.getHandshake().getSenderID());
        cleanedUpHs.setReceiverID(ms.getHandshake().getReceiverID());
        cleanedUpHs.setContent(ms.getHandshake().getContent());
        cleanedUpHs.setStatus(ms.getHandshake().getStatus());
        
        Message m = new Message();
        m.setHandshake(cleanedUpHs);
        m.setContent(ms.getContent());
        m.setMessageType(ms.getMessageType());
        m.setOthers(ms.getOthers());
        m.setContent(ms.getContent());
        m.setReceiverId(ms.getReceiverId());
        m.setSenderId(ms.getSenderId());
        m.setTimestamp(ms.getTimestamp());
        
        return m;
    }
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Handshake handshake;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MediaStorage mediaStorage;
    
    public Handshake getHandshake(){
        if (isHandshake()){
            return this.handshake;
        } 
        return null;
    }
    
    public boolean isHandshake(){
        return this.getMessageType() == EnumKindOfMessage.HANDSHAKE;
    }

    public void setHandshake(Handshake handshake) {
        this.handshake = handshake;
    }

    public MediaStorage getMediaStorage() {
        return mediaStorage;
    }

    public void setMediaStorage(MediaStorage mediaStorage) {
        this.mediaStorage = mediaStorage;
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
    
}
