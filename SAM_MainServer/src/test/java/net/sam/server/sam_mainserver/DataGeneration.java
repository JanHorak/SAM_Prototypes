/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.sam_mainserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.sam.server.entities.MediaFile;
import net.sam.server.entities.Member;
import net.sam.server.entities.Message;
import net.sam.server.entities.ServerConfig;
import net.sam.server.enums.EnumKindOfMessage;
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
        Member m3 = new Member();

        m1.setName("Test1");
        m1.setPassword("sss");
        m1.setLastTimeOnline(new Date());
        m1.setActive(true);
        
        m2.setName("Test2");
        m2.setPassword("sss");
        m2.setLastTimeOnline(new Date());
        m2.setActive(true);
        
        m3.setName("Test3");
        m3.setPassword("sss");
        m3.setLastTimeOnline(new Date());
        m3.setActive(true);
        
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void shouldAddAnImageInMediaStorage(){
        em.getTransaction().begin();
        File image = new File("resources/graphics/AndroidLogo.png");
        
        Message m = new Message(1, 3, EnumKindOfMessage.LOGIN, "Blaaa", "additional bla");
        
        MediaFile mt = new MediaFile();
        mt.setFileName("AndroidLogo.png");
        mt.setDescription("Logo from Android");
        mt.setType(EnumMediaType.IMAGE);
        mt.setMessage(m);
        try {
            mt.setContent(Files.readAllBytes(image.toPath()));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DataGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.persist(mt);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void shouldAddTheDefaulConfiguration(){
        em.getTransaction().begin();
        ServerConfig sc = new ServerConfig();
        sc.setName("default*");
        sc.setActive(true);
        sc.setDeleteable(false);
        sc.setIpaddress("localhost");
        sc.setMaximalUsers(25);
        sc.setPort(2222);
        em.persist(sc);
        em.getTransaction().commit();
    }
    
    @Test
    public void postDelete(){
        em.getTransaction().begin();
        List<MediaFile> mfList = em.createQuery("SELECT m FROM MediaFile m").getResultList();
        for (MediaFile mf : mfList){
            em.remove(mf);
        }
        em.getTransaction().commit();
    }
    
    @After
    public void shutDown(){
        em.close();
    }
    

}
