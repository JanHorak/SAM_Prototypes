/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.rsa.test.keyGenerationTests;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jan.rsa.src.KeyGenerationHelper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Horak
 */
public class KeyGenerationTests {
    
    public KeyGenerationTests() {
    }
    
    @Test
    public void shouldTestGeneratingOfKeyPairs(){
        try {
            new KeyGenerationHelper().generateAndStoreKeys();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        File publicKey = new File("public.key");
        File privateKey = new File ("private.key");
        
        assertTrue(publicKey.exists());
        assertTrue(privateKey.exists());
        
        assertTrue(publicKey.length() > 0);
        assertTrue(privateKey.length() > 0);
        
        publicKey.delete();
        privateKey.delete();
        
        assertTrue(!publicKey.exists());
        assertTrue(!privateKey.exists());
        
        System.out.println("Defaultconstructor tested.. ok");
        
        try {
            new KeyGenerationHelper(512, "pub.key", "priv.key").generateAndStoreKeys();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        publicKey = new File("pub.key");
        privateKey = new File ("priv.key");
        
        assertTrue(publicKey.exists());
        assertTrue(privateKey.exists());
        
        assertTrue(publicKey.length() > 0);
        assertTrue(privateKey.length() > 0);
        
        publicKey.delete();
        privateKey.delete();
        
        assertTrue(!publicKey.exists());
        assertTrue(!privateKey.exists());
        
        System.out.println("Advancedconstructor tested.. ok");
    }
    
    
    @Test
    public void shouldGenerateKeyPairAndLoadThem(){
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        File filePublicKey = new File("public.key");
        File filePrivateKey = new File ("private.key");
        
        try {
            new KeyGenerationHelper().generateAndStoreKeys();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            publicKey = KeyGenerationHelper.readPublicKeyFromFile("public.key");
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            privateKey = KeyGenerationHelper.readPrivateKeyFromFile("private.key");
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assertTrue(publicKey != null);
        
        assertTrue(privateKey != null);
        
        assertTrue(publicKey.getAlgorithm().equals("RSA"));
        
        assertTrue(privateKey.getAlgorithm().equals("RSA"));
        
        System.out.println(privateKey.toString());
        System.out.println(publicKey.toString());
        
        filePrivateKey.delete();
        filePublicKey.delete();
        
        assertTrue(!filePrivateKey.exists());
        assertTrue(!filePublicKey.exists());
        
    }
    
}