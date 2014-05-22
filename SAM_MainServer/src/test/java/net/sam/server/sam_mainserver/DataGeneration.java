/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.sam_mainserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.sam.server.entities.MediaStorage;
import net.sam.server.entities.Member;
import net.sam.server.enums.EnumMediaType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
public class DataGeneration {
    
    EntityManager em;
    
    @Before 
    public void init(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
    }
    
    @Test
    public void shouldAddSomeMembers(){
        em.getTransaction().begin();
        Member m1 = new Member();
        Member m2 = new Member();

        m1.setName("Test1");
        m1.setPassword("sss");
        m1.setActive(true);
        
        m2.setName("Test2");
        m2.setPassword("sss");
        m2.setActive(true);
        
        em.persist(m1);
        em.persist(m2);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void shouldAddAnImageForMediaStorage(){
        em.getTransaction().begin();
        File image = new File("graphics/AndroidLogo.png");
        
        Member m = new Member();
        m.setUserID(2);
        
        MediaStorage ms = new MediaStorage();
        ms.setFileName("AndroidLogo.png");
        ms.setDescription("Logo from Android");
        ms.setType(EnumMediaType.IMAGE);
        ms.setMemberId(m);
        try {
            ms.setContent(Files.readAllBytes(image.toPath()));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DataGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(ms);
        em.getTransaction().commit();
        
    }
    
    @After
    public void shutDown(){
        em.close();
    }
    

}
