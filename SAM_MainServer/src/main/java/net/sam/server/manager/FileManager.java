/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.manager;

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
import java.nio.file.Path;
import java.util.Properties;
import net.sam.server.entities.MediaStorage;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;


/**
 *
 * @author janhorak
 */
public class FileManager {
    
    private static Logger logger;

    /**
     * Initializes the Logger
     */
    private static void initLogger(){
        logger = Logger.getLogger(FileManager.class);
    }
    
    public static void serialize(Object ob) {
        initLogger();
        logger.info(Utilities.getLogTime()+ " UI loaded successfully");
        
        FileOutputStream foutStream = null;
        try {
            foutStream = new FileOutputStream("data.bin");
        } catch (FileNotFoundException ex) {
            logger.error(Utilities.getLogTime()+" File not found exception: "+ex);
        }

        ObjectOutputStream obOutStream = null;
        try {
            obOutStream = new ObjectOutputStream(foutStream);
        } catch (IOException ex) {
            logger.error(Utilities.getLogTime()+ " Exception: "+ex);
        }
        try {
            obOutStream.writeObject(ob);
        } catch (IOException ex) {
            logger.error(Utilities.getLogTime()+ " Exception: "+ex);
        }
    }

    public static Object deserialize(Path path) {
        initLogger();
        FileInputStream finStream = null;
        try {
            finStream = new FileInputStream(path.toString());
        } catch (FileNotFoundException ex) {
            logger.error(Utilities.getLogTime()+ " Exception: "+ex);
        }

        ObjectInputStream obj_in = null;
        try {
            obj_in = new ObjectInputStream(finStream);
        } catch (IOException ex) {
           logger.error(Utilities.getLogTime()+ " Exception: "+ex);
        }

        Object obj = null;
        try {
            obj = obj_in.readObject();
        } catch (IOException ex) {
            logger.error(Utilities.getLogTime()+ " Exception: "+ex);
        } catch (ClassNotFoundException ex) {
            logger.error(Utilities.getLogTime()+ " Exception: "+ex);
        }

        if (obj instanceof MediaStorage) {
            MediaStorage media = (MediaStorage) obj;
            return media;
        }
        return null;
    }
    
    /**
     * Initializes and returns the properties.
     * @param path
     * @return Properties in path
     */
    public static Properties initProperties(String path){
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
        logger.info(Utilities.getLogTime() + " Properties are loaded");
        return property;
    }
    
    /**
     * Storing the new value at the key in the propertiesfile.
     * @param path
     * @param key
     * @param value 
     */
    public static void storeValueInPropertiesFile(String path, String key, String value){
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
    

}
