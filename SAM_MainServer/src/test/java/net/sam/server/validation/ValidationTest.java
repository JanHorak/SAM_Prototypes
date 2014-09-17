/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.validation;

import net.sam.server.entities.Handshake;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.entities.Message;
import net.sam.server.entities.ServerConfig;
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
    public void TestMessageValidation() {
        // Test for simple Messages
        Message m = new Message(1, 3, EnumKindOfMessage.LOGIN, "Blaaa", "additional bla");
        Message m2 = new Message(1, 4, EnumKindOfMessage.LOGIN, null, null);
        Message m3 = new Message(1, 5, EnumKindOfMessage.LOGIN, null, "additional bla");
        Message m4 = new Message(1, 2, null, "bla", "additional bla");
        Message m5 = new Message(1, 5, null, null, null);
        Message m6 = new Message(1, 4, EnumKindOfMessage.LOGIN, "", "");

        assertTrue(ValidationManager.isValid(m));
        assertTrue(!ValidationManager.isValid(m2));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m2).size() == 2);
        assertTrue(!ValidationManager.isValid(m3));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m3).size() == 1);
        assertTrue(!ValidationManager.isValid(m4));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m4).size() == 1);
        assertTrue(!ValidationManager.isValid(m5));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(m5).size() == 3);
        assertTrue(ValidationManager.isValid(m6));


        Handshake hs = new Handshake();
        hs.setId(200);
        hs.setAnswer(true);
        hs.setContent("dasd");
        hs.setReason(EnumHandshakeReason.FILE_REQUEST);
        hs.setStatus(EnumHandshakeStatus.END);
        m = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        hs.setOwner(m);
        assertTrue(ValidationManager.isValid(hs));

        Handshake hs2 = new Handshake();
        hs2.setId(200);
        hs2.setAnswer(true);
        hs2.setContent(null); // Error
        hs2.setReason(null); // Error
        hs2.setStatus(EnumHandshakeStatus.END);
        m2 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        m2.setHandshake(hs2);
        hs2.setOwner(m2);
        assertTrue(!ValidationManager.isValid(hs2));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(hs2).size() == 2);

        Handshake hs3 = new Handshake();
        hs3.setId(200);
        hs3.setAnswer(true);
        hs3.setContent("dasd");
        hs3.setReason(EnumHandshakeReason.FILE_REQUEST);
        hs3.setStatus(EnumHandshakeStatus.END);
        m3 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        hs3.setOwner(m3);
        assertTrue(ValidationManager.isValid(hs3));

        Handshake hs4 = new Handshake();
        hs4.setId(200);
        hs4.setAnswer(true);
        hs4.setContent("dasd");
        hs4.setReason(EnumHandshakeReason.FILE_REQUEST);
        hs4.setStatus(null); // Error
        m3 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        hs4.setOwner(m3);
        assertTrue(!ValidationManager.isValid(hs4));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(hs4).size() == 1);

        Handshake hs5 = new Handshake();
        hs5.setId(200);
        hs5.setAnswer(true);
        hs5.setContent("dasd");
        hs5.setReason(null); // Error
        hs5.setStatus(null); // Error
        m5 = new Message(0, 1, EnumKindOfMessage.LOGIN, "d", "ddd");
        hs5.setOwner(m5);
        assertTrue(!ValidationManager.isValid(hs5));
        assertTrue(ValidationManager.returnAmountOfInvalidFields(hs5).size() == 2);

    }

    @Test
    public void shouldValidateSomeServerConfigs() {
        ServerConfig s = new ServerConfig();
        ServerConfig s2 = new ServerConfig();
        ServerConfig s3 = new ServerConfig();
        ServerConfig s4 = new ServerConfig();
        ServerConfig s5 = new ServerConfig();
        ServerConfig s6 = new ServerConfig();
        ServerConfig s7 = new ServerConfig();
        ServerConfig s8 = new ServerConfig();
        ServerConfig s9 = new ServerConfig();

        s.setName("my1");
        s.setActive(true);
        s.setDeleteable(true);
        s.setIpaddress("112.125.145.125");
        s.setMaximalUsers(2);
        s.setPort(2125);
        assertTrue(ValidationManager.isValid(s));

        s2.setName("my1");
        s2.setActive(true);
        s2.setDeleteable(true);
        s2.setIpaddress("112.dd.145.125"); // Error
        s2.setMaximalUsers(2);
        s2.setPort(2125);
        assertTrue(!ValidationManager.isValid(s2));

        s3.setName("my1");
        s3.setActive(true);
        s3.setDeleteable(true);
        s3.setIpaddress("-33.125.31.125"); // Error
        s3.setMaximalUsers(2);
        s3.setPort(2125);
        assertTrue(!ValidationManager.isValid(s3));

        s4.setName("my1");
        s4.setActive(true);
        s4.setDeleteable(true);
        s4.setIpaddress("112.125.145.125");
        s4.setMaximalUsers(2);
        s4.setPort(77253); // Error
        assertTrue(!ValidationManager.isValid(s4));

        s5.setName("my1");
        s5.setActive(true);
        s5.setDeleteable(true);
        s5.setIpaddress("112.125.145.125");
        s5.setMaximalUsers(200); // Error
        s5.setPort(2125);
        assertTrue(!ValidationManager.isValid(s5));
        s5.setMaximalUsers(-3); // Error
        assertTrue(!ValidationManager.isValid(s5));

        s6.setName("my1");
        s6.setActive(true);
        s6.setDeleteable(true);
        s6.setIpaddress("localhost");
        s6.setMaximalUsers(2);
        s6.setPort(2125);
        assertTrue(ValidationManager.isValid(s6));

        s7.setName("my1");
        s7.setActive(true);
        s7.setDeleteable(true);
        s7.setIpaddress("LOCALHOST");
        s7.setMaximalUsers(2);
        s7.setPort(2125);
        assertTrue(ValidationManager.isValid(s7));

        s8.setName("my1");
        s8.setActive(true);
        s8.setDeleteable(true);
        s8.setIpaddress("0.0.0.0"); // Error
        s8.setMaximalUsers(2);
        s8.setPort(2125);
        assertTrue(!ValidationManager.isValid(s8));

        s9.setName("my1");
        s9.setActive(true);
        s9.setDeleteable(true);
        s9.setIpaddress("350.254.1440.258"); // Error
        s9.setMaximalUsers(2);
        s9.setPort(2125);
        assertTrue(!ValidationManager.isValid(s9));
    }
}
