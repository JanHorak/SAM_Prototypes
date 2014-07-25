/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.test.resourcesPoolTest;

import java.util.Properties;
import javax.swing.ImageIcon;
import sam_testclient.exceptions.MethodNotAllowedForResourceType;
import sam_testclient.exceptions.ResourcesAlreadyLoadedException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import sam_testclient.services.ClientResoucesPool;
import sam_testclient.services.ResourcePoolHandler;

/**
 *
 * @author Jan
 */
@Ignore
public class ResourcesPoolTest {
    
    public ResourcesPoolTest() {
    }

    
    @Before
    public void init(){
        if (ResourcePoolHandler.isLoaded()){
            ResourcePoolHandler.destroy();
        }
        ResourcePoolHandler.loadFileResources(ClientResoucesPool.class);
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
    
    @Test
    public void testTexts(){
        String originalContent = ResourcePoolHandler.getResource("serverLog");
        assertTrue(!originalContent.isEmpty() || originalContent != null);
        String testString = "MyTestStringLaLaLa";
        
        ResourcePoolHandler.TextFileHelper.writeContentToTextFile("serverLog", testString, true);
        String testContent = ResourcePoolHandler.getResource("serverLog");
        assertTrue(!testContent.equals(originalContent));
        
        ResourcePoolHandler.TextFileHelper.writeContentToTextFile("serverLog", originalContent, false);
        assertTrue(!ResourcePoolHandler.getResource("serverLog").toString().contains("LaLaLa"));
    }
    
    // Exceptiontesting
    @Test(expected = ResourcesAlreadyLoadedException.class)
    public void shouldFailWithResourcesAlreadyLoadedException(){
         ResourcePoolHandler.loadFileResources(ClientResoucesPool.class);
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
