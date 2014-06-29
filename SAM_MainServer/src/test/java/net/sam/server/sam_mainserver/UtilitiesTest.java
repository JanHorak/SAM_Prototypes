/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.sam_mainserver;

import net.sam.server.utilities.Utilities;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 *
 * @author janhorak
 */
public class UtilitiesTest {

    public UtilitiesTest() {
    }

    @Test
    public void UUIDTest() {
        System.out.println("UUID's:");
        for (int i = 0; i < 10; i++) {
            assertNotNull(Utilities.generateRandomUUID());
        }

        System.out.println("cleaned UUID's:");
        for (int i = 0; i < 10; i++) {
            assertNotNull(Utilities.generateNewCleanedUUID());
        }
    }

    @Test
    public void generateRandomNumbers() {
        for (int i = 0; i < 10; i++) {
            int number = Utilities.generateRandomNumberBetween(5, 100);
            assertNotNull(number);
            assertTrue(number > 5);
            assertTrue(number <= 100);
        }
    }

    @Test
    public void calcTest() {
        System.out.println("---Start Calculation- Tests---");
        for (int y = 0; y < 10; y++) {
            String number = String.valueOf(Utilities.generateRandomNumberBetween(2, 10));
            System.out.println("RandomNumber: " + number);
            String cleaned = Utilities.generateNewCleanedUUID();
            System.out.println("CleanedUUID: " + cleaned);
            String result = "";
            result = Utilities.calculateSecret(cleaned + " " + number);
            System.out.println("Result: " + result);
            System.out.println("");
        }
        System.out.println("---End Calculation- Tests---");

    }

}
