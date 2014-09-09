/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import sam_testclient.entities.MemberSettings;

/**
 *
 * @author Jan
 */
public abstract class DataAccess {

    static EntityManager em;
    static EntityManagerFactory emf;

    public static void updateClientSettings(MemberSettings settings) {

        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
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
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        MemberSettings result = (MemberSettings) em.createNamedQuery("MemberSettings.find").getSingleResult();
        em.getTransaction().commit();
        em.close();
        return result;
    }

}
