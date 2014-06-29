/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author janhorak
 */
public class Utilities {

    /**
     * Builds and returns the MD5- Hash of the incoming String.
     *
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

    public static String getLogTime() {
        return "[" + new Date().toString() + "]";
    }

    public static String getTime() {
        SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
        return sm.format(new Date()).toString();
    }

    public static Map<Integer, Boolean> getOnlineMap(String input) {
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        input = input.substring(1,input.length()-1);
        String[] cleaned = input.split(", ");
        
        for ( int i = 0; i < cleaned.length; i++){
            System.out.println(cleaned[i]);
            String[] tmpSplit = cleaned[i].split("=");
            map.put(Integer.parseInt(tmpSplit[0]), Boolean.valueOf(tmpSplit[1]));
        }
        System.out.println(map);
        return map;
    }
    
    /**
     * Generates a random UUID for the Secret
     * @return UUID
     */
    public static UUID generateRandomUUID(){
        return UUID.randomUUID();
    }
    
    /**
     * Generates a random UUID and returns the cleaned String.
     * Removes all letters and sonder symbols from the UUID
     * @return cleaned UUID
     */
    public static String generateNewCleanedUUID(){
        return generateRandomUUID().toString().replaceAll("[^\\d.]", "");
    }
    
    /**
     * Returns a incoming String from letters and sonder symbols.
     * 
     * @param uuid (Type String)
     * @return cleaned String
     */
    public static String generateCleanedUUID(String uuid){
        return uuid.replaceAll("[^\\d.]", "");
    }
    
    /**
     * Returns a random int in a passed range
     * @param min
     * @param max
     * @return random int in range
     */
    public static int generateRandomNumberBetween(int min, int max){
        return (int)(min + (Math.random() * (max - min)));
    }
    
    /**
     * Calculates the Secret.
     * @param secret
     * @return Result of the calculation
     */
    public static String calculateSecret(String secret){
        String cleaned[] = secret.split(" ");
        String cleanedUUID = generateCleanedUUID(cleaned[0]);
        String result = "";
        String number = cleaned[1];
        for (int i = 0; i < cleanedUUID.length(); i++) {
                int c = cleanedUUID.charAt(i);
                result += String.valueOf(c % Integer.valueOf(number));
            }
        return result;
    }

}
