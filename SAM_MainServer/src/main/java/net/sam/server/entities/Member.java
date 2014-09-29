package net.sam.server.entities;

import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author janhorak
 */
@Entity
@Table(name = "Member")
@NamedQueries({
    @NamedQuery(name = "Member.findAll", query = "SELECT m FROM Member m"),
    @NamedQuery(name = "Member.findByID", query = "SELECT m FROM Member m WHERE m.memberID = :id"),
    @NamedQuery(name = "Member.findByName", query = "SELECT m FROM Member m WHERE m.name = :name"),
    @NamedQuery(name = "Member.getPasswordByName", query = "SELECT m.password FROM Member m WHERE m.name = :name")
})
public class Member implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int memberID;
    
    @Transient
    private Socket socket;

    @NotNull
    @Column(name = "name")
    private String name;
    
    @NotNull
    @Column(name = "pw")
    private String password;
    
    @NotNull
    private boolean active;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTimeOnline;

    @Transient
    private MemberSettings memberSettings;

    public MemberSettings getMemberSettings() {
        return memberSettings;
    }
    
    public Date getLastTimeOnline() {
        return lastTimeOnline;
    }

    public void setLastTimeOnline(Date lastTimeOnline) {
        this.lastTimeOnline = lastTimeOnline;
    }

    public void setMemberSettings(MemberSettings memberSettings) {
        this.memberSettings = memberSettings;
    }

    public int getUserID() {
        return memberID;
    }

    public void setUserID(int userID) {
        this.memberID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ObjectData\n");
        sb.append("ID: " +this.memberID+"\n");
        sb.append("Name: " + this.name+"\n");
        sb.append("Active: " + this.active+"\n");
        return sb.toString();
    }
}
