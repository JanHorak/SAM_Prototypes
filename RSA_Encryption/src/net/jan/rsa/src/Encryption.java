/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.rsa.src;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
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
public class Encryption {

    public static final String ALGORITHM = "RSA";

    /**
     * This static method returns the bytearray which is encrypted with the publicKey
     * 
     * @param publicKey_path gives the path of the public key
     * @param message gives the message we want to encrypt
     * @return the encrypted bytearray
     * @throws IOException
     * @throws InvalidKeyException 
     */
    public static byte[] encrypt(String publicKey_path, String message) throws IOException, InvalidKeyException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // reads the Public Key from the File
        PublicKey publicKey = new KeyGenerationHelper().readPublicKeyFromFile(publicKey_path);
        
        // Initializes the cipher with the mode and the Key
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        // Creates the cipherData - a byteArray for the encrypted data
        byte[] cipherData = null;
        try {
            // Encryption
            cipherData = cipher.doFinal(message.getBytes());
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cipherData;
    }

    
}
