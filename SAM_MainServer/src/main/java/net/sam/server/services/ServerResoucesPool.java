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
            writeable = false,
            kindOfResource = FileResource.Type.IMAGE)
    private static ImageIcon androidLogo;

    @FileResource(path = "resources/graphics/simpleLogoSAM.png",
            writeable = false,
            kindOfResource = FileResource.Type.IMAGE)
    private static ImageIcon samLogo;

    @FileResource(path = "resources/properties/server.properties",
            writeable = true,
            kindOfResource = FileResource.Type.PROPERTY)
    private static Properties serverProperties;

    @FileResource(path = "resources/properties/log4j.properties",
            writeable = false,
            kindOfResource = FileResource.Type.PROPERTY)
    private static Properties log4jProperties;

    @FileResource(path = "resources/logs/Server.log",
            writeable = true,
            kindOfResource = FileResource.Type.TEXTFILE)
    private static String serverLog;

}
