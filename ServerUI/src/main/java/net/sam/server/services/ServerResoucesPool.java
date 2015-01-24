/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import javax.swing.ImageIcon;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import net.jan.poolhandler.resourcepoolhandler.Rules;
import net.jan.poolhandler.resourcepoolhandler.annotations.FileResource;
import net.jan.poolhandler.resourcepoolhandler.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author Jan
 */
public class ServerResoucesPool implements Rules {

    @FileResource(path = "src/main/resources/graphics/AndroidLogo.png",
            writeable = false)
    private static ImageIcon androidLogo;

    @FileResource(path = "src/main/resources/graphics/simpleLogoSAM.png",
            writeable = false)
    private static ImageIcon samLogo;

    @FileResource(path = "src/main/resources/graphics/edit-file-icon_25x25.png",
            writeable = false)
    private static ImageIcon editConfig;

    @FileResource(path = "src/main/resources/graphics/File-Delete-icon_25x25.png",
            writeable = false)
    private static ImageIcon deleteConfig;

    @FileResource(path = "src/main/resources/graphics/new-file-icon_25x25.png",
            writeable = false)
    private static ImageIcon newConfig;

    @FileResource(path = "src/main/resources/graphics/ok_25x25.png",
            writeable = false)
    private static ImageIcon validImage;

    @FileResource(path = "src/main/resources/graphics/wrong_25x25.png",
            writeable = false)
    private static ImageIcon invalidImage;

    @FileResource(path = "src/main/resources/properties/server.properties",
            writeable = true)
    private static Properties serverProperties;

    @FileResource(path = "src/main/resources/properties/log4j.properties",
            writeable = false)
    private static Properties log4jProperties;

    @FileResource(path = "src/main/resources/logs/Server.log",
            writeable = true,
            kindOfResource = FileResource.Type.TEXTFILE)
    private static String serverLog;

    // ----------- UpdateRules --------------------------
    @Override
    public void updateField(Field field) {
        Logger logger = Logger.getLogger(ServerResoucesPool.class);
        // Default- values
        String path = "";
        boolean isWriteable = false;

        // Getting Metadata
        if (field.isAnnotationPresent(FileResource.class)) {
            path = ResourcePoolHandler.getPathFromFileResource(field);
            isWriteable = ResourcePoolHandler.getIsWriteableFromFileResource(field);

            logger.info("Field (re)loaded: Field: " + field.getName()
                    + " path: " + ResourcePoolHandler.getPathFromFileResource(field)
                    + " writeable: " + ResourcePoolHandler.getIsWriteableFromFileResource(field)
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
        if (field.getType() == ImageIcon.class) {
            ImageIcon icon = new ImageIcon(ResourcePoolHandler.getPathFromFileResource(field));
            try {
                field.set(field.getName(), icon);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.error(Utilities.getLogTime() + " IllegalArgumentException " + ex);
            }
        }

        // TextFile
        if (field.getType() == String.class && ResourcePoolHandler.getTypeOfFileResource(field) == FileResource.Type.TEXTFILE) {
            String content = ResourcePoolHandler.TextFileHelper.getContentOfTextFile(field);
            try {
                field.set(field.getName(), content);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                logger.error(Utilities.getLogTime() + " IllegalArgumentException " + ex);
            }
        }

        if (ResourcePoolHandler.getTypeOfFileResource(field) == FileResource.Type.XMLFILE) {
            throw new UnsupportedOperationException("XML is not supported yet");
        }

        if (field.getType() == File.class) {
            File file = new File(ResourcePoolHandler.getPathFromFileResource(field));
            try {
                field.set(field.getName(), file);
            } catch (IllegalArgumentException | IllegalAccessException ex) {

            }

        }
    }

}
