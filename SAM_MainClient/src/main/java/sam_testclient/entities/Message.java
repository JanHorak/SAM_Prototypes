/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.exceptions.NotAHandshakeException;

/**
 *
 * @author janhorak
 */
public class Message extends TransportObject implements Serializable {

    private static Logger logger = Logger.getLogger(Message.class);
    
    protected Message() {
    }

    public Message(int senderId, int receiverId, EnumKindOfMessage kind, String content, String others) {
        this.setContent(content);
        this.setMessageType(kind);
        this.setReceiverId(receiverId);
        this.setSenderId(senderId);
        this.setOthers(others);
        this.setTimestamp(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    }

    public Message(Handshake hs, MediaFile mf) {
        this.handshake = hs;
        this.setContent(hs.getContent());
        this.setOthers("Not in use (because Handshake)");
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    }


    private Handshake handshake;

    private MediaStorage mediaStorage;

    public Handshake getHandshake() throws NotAHandshakeException {
        if (isHandshake()) {
            return this.handshake;
        } else {
            throw new NotAHandshakeException("This Message is not a Handshake", "1000");
        }
    }

    public boolean isHandshake() {
        return this.getMessageType() == EnumKindOfMessage.HANDSHAKE;
    }
    
    public boolean hasFile(){
        return this.mediaStorage != null;
    }

    public void setHandshake(Handshake handshake) {
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.handshake = handshake;
    }

    public MediaStorage getMediaStorage() {
        return mediaStorage;
    }

    public void setMediaStorage(MediaStorage mediaStorage) {
        this.mediaStorage = mediaStorage;
    }
    
    public void interchangeSenderIDAndReceiverID(){
        int buffer = this.getSenderId();
        this.setSenderId(this.getReceiverId());
        this.setReceiverId(buffer);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTimestamp()).append(": ")
                .append(this.getSenderId())
                .append(" to ").append(this.getReceiverId())
                .append(" Type: ").append(this.getMessageType())
                .append(" Content: ").append(this.getContent())
                .append(" Others: ").append(this.getOthers())
                .append(" Handshake: ").append(this.isHandshake());
        if (this.isHandshake()) {
            Handshake hs = null;
            try {
                hs = this.getHandshake();
            } catch (NotAHandshakeException ex) {
                logger.error("Fatal" + ex);
            }
            sb.append(" Id: ").append(hs.getId())
                    .append(" Reason: ")
                    .append(hs.getReason())
                    .append(" Status: ").append(hs.getStatus())
                    .append(" Content: ").append(hs.getContent())
                    .append(" Owner: ").append(hs.getOwner());
        }
        return sb.toString();

    }

}
