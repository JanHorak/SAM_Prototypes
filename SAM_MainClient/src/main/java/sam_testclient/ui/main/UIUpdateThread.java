/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.ui.main;

import javax.swing.ImageIcon;
import javax.swing.JList;
import org.apache.log4j.Logger;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class UIUpdateThread extends Thread {

    private JList ui_userList;

    private Logger logger;
    
    private MainUI ui;
    
    public UIUpdateThread(MainUI ui) {
        logger = Logger.getLogger(UIUpdateThread.class);
        this.ui = ui;
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
