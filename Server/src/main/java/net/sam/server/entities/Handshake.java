/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import net.sam.server.enums.EnumHandshakeReason;
import net.sam.server.enums.EnumHandshakeStatus;

/**
 * This class is used for the {@code Handshake} of Clients or between Client and
 * Server. The {@link #status} controls what are the next steps for the
 * handshake. For example if the Server should save the request for a sent file
 * in the database.
 *
 * @author janhorak
 */
@Entity
public class Handshake implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EnumHandshakeStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EnumHandshakeReason reason;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "handshake")
    private Message owner;

    @NotNull
    private boolean answer;

    @NotNull
    private String content;

    public EnumHandshakeStatus getStatus() {
        return status;
    }

    public void setStatus(EnumHandshakeStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EnumHandshakeReason getReason() {
        return reason;
    }

    public void setReason(EnumHandshakeReason reason) {
        this.reason = reason;
    }

    /**
     * Defines the answer of the handshake. For example answer will be false if
     * the client denies a buddy-request
     *
     * @return - answer
     */
    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id + " " + this.reason + " "
                + this.status + " " + this.answer + " " + this.content;
    }

    public void setOwner(Message owner) {
        this.owner = owner;
    }

    public Message getOwner() {
        return this.owner;
    }
}
