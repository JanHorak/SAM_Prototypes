/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.apache.log4j.Logger;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.enums.EnumMessageStatus;
import sam_testclient.exceptions.NotAHandshakeException;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Message.findById", query = "SELECT m FROM Message m WHERE m.id = :id"),
    @NamedQuery(name = "Message.findConversation", query = "SELECT m FROM Message m WHERE m.senderId = :sId AND m.receiverId = :ownId"),
    @NamedQuery(name = "Message.findConversationWhereBuddyIsInvolved", query = "SELECT m FROM Message m WHERE m.senderId = :sId OR m.receiverId = :sId")
})
public class Message extends TransportObject implements Serializable {

    private static Logger logger = Logger.getLogger(Message.class);

    protected Message() {
    }

    public Message(int senderId, int receiverId, EnumKindOfMessage kind, String content, String others) {
        this.setId(Utilities.generateRandomUUID().toString());
        this.setMessageStatus(EnumMessageStatus.NA);
        this.setContent(content);
        this.setMessageType(kind);
        this.setReceiverId(receiverId);
        this.setSenderId(senderId);
        this.setOthers(others);
        this.setTimestamp(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    }

    public Message(Handshake hs) {
        this.handshake = hs;
        this.setContent(hs.getContent());
        this.setOthers("Not in use (because Handshake)");
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.setTimestamp(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    }

    @Enumerated(EnumType.STRING)
    private EnumMessageStatus messageStatus;

    private Handshake handshake;
    
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

    public EnumMessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(EnumMessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public void setHandshake(Handshake handshake) {
        this.setMessageType(EnumKindOfMessage.HANDSHAKE);
        this.handshake = handshake;
    }

    public void interchangeSenderIDAndReceiverID() {
        int buffer = this.getSenderId();
        this.setSenderId(this.getReceiverId());
        this.setReceiverId(buffer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.getId())
                .append(" @ ")
                .append(this.getTimestamp()).append(": ")
                .append(this.getSenderId())
                .append(" to ").append(this.getReceiverId())
                .append(" Type: ").append(this.getMessageType())
                .append(" Content: ").append(this.getContent())
                .append(" Others: ").append(this.getOthers())
                .append(" Handshake: ").append(this.isHandshake())
                .append(" Status: ").append(this.getMessageStatus().toString());
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

    // Java 8 Impr!!
    public static class CompareMessageByTimeStampHelper implements Comparator<String> {

        private Map<String, Message> messageMap;

        private CompareMessageByTimeStampHelper(HashMap<String, Message> messageMap) {
            this.messageMap = messageMap;
        }
        
        public CompareMessageByTimeStampHelper(){
            
        }

        @Override
        public int compare(String o1, String o2) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            String t1 = messageMap.get(o1).getTimestamp();
            String t2 = messageMap.get(o2).getTimestamp();

            try {
                if (sdf.parse(t1).after(sdf.parse(t2))) {
                    return 1;
                } else {
                    return -1;
                }
            } catch (ParseException ex) {
                java.util.logging.Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }

        public TreeMap<String, Message> sortByValue(HashMap<String, Message> map) {
            Message.CompareMessageByTimeStampHelper vc = new Message.CompareMessageByTimeStampHelper(map);
            TreeMap<String, Message> sortedMap = new TreeMap<String, Message>(vc);
            sortedMap.putAll(map);
            return sortedMap;
        }

    }

}
