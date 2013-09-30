/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.aes.keygenhelper;

import java.io.File;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class KeyGenerationTest {
    
    
    @Test
    public void shouldGenerateSomeKeys(){
        File file = new File("secret.key");
        KeyGenerationManager keyManager = new KeyGenerationManager();
        
        keyManager.generateAndStoreKey();
        
        assertTrue(file.exists());
        
        assertNotNull(file);
        
        String testString = "test";
        
        String encryptedString = new Encryption().returnEncryptedString(testString, null);
        
        assertNotNull("Encrypted String is null",encryptedString);
        
        assertTrue("Decrypted and original String are not equals",testString.equals(new Decryption().returnDecryptedString(encryptedString, null)));
        
        file.delete();
    }
    
    
}