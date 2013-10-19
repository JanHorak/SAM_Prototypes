/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.valproto.entities;

import javax.validation.constraints.NotNull;
import net.sam.valproto.validation.Money;
import net.sam.valproto.validation.Suffix;

/**
 *
 * @author janhorak
 */
public class User {
    
    @NotNull
    private String name;
    
    @Suffix(suffix = "@sam.net")
    @NotNull
    private String eMail;
    
    @NotNull
    private String password;
    
    @Money
    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    
    
}
