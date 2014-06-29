/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.test.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
public class CheckResourcesTest {
    
    public CheckResourcesTest() {
    }
    
    @BeforeClass
    public static void checkAvailabilityOfProperties(){
        String propertiesPath = "resources/properties/client.properties";
        if (!new File(propertiesPath).exists()){
            Properties p = new Properties();
            p.setProperty("avatar", "../resources/graphics/unknown.png");
            p.setProperty("announcementName", "Testname");
            p.setProperty("recreationType", "AT_SERVER");
            p.setProperty("recreationDays", "0");
            p.setProperty("autodownload", "YES");
            p.setProperty("validFor", "WLAN");
            p.setProperty("allowWebClientRequests", "true");
            p.setProperty("saveLocaleHistory", "true");
            try {
                p.store(new FileOutputStream(new File(propertiesPath)), "");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CheckResourcesTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CheckResourcesTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("--> " + propertiesPath + " is added");
        } else {
            System.out.println("Client- Properties founded. No recreation needed");
        }
    }
    
    
    @Test
    public void checkPropertiesTest(){
        File propertiesFile = new File("resources/properties/client.properties");
        assertTrue(propertiesFile.exists());
        assertTrue(propertiesFile.canRead());
        assertTrue(propertiesFile.canWrite());
    }
    
    @Test
    public void checkImagesTest(){
        File imageFile = new File("resources/graphics/testImage_buntesBild.png");
        assertTrue(imageFile.exists());
        File imageFile2 = new File("resources/graphics/testImage_buntesBildII.png");
        assertTrue(imageFile2.exists());
        File imageFile3 = new File("resources/graphics/testImage_shouldFail.png");
        assertTrue(imageFile3.exists());
        File imageFile4 = new File("resources/graphics/unknown.png");
        assertTrue(imageFile4.exists());
    }
    
    
}
