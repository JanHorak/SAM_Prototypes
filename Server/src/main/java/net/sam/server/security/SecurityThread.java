/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.security;

import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class SecurityThread extends Thread {

    private Logger logger;

    private DoSGuard dosGuard;

    public SecurityThread() {
        this.setDaemon(true);
        this.logger = Logger.getLogger(SecurityThread.class);
        this.dosGuard = DoSGuard.getInstance();
    }

    @Override
    public void run() {
        logger.info(Utilities.getLogTime() + " SecurityThread is started...");
        while (true) {
            try {
                sleep(60000);
            } catch (InterruptedException ex) {
                logger.error(Utilities.getLogTime() + " Error by sleeping (Interrupted): " + ex);
            }
            this.dosGuard.decreaseLevelForAllSockets();
        }

    }

}
