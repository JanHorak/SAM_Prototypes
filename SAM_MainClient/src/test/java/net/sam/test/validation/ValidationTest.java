/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.test.validation;

import java.io.File;
import javax.swing.ImageIcon;
import static org.junit.Assert.*;
import org.junit.Test;
import sam_testclient.entities.AvatarImage;
import sam_testclient.entities.Handshake;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumHandshakeReason;
import sam_testclient.enums.EnumHandshakeStatus;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.sources.ValidationManager;
import sam_testclient.validations.Avatar;


/**
 *
 * @author janhorak
 */
public class ValidationTest {

    public ValidationTest() { 
    }

    /**
     * Test for Messages and Handshakes.
     */
    @Test
    public void TestMessageValidation() {
        // Test for simple Messages
        Message m = new Message(1, 3, EnumKindOfMessage.LOGIN, "Blaaa", "additional bla");
        Message m2 = new Message(1, 4, EnumKindOfMessage.LOGIN, null, null);
        Message m3 = new Message(1, 5, EnumKindOfMessage.LOGIN, null, "additional bla");
        Message m4 = new Message(1, 2, null, "bla", "additional bla");
        Message m5 = new Message(1, 5, null, null, null);

        assertTrue(ValidationManager.isValid(m));
        assertTrue(!ValidationManager.isValid(m2));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m2).size() == 2);
        assertTrue(!ValidationManager.isValid(m3));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m3).size() == 1);
        assertTrue(!ValidationManager.isValid(m4));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m4).size() == 1);
        assertTrue(!ValidationManager.isValid(m5));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m5).size() == 3);

        // Test for Handshakes

        Handshake hs = new Handshake(0, EnumHandshakeStatus.END, EnumHandshakeReason.FILE_REQUEST, true, "ff");
        m = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        m.setHandshake(hs);
        hs.setOwner(m);
        assertTrue(ValidationManager.isValid(hs));
        
        Handshake hs2 = new Handshake(0, EnumHandshakeStatus.END, null, true, null);
        m2 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        m2.setHandshake(hs2);
        hs2.setOwner(m2);
        assertTrue(!ValidationManager.isValid(hs2));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(hs2).size() == 2);
        
        Handshake hs3 = new Handshake(0, EnumHandshakeStatus.END, EnumHandshakeReason.FILE_REQUEST, true, "eaw");
        m3 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        m3.setHandshake(hs3);
        hs3.setOwner(m3);
        assertTrue(ValidationManager.isValid(hs3));
        
        Handshake hs4 = new Handshake(0, null, EnumHandshakeReason.FILE_REQUEST, true, "dasd");
        m4 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        m4.setHandshake(hs4);
        hs4.setOwner(m4);
        assertTrue(!ValidationManager.isValid(hs4));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(hs4).size() == 1);
        
        Handshake hs5 = new Handshake(0, null, null, true, "dadsd");
        m5 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        m5.setHandshake(hs5);
        hs5.setOwner(m5);
        assertTrue(!ValidationManager.isValid(hs5));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(hs5).size() == 2);
        
    }
    
   
    
    @Test
    public void avatarTest(){
        AvatarImage avatar;
        File file = new File("resources/graphics/testImage_buntesBild.png");
        File file2 = new File("resources/graphics/testImage_shouldFail.png");

        avatar = new AvatarImage(file.getAbsolutePath());
        assertTrue(ValidationManager.isValid(avatar));
        
        avatar = new AvatarImage(file2.getAbsolutePath());
        assertTrue(!ValidationManager.isValid(avatar));
    }
}
