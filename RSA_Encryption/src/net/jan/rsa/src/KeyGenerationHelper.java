/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.rsa.src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * This Class manages the generation, the storing and the reading of keys.
 * Every key has to be serialized with his special settings:
 * <b>modules</b> and the <b>exponent</b>.
 * @author Jan Horak
 */
public class KeyGenerationHelper {

    
    
    public static final String ALGORITHM = "RSA";
    private static int keySize = 0;
    private static String publicKeyPath = "unknown";
    private static String privateKeyPath = "unknown";
    
    /**
     * Common Constructor
     * It defines the defaultsettings:
     * <li>   Keysize = 2048</li>
     * <li>   Path to name of private Key = \private.key </li>
     * <li>   Path to name of public Key = \public.key </li>
     */
    public KeyGenerationHelper(){
       keySize = 2048;
       privateKeyPath = "private.key";
       publicKeyPath = "public.key";
    }
    
    /**
     * Overloaded Constructor
     * It defines other values for keySize, path of private and public Key
     * @param keySize gives size of the key
     * @param pubKeyPath gives path and name of the public key
     * @param privKeyPath gives path and name of the private key
     */
    public KeyGenerationHelper(int keySizeInput, String pubKeyPath, String privKeyPath){
        keySize = keySizeInput;
        privateKeyPath = privKeyPath;
        publicKeyPath = pubKeyPath;
    }
    
    
    /**
     * This static method generates and stores the public and private key
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException 
     */
    public void generateAndStoreKeys() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(keySize);
        final KeyPair keyPair = keyGen.generateKeyPair();

        KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
        RSAPublicKeySpec publicKey = fact.getKeySpec(keyPair.getPublic(),
                RSAPublicKeySpec.class);
        RSAPrivateKeySpec privateKey = fact.getKeySpec(keyPair.getPrivate(),
                RSAPrivateKeySpec.class);
        
        saveKeyAndProperties(publicKeyPath, publicKey.getModulus(), publicKey.getPublicExponent());
        saveKeyAndProperties(privateKeyPath, privateKey.getModulus(), privateKey.getPrivateExponent());
    }

    private static void saveKeyAndProperties(String fileName,
            BigInteger modules, BigInteger exponent) throws IOException {
        ObjectOutputStream objectOutStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            objectOutStream.writeObject(modules);
            objectOutStream.writeObject(exponent);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            objectOutStream.close();
        }
    }
    
    public static PublicKey readPublicKeyFromFile(String keyFileName) throws IOException {
        InputStream in =
                new FileInputStream(keyFileName);
        ObjectInputStream oInputStream =
                new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger modules = (BigInteger) oInputStream.readObject();
            BigInteger exponents = (BigInteger) oInputStream.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modules, exponents);
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = factory.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            oInputStream.close();
        }
    }
    
    public static PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
        InputStream in =
                new FileInputStream(keyFileName);
        ObjectInputStream oInputStream =
                new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger modules = (BigInteger) oInputStream.readObject();
            BigInteger exponents = (BigInteger) oInputStream.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modules, exponents);
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            oInputStream.close();
        }
    }
}
