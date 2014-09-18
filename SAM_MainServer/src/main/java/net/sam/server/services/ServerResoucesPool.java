/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.services;

import net.sam.server.annotations.FileResource;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author Jan
 */
public abstract class ServerResoucesPool {

    @FileResource(path = "resources/graphics/AndroidLogo.png",
            writeable = false)
    private static ImageIcon androidLogo;

    @FileResource(path = "resources/graphics/simpleLogoSAM.png",
            writeable = false)
    private static ImageIcon samLogo;
    
    @FileResource(path = "resources/graphics/edit-file-icon_25x25.png",
            writeable = false)
    private static ImageIcon editConfig;
    
    @FileResource(path = "resources/graphics/File-Delete-icon_25x25.png",
            writeable = false)
    private static ImageIcon deleteConfig;
    
    @FileResource(path = "resources/graphics/new-file-icon_25x25.png",
            writeable = false)
    private static ImageIcon newConfig;
    
    @FileResource(path = "resources/graphics/ok_25x25.png",
            writeable = false,
            kindOfResource = FileResource.Type.IMAGE)
    private static ImageIcon validImage;
    
    @FileResource(path = "resources/graphics/wrong_25x25.png",
            writeable = false)
    private static ImageIcon invalidImage;

    @FileResource(path = "resources/properties/server.properties",
            writeable = true)
    private static Properties serverProperties;

    @FileResource(path = "resources/properties/log4j.properties",
            writeable = false)
    private static Properties log4jProperties;

    @FileResource(path = "resources/logs/Server.log",
            writeable = true,
            kindOfResource = FileResource.Type.TEXTFILE)
    private static String serverLog;

}
