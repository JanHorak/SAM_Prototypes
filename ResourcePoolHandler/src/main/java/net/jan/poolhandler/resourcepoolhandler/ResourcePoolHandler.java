/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.poolhandler.resourcepoolhandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import net.jan.poolhandler.resourcepoolhandler.annotations.FileResource;
import net.jan.poolhandler.resourcepoolhandler.exceptions.MethodNotAllowedForResourceType;
import net.jan.poolhandler.resourcepoolhandler.exceptions.ResourcesAlreadyLoadedException;
import net.jan.poolhandler.resourcepoolhandler.utilities.Utilities;
import org.apache.log4j.Logger;


/**
 * The {@link ResourcePoolHandler} is main- class for the management of the used
 * Resources.
 *
 * The {@link ResourcePoolHandler} provides a lot of methods and functions to
 * getting and modifying the resources which are defined in the passed class.
 *
 * It is possible to use one or more {@link ResourcePoolHandler} at the same
 * time. If it is only one {@link ResourcePoolHandler} needed the
 * {@link ResourcePoolHandler} can be used staticly. Else it is possible to pass
 * every instance of {@link ResourcePoolHandler} an own Pool- Class.
 *
 * The {@link ResourcePoolHandler} contains for the most kinds of
 * {@link FileResource} an inner abstract class for the management and the
 * access of the sources.
 *
 * @version 1.4
 */
public class ResourcePoolHandler {

    private static boolean isLoaded = false;

    private static Logger logger;
   
    private static Rules updateRules;
    
    private static Rules associatedPool;

    /**
     * Common constructor for {@link ResourcePoolHandler}. Used if more than one
     * Resource-Pool is needed. If the constructor is called, the calling of 
     * {@link #loadFileResources(java.lang.Class) } is not needed and not
     * allowed.
     *
     * @param resourcesPoolClass
     */
    public <T extends Rules> ResourcePoolHandler(Class<T> resourcesPoolClass, Rules rules) {
        updateRules = rules;
        loadFields(resourcesPoolClass);
    }

    /**
     * If there is only one {@link ResourcePoolHandler} this method should be
     * called for the initial setup of the Resouces- Pool.
     *
     * @param <T>
     * @param resourcesPoolClass
     */
    public static <T extends Rules> void loadFileResources(Class<T> resourcesPoolClass) {
        if (!isLoaded()) {
            Rules r = null;
            try {
                r = resourcesPoolClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(ResourcePoolHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            isLoaded = true;
            logger = Logger.getLogger(ResourcePoolHandler.class);
            associatedPool = r;
            for (Field field : loadFields(resourcesPoolClass)) {
                r.updateField(field);
            }
        } else {
            throw new ResourcesAlreadyLoadedException("The Resources are already loaded");
        }
    }

    private static <T extends Rules> Field[] loadFields(Class<T> resourcesPoolClass) {
        return resourcesPoolClass.getDeclaredFields();
    }

    /**
     * Returns an instance of the in the Pool defiend field by passing the
     * fieldname.
     *
     * For example it is defined in the Resourcepool a Properties- File:
     * <pre>@FileResource(path = "test.properties",
     * writeable = true,
     * kindOfResource = FileResource.Type.PROPERTY)
     * private static Properties propertiesFile;</pre>
     *
     * Will return the call of:
     *
     * <pre>ResourcePoolHandler.getResource("propertiesFile");</pre>
     *
     * The loaded instance of type {@link Properties}.
     *
     * @param <T>
     * @param fieldName - Name of field in the ResourcePool
     * @return Casted Object of Type T
     */
    public static <T> T getResource(String fieldName) {
        Field field = getFieldByName(fieldName);

        field.setAccessible(true);
        T result = null;
        try {
            result = (T) field.get(field.getName());
        } catch (IllegalArgumentException | IllegalAccessException ex) {

        }
        return result;
    }

    private static void updateField(Field field) {
        

    }

    // Getter for Annotation- Attributes
    public static String getPathFromFileResource(Field field) {
        FileResource myAnn = field.getAnnotation(FileResource.class);
        return myAnn.path();
    }

    public static boolean getIsWriteableFromFileResource(Field field) {
        FileResource myAnn = field.getAnnotation(FileResource.class);
        return myAnn.writeable();
    }

    public static FileResource.Type getTypeOfFileResource(Field field) {
        FileResource myAnn = field.getAnnotation(FileResource.class);
        return myAnn.kindOfResource();
    }

    public static Field getFieldByName(String name) {
        Field field = null;

        try {
            field = associatedPool.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException | SecurityException ex) {
            logger.error(Utilities.getLogTime() + " NoSuchFieldException " + ex);
        }

        return field;
    }

    public static String getPathOfResource(String fieldname) {
        return getPathFromFileResource(getFieldByName(fieldname));
    }

    public static void destroy() {
        isLoaded = false;
        associatedPool = null;
        logger.info(Utilities.getLogTime() + " ResourcePoolHandler is destroyed.");
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Helperclass for all methods of the Property- Type.
     *
     * Note: The values can get by using {@link #getResource(java.lang.String)}-
     * method, which returns the loaded Object.
     */
    public abstract static class PropertiesHelper {

        /**
         * Set and save a new value in the passed field.
         *
         * @param fieldName
         * @param key
         * @param value
         */
        public static void setValueInProperties(String fieldName, String key, String value) {
            Field field = getFieldByName(fieldName);
            if (field.getType() == Properties.class) {
                if (getIsWriteableFromFileResource(field)) {
                    Properties properties = getResource(fieldName);
                    if (!properties.get(key).equals(value)) {
                        properties.setProperty(key, value);
                        try {
                            properties.store(new FileOutputStream(new File(getPathFromFileResource(field))), "");
                        } catch (FileNotFoundException ex) {
                            logger.error(Utilities.getLogTime() + " FileNotFound " + ex);
                        } catch (IOException ex) {
                            logger.error(Utilities.getLogTime() + " IOException " + ex);
                        }
                        logger.info(Utilities.getLogTime() + "Update succeeded: " + key + " -> " + value + " in Field: " + fieldName);
                        updateField(field);
                    } else {
                        logger.info(Utilities.getLogTime() +"Update is not needed: Value is the same as stored");
                    }

                } else {
                    logger.error(Utilities.getLogTime() +  " "+ getPathFromFileResource(field) + " is not writeable!");
                }
            } else {
                throw new MethodNotAllowedForResourceType("Using of this Method is not allowed. FileResource needs to has Type: "
                        + MethodNotAllowedForResourceType.format(Properties.class));
            }

        }

        public static String getValueOfKey(String fieldName, String key){
            return ((Properties) getResource(fieldName)).getProperty(key);
        }
    }

    /**
     * Helperclass for all TextFiles
     */
    public abstract static class TextFileHelper {

        /**
         * Writes a content to the field which is passed by name.
         *
         * @param fieldName name of Field in ResourcePool
         * @param content content
         * @param append appending to file. If this is <pre>true</pre> the
         * content will be added at the end of the String. Else the content will
         * replace the whole file.
         */
        public static void writeContentToTextFile(String fieldName, String content, boolean append) {
            Field field = getFieldByName(fieldName);
            if (getTypeOfFileResource(field) == FileResource.Type.TEXTFILE) {
                if (getIsWriteableFromFileResource(field)) {
                    File file = new File(getPathFromFileResource(field));
                    FileWriter outFile = null;
                    try {
                        outFile = new FileWriter(file, append);
                    } catch (IOException ex) {
                        logger.error(Utilities.getLogTime() + " IOException " + ex);
                    }
                    PrintWriter out = null;
                    out = new PrintWriter(new BufferedWriter(outFile));

                    out.append(content);

                    out.flush();
                    out.close();
                    updateField(field);
                } else {
                    logger.error(Utilities.getLogTime() +  " "+ getPathFromFileResource(field) + " is not writeable!");
                }
            } else {
                throw new MethodNotAllowedForResourceType("Using of this Method is not allowed. FileResource needs to has Type: "
                        + MethodNotAllowedForResourceType.format(String.class));
            }

        }

        public static String getContentOfTextFile(Field field) {
            File file = new File(getPathFromFileResource(field));
            StringBuilder contents = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                String text = null;
                while ((text = reader.readLine()) != null) {
                    contents.append(text)
                            .append(System.getProperty(
                                            "line.separator"));
                }
            } catch (IOException e) {
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                }
            }
            return contents.toString();
        }
    }
}
