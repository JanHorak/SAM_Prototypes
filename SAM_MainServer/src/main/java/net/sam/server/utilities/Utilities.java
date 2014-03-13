/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author janhorak
 */
public class Utilities {
    
    /**
     * Builds and returns the MD5- Hash of the incoming String.
     * @param input
     * @return Hashed String
     */
    public static String getHash(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Error with Hash");
        }
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    
}
