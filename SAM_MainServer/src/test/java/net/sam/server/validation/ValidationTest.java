/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.validation;

import net.sam.server.entities.Handshake;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.entities.Message;
import net.sam.server.enums.EnumHandshakeReason;
import net.sam.server.enums.EnumHandshakeStatus;
import net.sam.server.manager.ValidationManager;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void TestMessageValidation(){
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
        
        // Please note: ValidationTest will print Validationmessage:
        // One of the HandshakeValues is null!
        // for *every* error
        Handshake hs = new Handshake();
        hs.setId(200);
        hs.setAnswer(true);
        hs.setContent("dasd");
        hs.setReason(EnumHandshakeReason.FILE_REQUEST);
        hs.setStatus(EnumHandshakeStatus.END);
        m = new Message(hs);
        assertTrue(ValidationManager.isValid(m));
        
        Handshake hs2 = new Handshake();
        hs2.setId(200);
        hs2.setAnswer(true);
        hs2.setContent(null); // Error
        hs2.setReason(null); // Error
        hs2.setStatus(EnumHandshakeStatus.END);
        m2 = new Message(hs2);
        assertTrue(!ValidationManager.isValid(m2));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m2).size() == 2);
        
        Handshake hs3 = new Handshake();
        hs3.setId(200);
        hs3.setAnswer(true);
        hs3.setContent("dasd");
        hs3.setReason(EnumHandshakeReason.FILE_REQUEST);
        hs3.setStatus(EnumHandshakeStatus.END);
        m3 = new Message(hs3);
        assertTrue(ValidationManager.isValid(m3));
        
        Handshake hs4 = new Handshake();
        hs4.setId(200);
        hs4.setAnswer(true);
        hs4.setContent("dasd");
        hs4.setReason(EnumHandshakeReason.FILE_REQUEST);
        hs4.setStatus(null); // Error
        m4 = new Message(hs4);
        assertTrue(!ValidationManager.isValid(m4));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m4).size() == 1);
        
        Handshake hs5 = new Handshake();
        hs5.setId(200);
        hs5.setAnswer(true);
        hs5.setContent("dasd");
        hs5.setReason(null); // Error
        hs5.setStatus(null); // Error
        m5 = new Message(hs5);
        assertTrue(!ValidationManager.isValid(m5));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m5).size() == 1);
        
    }
}
