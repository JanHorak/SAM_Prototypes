/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.aes.advancedAESFunctions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author janhorak
 */
public class AdvancedFunctions {

    private final String dummyFile = "dummy.txt";
    private final String outputFile_enc = "test_enc.txt";
    private final String outputFile_dec = "test_dec.txt";
    private String secKeyPath = null;
    
    
    @Before
    public void init() {
        KeyGenerationManager keyGen = new KeyGenerationManager();
        keyGen.generateAndStoreKey();
        createADummyFile();
    }
    
    @After
    public void cleanUp(){
        File file = new File(dummyFile);
        file.delete();
        file = new File(outputFile_enc);
        file.delete();
        file = new File(outputFile_dec);
        file.delete();
        if ( secKeyPath == null ){
            secKeyPath = "secret.key";
        }
        file = new File(secKeyPath);
        file.delete();
    }

    @Test
    public void shouldEncryptAFile() {
        File encryptedFile = new File(outputFile_enc);
        File decryptedFile = new File(outputFile_dec);
        encryptedFile = new Encryption().returnEncryptedFile(dummyFile, encryptedFile.getAbsolutePath(), secKeyPath);
        decryptedFile = new Decryption().returnDecryptedFile(outputFile_enc, outputFile_dec, secKeyPath);
    }

    private void createADummyFile() {
        String dummyString = "lalalala - i'm a dummy- Text for the file\n and "
                + "i'm singing... lalalalaaaa. !§$%&/";
        File dummyFiletmp = new File(dummyFile);
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(dummyFiletmp));
        } catch (IOException ex) {
            Logger.getLogger(AdvancedFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            br.write(dummyString);
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(AdvancedFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}