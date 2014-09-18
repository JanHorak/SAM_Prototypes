/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.test.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import sam_testclient.entities.MemberSettings;

/**
 *
 * @author Jan
 */
@Ignore
public class UpdateTest {
    
    public UpdateTest() {
    }

    private EntityManager em;

    private EntityManagerFactory emf;
    
    @Test
    public void update(){
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
        em.getTransaction().begin();
        MemberSettings ms = new MemberSettings();
        ms = (MemberSettings) em.createNamedQuery("MemberSettings.find").getSingleResult();
        
        MemberSettings settings = ms;
        settings.setAllowWebClients(true);
        settings.setAutoDownload(MemberSettings.AutoDownload.YES);
        settings.setAvatarPath("dddddd");
        settings.setHistBorder("11111");
        settings.setName("asdasd");
        settings.setAnnouncementName("asdasd");
        settings.setRecreationDays(2);
        settings.setRecreationType(MemberSettings.RecreationEnum.AT_LOGIN);
        settings.setSaveLocaleHistory(true);
        settings.setValidFor(MemberSettings.ValidFor.WLAN);
        
        em.merge(settings);
        em.getTransaction().commit();
        em.close();
    }
}
