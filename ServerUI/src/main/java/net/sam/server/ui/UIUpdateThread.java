/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.ui;

import javax.inject.Inject;
import javax.swing.JList;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.manager.DataAccess;
import net.sam.server.services.ContainerService;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class UIUpdateThread extends Thread {

    private JList ui_userList;

    private Logger logger;
    
    public UIUpdateThread(JList loggedInList) {
        logger = Logger.getLogger(UIUpdateThread.class);
        this.ui_userList = loggedInList;
    }

    @Override
    public void run() {
        logger.info(Utilities.getLogTime()+" UI- updateThread is started...");
        while (true) {
            
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                logger.error(Utilities.getLogTime()+" UIThread is interrupted!");
            }
        }
    }


}
