/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.manager.DataAccess;
import net.sam.server.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class UIUpdateThread extends Thread {

    private JList ui_userList;

    private ServerMainBean sb;

    public UIUpdateThread(JList loggedInList) {
        this.ui_userList = loggedInList;
        sb = ServerMainBean.getInstance();
        sb.setRegisteredMembers(DataAccess.getAllRegisteredMembers());
    }

    @Override
    public void run() {
        System.out.println(Utilities.getLogTime() + " ServerUIThread is started");
        while (true) {

            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UIUpdateThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
