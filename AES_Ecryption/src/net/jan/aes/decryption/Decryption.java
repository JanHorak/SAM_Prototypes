/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.aes.decryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import sun.misc.BASE64Decoder;

/**
 *
 * @author janhorak
 */
public class Decryption {

    /**
     * This method returns the decrypted file you can pass in the parameters.
     * The File returns will be stored at the position you can pass, too.
     * @param path2FileForDecryption - File, you want to decrypt
     * @param outputFilePath - File- Location of the decrypted File
     * @param keyPath - Path of the Key (if the path is <b>null</b> it will use the default- Path
     * @return - The Decrypted File
     */
    public File returnDecryptedFile(String path2FileForDecryption, String outputFilePath, String keyPath) {
        File encryptedFile = new File(path2FileForDecryption);
        if (keyPath == null || keyPath.isEmpty()) {
            keyPath = "secret.key";
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        File outputFile = new File(outputFilePath);
        String tmp = "";

        try {
            br = new BufferedReader(new FileReader(encryptedFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }

        String fileInput = "";
        try {
            while ((tmp = br.readLine()) != null) {
                fileInput += tmp;
            }
        } catch (IOException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }


        try {
            bw = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        String decryptedString = returnDecryptedString(fileInput, keyPath);
        try {
            bw.write(decryptedString);
        } catch (IOException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            br.close();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputFile;

    }

    /**
     * This Method will be returns the decrypted String of the String you can 
     * pass in the parameters.
     * @param encryptedString - The String you want to decrypt
     * @param keyPath - Path of the Key (if the path is <b>null</b> it will use the default- Path
     * @return - The Decrypted String
     */
    public String returnDecryptedString(String encryptedString, String keyPath) {
        Key key;
        if (keyPath != null) {
            key = new KeyGenerationManager().readSecretKey(keyPath);
        } else {
            key = new KeyGenerationManager().readSecretKey();
        }

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] decodedValue = null;
        try {
            decodedValue = new BASE64Decoder().decodeBuffer(encryptedString);
        } catch (IOException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] decValue = null;
        try {
            decValue = cipher.doFinal(decodedValue);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        String decrValue = new String(decValue);
        return decrValue;

    }
}
