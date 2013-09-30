package net.jan.aes.keygenerationmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author janhorak
 */
public class KeyGenerationManager {

    private final byte[] alphaLib = new byte[]{'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f', 'G', 'g',
        'H', 'h', 'I', 'i', 'J', 'j', 'K', 'k', 'L', 'l', 'M', 'm', 'N', 'n',
        'O', 'o', 'P', 'p', 'Q', 'q', 'R', 'r', 'S', 's', 'T', 't', 'U', 'u',
        'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Z', 'z'};
    private final byte[] numericLib = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private final byte[] specialLib = new byte[]{'!', '$', '%', '&', '/', '(', ')', '=', '?', '*'};
    private int KEYSIZE_BYTE = 0;
    private int amountAlpha = 0;
    private int amountNumeric = 0;
    private int amountSpecial = 0;

    /**
     * This Constructor defines a specific key. The sum of <b>amountAlpha</b>,
     * <b>amountNumeric</b> and <b>amountSpecial</b>
     * has to be the <b>keySize</b>. The <b>keySize</b> has to be a 128 Bit
     * -Key. It is defined in the AES- Standard. The <b>keySize</b> is a Byte-
     * Size, so it's not allowed to use a value over 16.
     * <br/>
     * <b>Example</b>
     * <br/>
     * Valid Constructor: Size = 16, amountAlpha = 10, amountNumeric = 3,
     * amountSpecial = 3 Valid Constructor, too: Size = 12, amountAlpha = 6,
     * amountNumeric = 3, amountSpecial = 3
     * <br/>
     * Invalid Constructor: Size = 32, amountAlpha = 20, amountNumeric = 10,
     * amountSpecial = 2
     * <br/>
     *
     * @param keySize - defines the size of the key. The AES- Standard defines
     * the value to 128Bit
     * @param amountAlpha - defines the size of the alphabetic quota of the
     * <b>keySize</b>
     * @param amountNumeric- defines the size of the numeric quota of the
     * <b>keySize</b>
     * @param amountSpecial - defines the size of the special characters quota
     * of the <b>keySize</b>
     */
    public KeyGenerationManager(int keySize, int amountAlpha, int amountNumeric, int amountSpecial) throws Exception {
        this.KEYSIZE_BYTE = keySize;
        this.amountAlpha = amountAlpha;
        this.amountNumeric = amountNumeric;
        this.amountSpecial = amountSpecial;
        if ((amountAlpha + amountNumeric + amountSpecial) != KEYSIZE_BYTE || KEYSIZE_BYTE <= 0 || amountAlpha <= 0
                || amountNumeric <= 0 || amountSpecial <= 0 || KEYSIZE_BYTE >= 17) {
            throw new RuntimeException("Failed to build Object");
        }
    }

    /**
     * This Constructor defines a default key. The sum of <b>amountAlpha</b>,
     * <b>amountNumeric</b> and <b>amountSpecial</b>
     * has to be the <b>keySize</b>. The <b>keySize</b> has to be a 128 Bit
     * -Key. It is defined in the AES- Standard. The <b>keySize</b> is a Byte-
     * Size, so it's not allowed to use a value over 16. The default key will
     * have the following settings:
     * <li>KeySize = 16</li>
     * <li>Size of the alphabetic quota = 8</li>
     * <li>Size of the numeric quota = 4</li>
     * <li>Size of the special characters quota = 4</li>
     */
    public KeyGenerationManager() {
        this.KEYSIZE_BYTE = 16;
        this.amountAlpha = 8;
        this.amountNumeric = 4;
        this.amountSpecial = 4;

    }

    /**
     * This method generates the random key.
     *
     * @return - Returns the generated random key
     */
    private byte[] generateRandomKey() {
        boolean keyComplete = false;
        int counter = 0;
        byte[] generatedKey = new byte[KEYSIZE_BYTE];
        while (!keyComplete) {
            if (amountAlpha != 0) {
                generatedKey[counter] += alphaLib[returnRandomNumber(alphaLib.length, 0)];
                counter++;
                amountAlpha--;
            }
            if (amountNumeric != 0) {
                generatedKey[counter] += numericLib[returnRandomNumber(numericLib.length, 0)];
                counter++;
                amountNumeric--;
            }
            if (amountSpecial != 0) {
                generatedKey[counter] += specialLib[returnRandomNumber(specialLib.length, 0)];
                counter++;
                amountSpecial--;
            }
            if (counter == KEYSIZE_BYTE) {
                keyComplete = true;
            }
        }
        return generatedKey;
    }

    private int returnRandomNumber(int max, int min) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * This method generates the secret key and stores it. Every call generates
     * and stores a new Key.
     */
    public void generateAndStoreKey() {
        generateAndStore("secret.key");
    }

    /**
     * This method generates the secret key and stores it. Every call generates
     * and stores a new Key.
     */
    public void generateAndStoreKey(String pathOfKey) {
        if (pathOfKey.isEmpty() || pathOfKey == null) {
            throw new RuntimeException("Path is not valid");
        }
        else {
            generateAndStore(pathOfKey);
        }
    }

    private void generateAndStore(String keyPath) {
        ObjectOutputStream obOutStream = null;
        try {
            Key key = new SecretKeySpec(generateRandomKey(), "AES");
            File keyFile = new File(keyPath);
            obOutStream = new ObjectOutputStream(new FileOutputStream(keyFile));
            obOutStream.writeObject(key);
            obOutStream.close();
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                obOutStream.close();
            } catch (IOException ex) {
                Logger.getLogger(KeyGenerationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method returns the secret key
     * @return - returns the generated secret key
     * @throws RuntimeException if the Path is empty or the File does not
     * exist
     */
    public Key readSecretKey(String pathOfKey) {
        if ( !pathOfKey.isEmpty() && new File(pathOfKey).exists() ){
            return readSecret(pathOfKey);
        }
        else {
            throw new RuntimeException("Keypath is not valid");
        }
    }
    
    /**
     * This method returns the secret key
     * @return - returns the generated secret key from the default-path
     */
    public Key readSecretKey() {
        return readSecret("secret.key");
    }
    
    private Key readSecret(String pathOfKey){
        Key key = null;
        try {
            File file = new File(pathOfKey);
            ObjectInputStream obInStream = new ObjectInputStream(new FileInputStream(file));
            key = (Key) obInStream.readObject();
            obInStream.close();
        } catch (IOException ex) {
            Logger.getLogger(KeyGenerationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KeyGenerationManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return key;
    }
}
