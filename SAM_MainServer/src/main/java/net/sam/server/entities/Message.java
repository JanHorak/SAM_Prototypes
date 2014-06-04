/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.exceptions.NotAHandshakeException;
import net.sam.server.servermain.ServerMainThread;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
@Entity
public class Message extends TransportObject implements Serializable {

    private static Logger logger = Logger.getLogger(ServerMainThread.class);

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
        this.setOthers("");
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    }

    /**
     * This method is cleaning up the message which comes from the database.
     * Because the returning object is modified by the persistence provider
     * which adds some additional informations it is not possible to map the
     * plain returning object to the used JSON- Format.
     *
     * This method creates a new {@link Handshake} Object, copies the important
     * values and returns a new clean {@link Message} Object.
     *
     * @param ms
     * @return Cleaned up Message
     */
    public static Message cleanUpHandshake(Message ms) {
        Message m = new Message();
        try {
            Handshake cleanedUpHs = new Handshake();
            cleanedUpHs.setId(ms.getHandshake().getId());
            cleanedUpHs.setAnswer(ms.getHandshake().isAnswer());
            cleanedUpHs.setReason(ms.getHandshake().getReason());
            cleanedUpHs.setContent(ms.getHandshake().getContent());
            cleanedUpHs.setStatus(ms.getHandshake().getStatus());
            m.setHandshake(cleanedUpHs);
        } catch (NotAHandshakeException ex) {
            logger.error("Error in Message-Class: " + ex);
        }

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
