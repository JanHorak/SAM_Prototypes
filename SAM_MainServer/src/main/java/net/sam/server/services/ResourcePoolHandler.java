/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.services;

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
import javax.swing.ImageIcon;
import net.sam.server.annotations.FileResource;
import net.sam.server.exceptions.MethodNotAllowedForResourceType;
import net.sam.server.exceptions.ResourcesAlreadyLoadedException;
import net.sam.server.utilities.Utilities;
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
 * @version 1.3
 */
public class ResourcePoolHandler {

    private static boolean isLoaded = false;

    private static Logger logger;

    private static Class<?> associatedPool;

    /**
     * Common constructor for {@link ResourcePoolHandler}. Used if more than one
     * Resource-Pool is needed. If the constructor is called, the calling of 
     * {@link #loadFileResources(java.lang.Class) } is not needed and not
     * allowed.
     *
     * @param resourcesPoolClass
     */
    public ResourcePoolHandler(Class<?> resourcesPoolClass) {
        loadFields(resourcesPoolClass);
    }

    /**
     * If there is only one {@link ResourcePoolHandler} this method should be
     * called for the initial setup of the Resouces- Pool.
     *
     * @param resourcesPoolClass
     */
    public static void loadFileResources(Class<?> resourcesPoolClass) {
        if (!isLoaded()) {
            isLoaded = true;
            logger = Logger.getLogger(ResourcePoolHandler.class);
            associatedPool = resourcesPoolClass;
            for (Field field : loadFields(resourcesPoolClass)) {
                updateField(field);
            }
        } else {
            throw new ResourcesAlreadyLoadedException("The Resources are already loaded");
        }
    }

    private static Field[] loadFields(Class<?> resourcesPoolClass) {
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
    public static <T extends Object> T getResource(String fieldName) {
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
        // Default- values
        String path = "";
        boolean isWriteable = false;
        
        // Getting Metadata
        if (field.isAnnotationPresent(FileResource.class)) {
            path = getPathFromFileResource(field);
            isWriteable = getIsWriteableFromFileResource(field);

            logger.info("Field (re)loaded: Field: " + field.getName()
                    + " path: " + getPathFromFileResource(field)
                    + " writeable: " + getIsWriteableFromFileResource(field)
                    + " Type: " + field.getType().toString());

            field.setAccessible(true);
        }

        // Instantiate
        // Properties- loading
        if (field.getType() == Properties.class) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(new File(path)));
            } catch (FileNotFoundException ex) {
                logger.error(Utilities.getLogTime() + " FileNotFound: " + ex);
            } catch (IOException ex) {
                logger.error(Utilities.getLogTime() + " IOException " + ex);
            }
            try {
                field.set(field.getName(), properties);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.error(Utilities.getLogTime() + " IllegalArgumentException " + ex);
            }
        }

        // ImageIcon
        if (field.getType()== ImageIcon.class) {
            ImageIcon icon = new ImageIcon(getPathFromFileResource(field));
            try {
                field.set(field.getName(), icon);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.error(Utilities.getLogTime() + " IllegalArgumentException " + ex);
            }
        }

        // TextFile
        if (field.getType() == String.class && getTypeOfFileResource(field) == FileResource.Type.TEXTFILE) {
            String content = TextFileHelper.getContentOfTextFile(field);
            try {
                field.set(field.getName(), content);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.error(Utilities.getLogTime() + " IllegalArgumentException " + ex);
            }
        }

        if (getTypeOfFileResource(field) == FileResource.Type.XMLFILE) {
            throw new UnsupportedOperationException("XML is not supported yet");
        }

        if (field.getType() == File.class) {
            File file = new File(getPathFromFileResource(field));
            try {
                field.set(field.getName(), file);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                
            }

        }

    }

    // Getter for Annotation- Attributes
    private static String getPathFromFileResource(Field field) {
        FileResource myAnn = field.getAnnotation(FileResource.class);
        return myAnn.path();
    }

    private static boolean getIsWriteableFromFileResource(Field field) {
        FileResource myAnn = field.getAnnotation(FileResource.class);
        return myAnn.writeable();
    }

    private static FileResource.Type getTypeOfFileResource(Field field) {
        FileResource myAnn = field.getAnnotation(FileResource.class);
        return myAnn.kindOfResource();
    }

    private static Field getFieldByName(String name) {
        Field field = null;

        try {
            field = associatedPool.getDeclaredField(name);
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

        private static String getContentOfTextFile(Field field) {
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
