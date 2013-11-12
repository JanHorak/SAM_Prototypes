/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.aes.encryption;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import sun.misc.BASE64Encoder;

/**
 *
 * @author janhorak
 */
public class Encryption {


    /**
     * This method returns the encrypted file you can pass in the parameters.
     * The File returns will be stored at the position you can pass, too.
     * @param path2FileForEncryption - File, you want to encrypt
     * @param outputFilePath - File- Location of the encrypted File
     * @param keyPath - Path of the Key (if the path is <b>null</b> it will use the default- Path
     * @return - The Encrypted File
     */
    public File returnEncryptedFile(String path2FileForEncryption ,String outputFilePath, String keyPath) {
        File file2Encrypt = new File(path2FileForEncryption);
        if ( keyPath == null || keyPath.isEmpty() ){
            keyPath = "secret.key";
        }
        
        File encryptedFile = new File(outputFilePath);
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        String fileInput ="";
        String tmp = "";
        
        try {
            br = new BufferedReader(new FileReader(file2Encrypt));
        } catch (IOException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while ((tmp = br.readLine()) != null ){
                    fileInput += tmp;
            }
        } catch (IOException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        fileInput = returnEncryptedString(fileInput, keyPath);
        try {
            bw = new BufferedWriter(new FileWriter(encryptedFile));
            bw.write(fileInput);
        } catch (IOException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            br.close();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return encryptedFile;
    }

    /**
     * This Method will be returns the encrypted String of the String you can 
     * pass in the parameters.
     * @param message - The String you want to encrypt
     * @param keyPath - Path of the Key (if the path is <b>null</b> it will use the default- Path
     * @return - The Encrypted String
     */
    public String returnEncryptedString(String message, String keyPath) {
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
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] encVal = null;
        try {
            encVal = cipher.doFinal(message.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        String encValue = new BASE64Encoder().encode(encVal);
        return encValue;
    }
}
