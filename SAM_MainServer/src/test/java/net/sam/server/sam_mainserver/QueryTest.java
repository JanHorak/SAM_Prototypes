/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.sam_mainserver;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import net.sam.server.entities.Member;
import net.sam.server.manager.DataAccess;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
public class QueryTest {

    EntityManager em;

    /*
    please see reference implementation for HibernateQueryLanguage (HQL) for
    more examples
    http://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html#queryhql-select
    */
    
    @Before
    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
    }

    @Test
    public void shouldReturnAllUsers() {
        em.getTransaction().begin();
        TypedQuery<Member> query = em.createNamedQuery("Member.findAll", Member.class);
        List<Member> results = query.getResultList();
        assertTrue(results.size() <= 5);
        assertTrue(results.get(0).getName() != null);
    }
    
    @Test
    public void shouldChangeActiveTimeFromUser(){
        em.getTransaction().begin();
        Member m = new Member();
        Query q = em.createNamedQuery("Member.findByID").setParameter("id", 3);
        try {
            m = (Member) q.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("ERROR");
        }
        m.setLastTimeOnline(new Date());
        em.persist(m);
        em.getTransaction().commit();
    }
    
    @Test
    public void shouldReturnMemberById(){
        final int id = 3;
        em.getTransaction().begin();
        TypedQuery<Member> query = em.createNamedQuery("Member.findByID", Member.class).setParameter("id", id);
        List<Member> results = query.getResultList();
        assertTrue(results.size() == 1);
        assertTrue(results.get(0).getName() != null);
    }
    
    @After
    public void cleanup() {
        em.close();
    }
}
