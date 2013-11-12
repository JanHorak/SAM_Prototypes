/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.rsa.src;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Jan Horak
 */
public class Decryption {
    
    public static final String ALGORITHM = "RSA";

    /**
     * This static method returns the bytearray which is decrypted with the privateKey
     * 
     * @param privateKey_path gives the path of the public key
     * @param encryptedMessage gives the message we want to encrypt
     * @return the decrypted bytearray
     * @throws IOException
     * @throws InvalidKeyException 
     */
    public static byte[] decrypt(String privateKey_path, byte[] encryptedMessage) throws IOException, InvalidKeyException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        // reads the PrivateKey  from the File
        PrivateKey privateKey =  new KeyGenerationHelper().readPrivateKeyFromFile(privateKey_path);
        
        // Initializes the cipher with the mode and the privateKey
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        // Creates the cipherData - a byteArray for the decrypted data
        byte[] cipherData = null;
        try {
            // Decryption
            cipherData = cipher.doFinal(encryptedMessage);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherData;
    }
    
}
