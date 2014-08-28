/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.services;

import sam_testclient.annotations.FileResource;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author Jan
 */
public abstract class ClientResoucesPool {

    @FileResource(path = "resources/graphics/simpleLogoSAM.png",
            writeable = false)
    private static ImageIcon samLogo;

    @FileResource(path = "resources/graphics/notice.gif",
            writeable = false)
    private static ImageIcon notice_gif;

    @FileResource(path = "resources/properties/client.properties",
            writeable = true)
    private static Properties clientProperties;

    @FileResource(path = "resources/properties/log4j.properties",
            writeable = false)
    private static Properties log4jProperties;

}
