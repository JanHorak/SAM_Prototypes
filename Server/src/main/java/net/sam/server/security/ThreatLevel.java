/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.security;

import net.sam.server.interfaces.Level;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class ThreatLevel implements Level {

    private int currentThreatLevel;

    private Logger logger;

    public ThreatLevel() {
        this.currentThreatLevel = LOW;
        logger = Logger.getLogger(ThreatLevel.class);
    }

    @Override
    public void increaseLevel() {
        if (currentThreatLevel <= MIDDLE) {
            currentThreatLevel++;
            logger.warn(Utilities.getLogTime() + " Threatlevel is increased. Now:" + currentThreatLevel);
        }
    }

    @Override
    public void decreaseLevel() {
        if (currentThreatLevel > LOW) {
            currentThreatLevel--;
            logger.warn(Utilities.getLogTime() + " Threatlevel is decreased. Now:" + currentThreatLevel);
        }
    }

    @Override
    public int getCurrentLevel() {
        return this.currentThreatLevel;
    }

    public boolean isCurrentLowerThanHigh() {
        return this.currentThreatLevel < HIGH;
    }

    public boolean isCurrentLowerThanMiddle() {
        return this.currentThreatLevel < MIDDLE;
    }

    public boolean isCurrentLow() {
        return this.currentThreatLevel == LOW;
    }

}
