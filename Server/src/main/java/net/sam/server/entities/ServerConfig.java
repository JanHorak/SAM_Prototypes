/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import net.sam.server.validations.IPAddress;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Jan
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "ServerConfig.findAll", query = "SELECT sc FROM ServerConfig sc"),
    @NamedQuery(name = "ServerConfig.findByName", query = "SELECT sc FROM ServerConfig sc WHERE sc.name = :name"),
    @NamedQuery(name = "ServerConfig.findActive", query = "SELECT sc FROM ServerConfig sc WHERE sc.active = true"),
    @NamedQuery(name = "ServerConfig.findDefault", query = "SELECT sc FROM ServerConfig sc WHERE sc.deleteable = false")
})
public class ServerConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private boolean active;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private boolean deleteable;

    @NotNull
    @IPAddress
    private String ipaddress;

    @NotNull
    @Min(value = 1)
    @Max(value = 65535)
    private Integer port;

    @NotNull
    @Min(value = 1)
    @Max(value = 25)
    private Integer maximalUsers;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleteable() {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getMaximalUsers() {
        return maximalUsers;
    }

    public void setMaximalUsers(Integer maximalUsers) {
        this.maximalUsers = maximalUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
