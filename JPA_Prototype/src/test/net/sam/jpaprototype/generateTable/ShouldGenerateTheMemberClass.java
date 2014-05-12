/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.Test;
import net.sam.jpaprototype.entities.*;

/**
 *
 * @author janhorak
 */
public class ShouldGenerateTheMemberClass {

    @Test
    public void test() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();

        entityManager.getTransaction().begin();

        Member m = new Member();

        entityManager.persist(m);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}