/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.beans;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import sam_testclient.entities.MediaFile;
import sam_testclient.sources.FileManager;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class ClientMainBean {
    
    private static Logger logger;
    private Map<Integer, String> buddyList;
    private Map<Integer, Boolean> buddy_statusList;
    private MediaFile lastFile;
    
    private ClientMainBean(){
        logger = Logger.getLogger(ClientMainBean.class);
        this.buddyList = (Map<Integer, String>) FileManager.deserialize("buddyList.data");
        this.buddy_statusList = new HashMap<Integer, Boolean>();
    }
    
    /**
     * Singleton- Pattern
     * [@TODO: Maybe it should be replaced by CDI]
     */
    private static ClientMainBean instance = null;
    
    public static ClientMainBean getInstance() {
        if (instance == null) {
            instance = new ClientMainBean();
            logger.info(Utilities.getLogTime()+" Singleton instantiated successfully");
        }
        return instance;
    }

    public Map<Integer, String> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(Map<Integer, String> buddyList) {
        this.buddyList = buddyList;
    }

    public Map<Integer, Boolean> getBuddy_statusList() {
        return buddy_statusList;
    }

    public void setBuddy_statusList(Map<Integer, Boolean> buddy_statusList) {
        this.buddy_statusList = buddy_statusList;
    }

    public MediaFile getLastFile() {
        return lastFile;
    }

    public void setLastFile(MediaFile lastFile) {
        logger.info(Utilities.getLogTime() + " File is saved in buffer");
        this.lastFile = lastFile;
    }
    
    
    
    
    
    
}
