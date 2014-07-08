/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.test.utilities;

import static org.junit.Assert.*;
import org.junit.Test;
import sam_testclient.utilities.Utilities;

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
            System.out.println(Utilities.generateRandomUUID());
        }

        System.out.println("cleaned UUID's:");
        for (int i = 0; i < 10; i++) {
            System.out.println(Utilities.generateNewCleanedUUID());
        }
    }

    @Test
    public void generateRandomNumbers() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Utilities.generateRandomNumberBetween(5, 100));
        }
    }

    @Test
    public void calcTest() {
        System.out.println("---Start Calculation- Tests---");
        for (int y = 0; y < 10; y++) {
            System.out.println("----" + y + "----");
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
