/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.entities;

import java.io.Serializable;
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
        this.setTimestamp(new Date());
    }

    public Message(Handshake hs, MediaFile mf) {
        this.handshake = hs;
        this.setContent(hs.getContent());
        this.setOthers("Not in use (because Handshake)");
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new Date());
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
//        try {
////            Handshake cleanedUpHs = new Handshake(ms.getHandshake().getId(),
////                                                    );
//            cleanedUpHs.setId(ms.getHandshake().getId());
//            cleanedUpHs.setAnswer(ms.getHandshake().isAnswer());
//            cleanedUpHs.setReason(ms.getHandshake().getReason());
//            cleanedUpHs.setContent(ms.getHandshake().getContent());
//            cleanedUpHs.setStatus(ms.getHandshake().getStatus());
//            m.setHandshake(cleanedUpHs);
//        } catch (NotAHandshakeException ex) {
//            logger.error("Error in Message-Class: " + ex);
//        }
//
//        m.setContent(ms.getContent());
//        m.setMessageType(ms.getMessageType());
//        m.setOthers(ms.getOthers());
//        m.setContent(ms.getContent());
//        m.setReceiverId(ms.getReceiverId());
//        m.setSenderId(ms.getSenderId());
//        m.setTimestamp(ms.getTimestamp());

        return m;

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
        return this.getMessageType() == EnumKindOfMessage.HANDSHAKE
                && this.handshake != null;
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
        // @TODO: Use Stringbuilder for building returning String!

        if (this.getMessageType() == EnumKindOfMessage.HANDSHAKE) {
            return this.getTimestamp() + " Handshake: From " + this.getSenderId()+ " to " + this.getReceiverId() 
                    + " in Status: " + this.handshake.getStatus() + " for Request: " + this.handshake.getReason() + " "
                    + this.handshake.getContent();
        } else {
            return this.getTimestamp() + ": " + this.getSenderId() + " to " + this.getReceiverId() + " "
                    + this.getMessageType() + " "
                    + this.getContent() + " "
                    + this.getOthers();
        }

    }

}
