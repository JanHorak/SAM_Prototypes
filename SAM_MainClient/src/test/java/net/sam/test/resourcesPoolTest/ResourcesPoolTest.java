/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.test.resourcesPoolTest;

import java.util.Properties;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import net.jan.poolhandler.resourcepoolhandler.exceptions.MethodNotAllowedForResourceType;
import net.jan.poolhandler.resourcepoolhandler.exceptions.ResourcesAlreadyLoadedException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sam_testclient.services.ClientResoucesPool;

/**
 *
 * @author Jan
 */

public class ResourcesPoolTest {

    public ResourcesPoolTest() {
    }

    @Before
    public void init() {
        if (ResourcePoolHandler.isLoaded()) {
            ResourcePoolHandler.destroy();
        }
        ResourcePoolHandler.loadFileResources(ClientResoucesPool.class);
    }

    @Test
    public void testProperties() {
        Properties clientProps = ResourcePoolHandler.getResource("clientProperties");
        assertTrue(!clientProps.isEmpty() || clientProps != null);
        assertTrue(clientProps.getProperty("announcementName") != null);

        String buffer = clientProps.getProperty("announcementName");

        ResourcePoolHandler.PropertiesHelper.setValueInProperties("clientProperties", "announcementName", "TestName");
        assertTrue(!clientProps.isEmpty() || clientProps != null);
        assertTrue(clientProps.getProperty("announcementName").equals("TestName"));

        //reset to default
        ResourcePoolHandler.PropertiesHelper.setValueInProperties("clientProperties", "announcementName", buffer);
    }

    // Exceptiontesting
    @Test(expected = ResourcesAlreadyLoadedException.class)
    public void shouldFailWithResourcesAlreadyLoadedException(){
         ResourcePoolHandler.loadFileResources(ClientResoucesPool.class);
    }
    
    @Test(expected = MethodNotAllowedForResourceType.class)
    public void shouldFailWithMethodNotAllowedForResourceType(){
        ResourcePoolHandler.PropertiesHelper.setValueInProperties("samLogo", "DAYSFORKEYRECREATION", "5");
        ResourcePoolHandler.TextFileHelper.writeContentToTextFile("samLogo", "This is my Fail-Text", true);
    }
    
    @After
    public void tearDown() {
        ResourcePoolHandler.destroy();
    }

}
