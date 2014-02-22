/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.net.sam.server.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author janhorak
 */
@Entity
@Table(name = "Ca")
public class CA implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @OneToOne
    @MapsId
    private Member userID;
    
    @NotNull
    private String publicKey;

    public Member getUserID() {
        return userID;
    }

    public void setUserID(Member userID) {
        this.userID = userID;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

}
