/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.sam_mainserver;

import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import net.sam.server.entities.Member;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertTrue(results.size() == 5);
        assertTrue(results.get(0).getName() != null);
    }
    
    @Test
    public void shouldReturnMemberById(){
        final int id = 2;
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
