package net.sam.server.manager;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import net.sam.server.entities.Member;
import net.sam.server.entities.Message;
import net.sam.server.entities.MessageBuffer;
import net.sam.server.utilities.Utilities;
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

    public static void registerUser(Member member) {
        setUp();
        Member resultMember = new Member();
        Query q = em.createQuery("SELECT m FROM Member m WHERE m.name = :name", Member.class).setParameter("name", member.getName());
        boolean isAlreadyRegistered = false;
        try {
            resultMember = (Member) q.getSingleResult();
            logger.warn(Utilities.getLogTime()+ " Registration failed: User already registered!");
            isAlreadyRegistered = true;

        } catch (NoResultException ex) {
            
        }
        shutDown();
        if ( !isAlreadyRegistered ){
            saveUser(member);
            logger.info(Utilities.getLogTime()+ " User registered!");
        }

    }

    private static void saveUser(Member member) {
        setUp();
        em.persist(member);
        em.getTransaction().commit();
        shutDown();
        logger.info(Utilities.getLogTime()+" New Member registered");
    }

    /**
     * Login method.
     * 
     * @param m {@link Member}
     * @return 
     */
    public static boolean login(Member m) {
        setUp();
        String pw = new String();
        Query q = em.createQuery("SELECT m.password FROM Member m WHERE m.name = :name", String.class).setParameter("name", m.getName());
        try {
            pw = (String) q.getSingleResult();
            System.out.println(pw);
            if (m.getPassword().equals(pw)) {
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
    
    /**
     * Saves a passed Message in the MessageBuffer
     * 
     * @param m ({@link Message})
     */
    public static void saveMessageInBuffer(Message m){
        setUp();
        MessageBuffer mb = new MessageBuffer();
        mb.setMessage(m.getContent());
        mb.setReceiverId(m.getReceiverId());
        mb.setSenderId(m.getSenderId());
        em.persist(mb);
        em.getTransaction().commit();
        shutDown();
        logger.info(Utilities.getLogTime() + " Message is saved in Buffer");
    }
    
    /**
     * Returns a specific Member by Id.
     * 
     * @param id
     * @return single {@link Member}
     */
    public static Member getMemberById(int id){
        setUp();
        Query q = em.createNamedQuery("Member.findByID").setParameter("id", id);
        try {
           return (Member) q.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("No User is founded!");
            return null;
        } finally {
            shutDown();
        }
        
    }
    
    /**
     * Return a single Member by Name
     * 
     * @param name
     * @return single {@link Member}
     */
    public static Member getMemberByName(String name){
        setUp();
        Query q = em.createNamedQuery("Member.findByName").setParameter("name", name);
        try {
           return (Member) q.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("No User is founded!");
            return null;
        } finally {
            shutDown();
        }
    }
    
    /**
     * Returns all registered Members.
     * 
     * @return List of {@link Member} they are registered
     */
    public static List<Member> getAllRegisteredMembers(){
        setUp();
        Query q = em.createNamedQuery("Member.findAll");
        try {
           return (List<Member>) q.getResultList();
        } catch (NoResultException ex) {
            System.err.println("No Users are founded!");
            return null;
        } finally {
            shutDown();
        }
    }
    
    /**
     * Returns all messages from the {@link MessageBuffer} by <code>receiverid</code>.
     * 
     * @param receiverId
     * @return List of all Messages in the Buffer (Type: {@link MessageBuffer})
     */
    public static List<MessageBuffer> getAllMessagesFromBuffer(int receiverId){
        setUp();
        Query q = em.createNamedQuery("MessageBuffer.findMessagesAllFromSenderToReceiver").setParameter("rId", receiverId);
        try {
           return (List<MessageBuffer>) q.getResultList();
        } catch (NoResultException ex) {
            System.err.println("No Messages are found!");
            return null;
        } finally {
            shutDown();
        }
    }
    
    /**
     * Delete all messages in the messageBuffer by Id.
     * 
     * @param receiverId 
     */
    public static void dropMessagesFromBuffer(int receiverId){
        setUp();
        Query deleteQuery = em.createQuery("DELETE FROM MessageBuffer m WHERE m.receiverId = :id");
        int deletedRecords = deleteQuery.setParameter("id", receiverId).executeUpdate();
        logger.info(deletedRecords +" Messages are deleted from buffer!");
        em.getTransaction().commit();
        shutDown();
    }
}
