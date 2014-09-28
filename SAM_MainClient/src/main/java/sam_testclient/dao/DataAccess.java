/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AsyncResult;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sam_testclient.communication.Client;
import sam_testclient.entities.Buddy;
import sam_testclient.entities.MemberSettings;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumKindOfMessage;

/**
 *
 * @author Jan
 */
public class DataAccess {


    public static void updateClientSettings(MemberSettings settings) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        MemberSettings result = (MemberSettings) em.createNamedQuery("MemberSettings.find").getSingleResult();

        result.setAllowWebClients(settings.isAllowWebClients());
        result.setAutoDownload(settings.getAutoDownload());
        result.setAvatarPath(settings.getAvatarPath());
        result.setHistBorder(settings.getHistBorder());
        result.setName(settings.getName());
        result.setAnnouncementName(settings.getAnnouncementName());
        result.setRecreationDays(settings.getRecreationDays());
        result.setRecreationType(settings.getRecreationType());
        result.setSaveLocaleHistory(settings.isSaveLocaleHistory());
        result.setValidFor(settings.getValidFor());

        em.merge(result);
        em.getTransaction().commit();
        em.close();
    }

    public static MemberSettings getMembersettings() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        MemberSettings result = (MemberSettings) em.createNamedQuery("MemberSettings.find").getSingleResult();
        em.getTransaction().commit();
        em.close();
        return result;
    }

    public static synchronized void saveNewMessage(Message hs) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(hs);
        em.getTransaction().commit();
        em.close();
    }

    public static synchronized void updateMessage(Message m) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Message message = null;

        if (m.getMessageType() == EnumKindOfMessage.MESSAGE_STATUS) {
            message = (Message) em.createNamedQuery("Message.findById").setParameter("id", m.getContent()).getSingleResult();
        }
        if (m.getMessageType() == EnumKindOfMessage.MESSAGE) {
            message = (Message) em.createNamedQuery("Message.findById").setParameter("id", m.getId()).getSingleResult();
        }
        message.setMessageStatus(m.getMessageStatus());
        em.merge(message);
        em.getTransaction().commit();
        em.close();
    }

    public static synchronized Future saveNewBuddy(Buddy buddy) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(buddy);
        em.getTransaction().commit();
        em.close();
        return new AsyncResult("Saving finished");
    }

    public static synchronized void mergeBuddy(Buddy buddy) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Attached Object
        Buddy old = (Buddy) em.createNamedQuery("Buddy.getBuddyById").setParameter("internalId", buddy.getInternalID()).getSingleResult();
        old = buddy;
        em.merge(old);
        em.getTransaction().commit();
        em.close();
    }

    public static synchronized List<Buddy> getAllBuddies() {
        List<Buddy> result = new ArrayList<>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        result = (List<Buddy>) em.createNamedQuery("Buddy.getAllBuddies").getResultList();
        if (result == null) {
            result = new ArrayList<>();
        }
        em.close();
        return result;
    }

    public static synchronized List<Message> getAllMessagesWhereBuddyIsSender(Buddy buddy) {
        List<Message> result = new ArrayList<>();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        result = (List<Message>) em.createNamedQuery("Message.findConversation")
                .setParameter("sId", Integer.valueOf(buddy.getInternalID()))
                .setParameter("ownId", Client.ownID)
                .getResultList();
        if (result == null) {
            result = new ArrayList<Message>();
        }
        em.close();
        return result;
    }
    
    public static synchronized List<Message> getAllMessagesWhereBuddyIsInvolved(Buddy buddy) {
        List<Message> result = new ArrayList<>();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        result = (List<Message>) em.createNamedQuery("Message.findConversationWhereBuddyIsInvolved")
                .setParameter("sId", Integer.valueOf(buddy.getInternalID()))
                .getResultList();
        em.close();
        return result;
    }

}
