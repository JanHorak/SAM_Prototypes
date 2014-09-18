/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.resourcesPoolTest;

import java.util.Properties;
import javax.swing.ImageIcon;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import net.jan.poolhandler.resourcepoolhandler.exceptions.MethodNotAllowedForResourceType;
import net.jan.poolhandler.resourcepoolhandler.exceptions.ResourcesAlreadyLoadedException;
import net.sam.server.services.ServerResoucesPool;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Jan
 */
public class ResourcesPoolTest {
    
    public ResourcesPoolTest() {
    }

    
    @Before
    public void init(){
        if (ResourcePoolHandler.isLoaded()){
            ResourcePoolHandler.destroy();
        }
        ResourcePoolHandler.loadFileResources(ServerResoucesPool.class);
    }
    
    @Test
    public void testImages(){
        ImageIcon androidLogo = ResourcePoolHandler.getResource("androidLogo");
        assertNotNull(androidLogo);
        assertTrue(androidLogo.getIconHeight() > 10);
        
        ImageIcon samLogo = ResourcePoolHandler.getResource("samLogo");
        assertNotNull(samLogo);
        assertTrue(samLogo.getIconHeight() > 10);
    }
    
    @Test
    public void testProperties(){
        Properties serverProps = ResourcePoolHandler.getResource("serverProperties");
        assertTrue(!serverProps.isEmpty() || serverProps != null);
        assertTrue(serverProps.getProperty("DAYSFORKEYRECREATION") != null);
        
        String buffer = serverProps.getProperty("DAYSFORKEYRECREATION");
        
        ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "DAYSFORKEYRECREATION", "15");
        assertTrue(!serverProps.isEmpty() || serverProps != null);
        assertTrue(serverProps.getProperty("DAYSFORKEYRECREATION").equals("15"));
        
        //reset to default
        ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "DAYSFORKEYRECREATION", buffer);
    }
    
   
    // Exceptiontesting
    @Test(expected = ResourcesAlreadyLoadedException.class)
    public void shouldFailWithResourcesAlreadyLoadedException(){
         ResourcePoolHandler.loadFileResources(ServerResoucesPool.class);
    }
    
    @Test(expected = MethodNotAllowedForResourceType.class)
    public void shouldFailWithMethodNotAllowedForResourceType(){
        ResourcePoolHandler.PropertiesHelper.setValueInProperties("androidLogo", "DAYSFORKEYRECREATION", "5");
        ResourcePoolHandler.TextFileHelper.writeContentToTextFile("androidLogo", "This is my Fail-Text", true);
    }
    
    @After
    public void tearDown(){
        ResourcePoolHandler.destroy();
    }
    
}
