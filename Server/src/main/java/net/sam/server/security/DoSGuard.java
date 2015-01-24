/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.security;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.services.ContainerService;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class DoSGuard extends Guard {

    private ServerMainBean smb;
    private boolean live;

    private Map<InetAddress, ThreatLevel> threatLevelPerSocket;
    private static Logger logger;

    private DoSGuard() {
        logger = Logger.getLogger(DoSGuard.class);
        smb = ServerMainBean.getInstance();
        this.threatLevelPerSocket = new ConcurrentHashMap<>();
        this.live = true;
    }

    /**
     * Singleton- Pattern [@TODO: Maybe it should be replaced by CDI]
     */
    private static DoSGuard instance = null;

    public static DoSGuard getInstance() {
        if (instance == null) {
            instance = new DoSGuard();
            logger.info(Utilities.getLogTime() + " Singleton instantiated successfully");
        }
        return instance;
    }

    public boolean isConnectedSocketHarmless(InetAddress address) {
        boolean isOk = true;
        if (!threatLevelPerSocket.containsKey(address)) {
            threatLevelPerSocket.put(address, new ThreatLevel());
            return isOk;
        }
        if (threatLevelPerSocket.get(address).isCurrentLowerThanHigh()) {
            return isOk;
        } else {
            return !isOk;
        }
    }

    public void increaseLevelForSocket(InetAddress address) {
        threatLevelPerSocket.get(address).increaseLevel();
    }

    protected void decreaseLevelForAllSockets() {
        if (!this.threatLevelPerSocket.isEmpty()) {
            for (ThreatLevel level : threatLevelPerSocket.values()) {
                level.decreaseLevel();
            }
        }

    }

}
