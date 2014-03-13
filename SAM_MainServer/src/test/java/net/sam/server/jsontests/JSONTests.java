/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.jsontests;

import net.sam.server.entities.Message;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.manager.MessageWrapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class JSONTests {
    
    
    @Test
    public void createSomeJSONsTest(){
        Message m1 = new Message(0, 0, EnumKindOfMessage.MESSAGE, "I'm a message!", "other");
        Message m2 = new Message(0, 0, EnumKindOfMessage.KEYEXCHANGE, "I'm a Keyexchange- request!", "other");
        Message m3 = new Message(0, 0, EnumKindOfMessage.REGISTER, "I'm a register- message!", "other");
        
        String json1 = MessageWrapper.createJSON(m1);
        String json2 = MessageWrapper.createJSON(m2);
        String json3 = MessageWrapper.createJSON(m3);
        
        assertNotNull(json1);
        System.out.println(json1);
        assertNotNull(json2);
        System.out.println(json2);
        assertNotNull(json3);
        System.out.println(json3);
        
        Message reM1 = MessageWrapper.JSON2Message(json1);
        assertNotNull(reM1);
        System.out.println(reM1.toString());
        Message reM2 = MessageWrapper.JSON2Message(json2);
        assertNotNull(reM2);
        System.out.println(reM2.toString());
        Message reM3 = MessageWrapper.JSON2Message(json3);
        assertNotNull(reM3);
        System.out.println(reM3.toString());
    }
    
}
