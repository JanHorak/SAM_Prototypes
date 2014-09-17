/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.cdiTests;

import net.sam.server.beans.ServerMainBean;
import net.sam.server.services.ContainerService;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
@Ignore
public class CDITest {
    
    ContainerService container = new ContainerService();
    
    @Before
    public void setupContainer(){
        container.startContainer();
    }
    
    @Test
    public void singletonSingleInjectionTest(){
        ServerMainBean test = ContainerService.getBean(ServerMainBean.class);
        assertNotNull(test);
    }
    
    
    @After
    public void tearDown(){
        container.shutDown();
    }
    
    
}
