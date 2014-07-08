/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import sam_testclient.entities.MemberSettings;
import sam_testclient.exceptions.InvalidSettingsException;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class FileManager {

    private static org.apache.log4j.Logger logger;

    /**
     * Initializes the Logger
     */
    private static void initLogger() {
        logger = org.apache.log4j.Logger.getLogger(FileManager.class);
    }

    public static void serialize(Object ob, String path) {
        FileOutputStream f_out = null;
        try {
            f_out = new FileOutputStream(path);

            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            obj_out.writeObject(ob);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f_out.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Object deserialize(String path) {
        FileInputStream f_in = null;
        Object result = null;
        try {
            FileInputStream fis = null;

            f_in = new FileInputStream(path);

            ObjectInputStream obj_in
                    = new ObjectInputStream(f_in);

            result = obj_in.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f_in.close();
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public static String getValueOfPropertyByKey(String path, String key) {
        System.out.println(path + " " + key);
        return initProperties(path).getProperty(key);
    }

    /**
     * Storing the new value at the key in the propertiesfile.
     *
     * @param path
     * @param key
     * @param value
     */
    public static void storeValueInPropertiesFile(String path, String key, String value) {
        initLogger();
        Properties props = initProperties(path);
        value = new String(value.getBytes(), Charset.forName("UTF-8"));
        props.setProperty(key, value);
        try {
            props.store(new FileOutputStream(new File(path)), "");
        } catch (IOException ex) {
            logger.error(Utilities.getLogTime() + " Cant storing Data");
        }
    }

    public static byte[] returnBytesOfFile(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            logger.error(Utilities.getLogTime() + " Error in IO: " + ex);
        }
        return null;
    }

    /**
     * Initializes and returns the properties.
     *
     * @param path
     * @return Properties in path
     */
    public static Properties initProperties(String path) {
        initLogger();
        Reader r = null;
        Properties property = null;
        try {
            property = new Properties();
            r = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
            property.load(r);
        } catch (UnsupportedEncodingException ex) {
            logger.error(Utilities.getLogTime() + " EncodingError");
        } catch (FileNotFoundException ex) {
            logger.error(Utilities.getLogTime() + " FileNotFound");
        } catch (IOException ex) {
            logger.error(Utilities.getLogTime() + " IO");
        }
        logger.info(Utilities.getLogTime() + " Properties are accessed");
        return property;
    }
    
    /**
     * Mapping- method for the settings of the client.
     * 
     * @param path
     * @return Settings of a Member
     * @throws InvalidSettingsException
     */
    public static MemberSettings getMemberSettings(String path) throws InvalidSettingsException{
        MemberSettings settings = new MemberSettings();
        Properties props = initProperties(path);
        
        settings.setName(props.getProperty("announcementName"));
        settings.setAllowWebClients(Boolean.valueOf(props.getProperty("allowWebClientRequests")));
        settings.setSaveLocaleHistory(Boolean.valueOf(props.getProperty("saveLocaleHistory")));
        settings.setAutoDownload(MemberSettings.AutoDownload.valueOf(props.getProperty("autodownload")));
        settings.setRecreationType(MemberSettings.RecreationEnum.valueOf(props.getProperty("recreationType")));
        settings.setValidFor(MemberSettings.ValidFor.valueOf(props.getProperty("validFor")));
        settings.setRecreationDays(Integer.parseInt(props.getProperty("recreationDays")));
        settings.setAvatarPath(props.getProperty("avatar"));
        settings.setHistBorder(props.getProperty("histBorder"));
        
        if (!ValidationManager.isValid(settings)){
            throw new InvalidSettingsException("Settings are not valid!", "54125");
        }
        
        return settings;
    }

    
}
