/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.ui;

import javax.swing.JList;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.manager.DataAccess;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class UIUpdateThread extends Thread {

    private JList ui_userList;

    private ServerMainBean sb;

    private Logger logger;
    
    public UIUpdateThread(JList loggedInList) {
        BasicConfigurator.configure();
        logger = Logger.getLogger(UIUpdateThread.class);
        this.ui_userList = loggedInList;
        sb = ServerMainBean.getInstance();
        sb.setRegisteredMembers(DataAccess.getAllRegisteredMembers());
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
