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
import sam_testclient.entities.MemberSettings;

/**
 *
 * @author Jan
 */
public class GenerateData {
    
    public GenerateData() {
    }
    
    private EntityManager em;

    private EntityManagerFactory emf;

    @Test
    public void generateData(){
        emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();
        
        MemberSettings settings = new MemberSettings();
        settings.setAllowWebClients(true);
        settings.setAutoDownload(MemberSettings.AutoDownload.YES);
        settings.setAvatarPath("sadasd");
        settings.setHistBorder("123");
        settings.setName("asdasd");
        settings.setRecreationDays(2);
        settings.setRecreationType(MemberSettings.RecreationEnum.AT_LOGIN);
        settings.setSaveLocaleHistory(true);
        settings.setValidFor(MemberSettings.ValidFor.WLAN);
        settings.setAnnouncementName("asdasd");
        
        em.getTransaction().begin();
        em.persist(settings);
        em.getTransaction().commit();
        em.close();
    }
    
    
}
