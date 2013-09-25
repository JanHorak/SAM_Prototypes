/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.jpaprototype.entities;



import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author janhorak
 */

@Entity
@Table(name = "member")
public class Member implements Serializable{
    @Id
    private Long id;
    
    @Column(name = "membername")
    private String name;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
