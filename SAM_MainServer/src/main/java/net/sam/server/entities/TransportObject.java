/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import net.sam.server.enums.EnumKindOfMessage;

/**
 * Abstract TrasportObject class. This will be used for the simple transport and
 * wrapping of JSONs
 *
 * @author janhorak
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TransportObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EnumKindOfMessage messageType;

    private String content;

    private int receiverId;

    private int senderId;

    private String others;

    //@TODO: @Temporal- annotation for timestamp adden
    //       maybe changes in the testdata- adden
    private Date timestamp;

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

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
