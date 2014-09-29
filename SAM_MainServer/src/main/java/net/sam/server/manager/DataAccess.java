package net.sam.server.manager;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import net.sam.server.entities.Member;
import net.sam.server.entities.Message;
import net.sam.server.entities.MessageBuffer;
import net.sam.server.entities.ServerConfig;
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
    
    
    // Todo: em.merge?!
    public static void updateServerConfig(ServerConfig old, ServerConfig newOne){
        setUp();
        
        ServerConfig sc_old = (ServerConfig) em.createNamedQuery("ServerConfig.findByName").setParameter("name", old.getName()).getSingleResult();
        
        sc_old.setName(newOne.getName());
        sc_old.setIpaddress(newOne.getIpaddress());
        sc_old.setMaximalUsers(newOne.getMaximalUsers());
        sc_old.setPort(newOne.getMaximalUsers());
        
        em.persist(sc_old);
        em.getTransaction().commit();
        shutDown();
    }
    
    public static ServerConfig getActiveConfig(){
        setUp();
        ServerConfig sc = (ServerConfig) em.createNamedQuery("ServerConfig.findActive").getSingleResult();
        shutDown();
        return sc;
    }
    
    public static List<ServerConfig> getAllServerConfigs(){
        setUp();
        List<ServerConfig> sc = (List<ServerConfig>)em.createNamedQuery("ServerConfig.findAll").getResultList();
        shutDown();
        return sc;
    }
    
    public static ServerConfig getServerConfigByName(String name){
        setUp();
        ServerConfig sc = (ServerConfig) em.createNamedQuery("ServerConfig.findByName").setParameter("name", name).getSingleResult();
        shutDown();
        return sc;
    }
    
    public static void setNewActiveConfig(ServerConfig newOne){
        setUp();
        ServerConfig old = (ServerConfig) em.createNamedQuery("ServerConfig.findActive").getSingleResult();
        old.setActive(false);
        ServerConfig sc = (ServerConfig) em.createNamedQuery("ServerConfig.findByName").setParameter("name", newOne.getName()).getSingleResult();
        sc.setActive(true);
        em.getTransaction().commit();
        shutDown();
    }
    
    public static void saveNewServerConfig(ServerConfig sc){
        setUp();
        em.persist(sc);
        em.getTransaction().commit();
        shutDown();
        logger.info(Utilities.getLogTime()+" New ServerConfiguration saved");
    }
    
    public static void deleteServerConfig(ServerConfig sc){
        setUp();
        ServerConfig old = (ServerConfig) em.createNamedQuery("ServerConfig.findByName").setParameter("name", sc.getName()).getSingleResult();
        em.remove(old);
        em.getTransaction().commit();
        shutDown();
        logger.info(Utilities.getLogTime()+" ServerConfiguration deleted: " + sc.getName());
    }
    
    /**
     * Saves a passed Message in the MessageBuffer
     * 
     * @param m ({@link Message})
     */
    public static void saveMessageInBuffer(Message m){
        setUp();
        MessageBuffer mb = new MessageBuffer();
        System.out.println(m.toString());
        mb.setMessage(m);
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
    
    public static void updateLastActionTime(int id){
        setUp();
        Member m = new Member();
        Query q = em.createNamedQuery("Member.findByID").setParameter("id", id);
        try {
            m = (Member) q.getSingleResult();
            System.out.println(m.toString());
        } catch (NoResultException e) {
            System.err.println("No User is founded");
        }
        m.setLastTimeOnline(new Date());
        System.out.println(m.toString());
        em.persist(m);
        em.getTransaction().commit();
        shutDown();
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
        Query q = em.createNamedQuery("MessageBuffer.findAllSavedMessangesFromReceiver").setParameter("rId", receiverId);
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
        Query query = em.createNamedQuery("MessageBuffer.findAllSavedMessangesFromReceiver").setParameter("rId", receiverId);
        List<MessageBuffer> list = query.getResultList();
        for ( MessageBuffer m : list){
            em.remove(m);
        }
        logger.info(list.size() +" Messages are deleted from buffer!");
        em.getTransaction().commit();
        shutDown();
    }
}
