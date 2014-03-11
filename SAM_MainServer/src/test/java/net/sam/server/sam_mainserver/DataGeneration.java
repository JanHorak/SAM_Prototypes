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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.slf4j.Logger;

/**
 *
 * @author janhorak
 */
@Ignore
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
        Member m3 = new Member();
        Member m4 = new Member();
        Member m5 = new Member();
        
        m1.setName("Darth Vader");
        m1.setPassword("aaa");
        m1.setActive(true);
        
        m2.setName("Homer Simpson");
        m2.setPassword("bbb");
        m2.setActive(true);
        
        m3.setName("Ralf Maier");
        m3.setPassword("ccc");
        m3.setActive(true);
        
        m4.setName("Vladimir Vampir");
        m4.setPassword("ddd");
        m4.setActive(false);
        
        m5.setName("Clark Kämmt");
        m5.setPassword("aaa");
        m5.setActive(true);
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);
        em.persist(m5);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void shouldAddAnImageForMediaStorage(){
        em.getTransaction().begin();
        File image = new File("src/main/resources/graphics/AndroidLogo.png");
        
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
