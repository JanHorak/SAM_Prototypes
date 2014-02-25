/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.sam_mainserver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.sam.server.entities.Member;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;

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
        m4.setActive(true);
        
        m5.setName("Clark Kämmt");
        m5.setPassword("aaa");
        m5.setActive(true);
        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);
        em.persist(m5);
        em.getTransaction().commit();
        em.close();
    }
    

}
