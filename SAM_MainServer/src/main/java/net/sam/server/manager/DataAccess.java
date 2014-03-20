package net.sam.server.manager;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import net.sam.server.entities.Member;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;



/**
 *
 * @author janhorak
 */
public abstract class DataAccess {

    private static EntityManager em;
    private static EntityManagerFactory emf;
    private static Logger logger;

    private static void setUp() {
        logger = Logger.getLogger(DataAccess.class);
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    private static void shutDown() {
        em.close();
        emf.close();
    }

    public static boolean registerUser(Member member) {
        setUp();
        Member resultMember = new Member();
        Query q = em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class).setParameter("name", member.getName());
        try {
            resultMember = (Member) q.getSingleResult();
            logger.warn(Utilities.getLogTime()+ " Registration failed: User already registered!");
            return false;
        } catch (NoResultException ex) {
            saveUser(member, em);
            return true;
        } finally {
            shutDown();
        }

    }

    private static void saveUser(Member member, EntityManager em) {
        em.persist(member);
        em.getTransaction().commit();
        logger.info(Utilities.getLogTime()+" New Member registered");
    }

    public static boolean login(String incomingPW) {
        setUp();
        String pw = new String();
        Query q = em.createQuery("SELECT m.password FROM Member m WHERE m.password = :pw", String.class).setParameter("pw", incomingPW);
        try {
            pw = (String) q.getSingleResult();
            System.out.println(pw);
            if (incomingPW.equals(pw)) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            System.err.println("No User founded!");
            return false;
        } finally {
            shutDown();
        }

    }
    
    public static Member getMemberById(int id){
        setUp();
        Query q = em.createNamedQuery("Member.findByID").setParameter("id", id);
        shutDown();
        return (Member) q.getSingleResult();
    }
    
    public static Member getMemberByName(String name){
        setUp();
        Query q = em.createNamedQuery("Member.findByName").setParameter("name", name);
        shutDown();
        return (Member) q.getSingleResult();
    }
    
    public static List<Member> getAllRegisteredMembers(){
        setUp();
        Query q = em.createNamedQuery("Member.findAll");
        shutDown();
        return (List<Member>) q.getResultList();
    }
}
