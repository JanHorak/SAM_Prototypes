package net.jan.rsa.test.enDeCryption;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jan.rsa.src.Decryption;
import net.jan.rsa.src.Encryption;
import net.jan.rsa.src.KeyGenerationHelper;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Jan Horak
 */
public class EnDecryptionTests {

    private final String dummyString = "lalalala - i'm a dummy- Text for the Encryption\n and "
                + "i'm singing... lalalalaaaa. !§$%&/";
   
    @Before
    public void init(){
        try {
            new KeyGenerationHelper().generateAndStoreKeys();
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException ex) {
            Logger.getLogger(EnDecryptionTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assertTrue(new File("private.key").exists());
        assertTrue(new File("public.key").exists());
    }
    
    @After
    public void cleanUp(){
        new File("private.key").delete();
        new File("public.key").delete();
    }
    
    
    @Test
    public void shouldEnAndDecryptATextFileTest(){
        byte[] encryptedArray = null;
        byte[] decryptedArray = null;
        
        try {
            encryptedArray = Encryption.encrypt("public.key", dummyString);
        } catch (IOException | InvalidKeyException ex) {
            Logger.getLogger(EnDecryptionTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(encryptedArray.length > 0);
        try {
            decryptedArray = Decryption.decrypt("private.key", encryptedArray);
        } catch (IOException | InvalidKeyException ex) {
            Logger.getLogger(EnDecryptionTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(decryptedArray.length > 0);
        try {
            System.out.println("DecryptedText: "+new String(decryptedArray, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EnDecryptionTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}