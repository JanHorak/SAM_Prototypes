package net.sam.test.utilities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import sam_testclient.sources.FileManager;

/**
 *
 * @author janhorak
 */
public class UtilityHelperTest {
    
    @Test
    public void generateEmptyBuddyListTest(){
        Map<Integer, String> buddyList = new HashMap<Integer, String>();
        FileManager.serialize(buddyList, "buddyList.data");
        buddyList = null;
        buddyList = (Map<Integer, String>) FileManager.deserialize("buddyList.data");
        System.out.println(buddyList.toString());
    }
}
