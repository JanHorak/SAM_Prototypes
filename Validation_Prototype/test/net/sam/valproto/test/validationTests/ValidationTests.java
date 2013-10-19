/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.valproto.test.validationTests;

import java.util.Set;
import javax.validation.ConstraintViolation;
import net.sam.valproto.entities.User;
import net.sam.valproto.mainvalidator.ValidationManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class ValidationTests {
    
    @Test
    public void testObjectShouldBeCompletlyValid(){
        User us = new User();
        us.setName("Hans");
        us.setPassword("123aaa");
        us.seteMail("admin@sam.net");
        us.setMoney(10000);

        Set<ConstraintViolation<Object>> violations = ValidationManager.returnConstraintViolantionSet(us);
        assertTrue(violations.isEmpty());
        ValidationManager.validate(us);
    }
    
    @Test
    public void testObjectShouldBeCompletlyInValid(){
        User us = new User();
        us.setName(null);
        us.setPassword(null);
        us.seteMail("admin@SAM.net");
        us.setMoney(4);

        Set<ConstraintViolation<Object>> violations = ValidationManager.returnConstraintViolantionSet(us);
        assertTrue(!violations.isEmpty());
        System.out.println("---Errors were expected!---");
        ValidationManager.validate(us);
        System.out.println("---End of expected Errors!---");
    }
}