/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.entities;

import java.io.Serializable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import sam_testclient.enums.EnumKindOfMessage;


/**
 * Abstract TrasportObject class. This will be used for the simple transport and
 * wrapping of JSONs
 *
 * @author janhorak
 */
public abstract class TransportObject implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EnumKindOfMessage messageType;

    @NotNull
    private String content;

    @NotNull
    private int receiverId;

    @NotNull
    private int senderId;

    @NotNull
    private String others;

    @NotNull
    private String timestamp;

    public int getSenderId() {
        return this.senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getOthers() {
        return this.others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public int getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(int recieverId) {
        this.receiverId = recieverId;
    }

    public EnumKindOfMessage getMessageType() {
        return this.messageType;
    }

    public void setMessageType(EnumKindOfMessage messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
