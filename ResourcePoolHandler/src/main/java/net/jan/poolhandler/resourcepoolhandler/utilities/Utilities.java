/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.jan.poolhandler.resourcepoolhandler.utilities;

import java.util.Date;

/**
 *
 * @author janhorak
 */
public class Utilities {
    
    
    public static String getLogTime(){
        return "["+new Date().toString()+"]";
    }

}
