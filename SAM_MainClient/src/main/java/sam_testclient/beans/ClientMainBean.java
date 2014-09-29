/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import sam_testclient.dao.DataAccess;
import sam_testclient.entities.Buddy;
import sam_testclient.entities.MediaFile;
import sam_testclient.entities.MemberSettings;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumMessageStatus;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class ClientMainBean {

    private static Logger logger;
    private List<Buddy> buddyList;
    private Map<Integer, String> buddy_statusMap;
    private MediaFile lastFile;
    
    private Map<Integer, Integer> buddyAmountOfMessages;

    private Map<Integer, List<Message>> unreadMessagesAtTab;

    private MemberSettings settings;
    
    private Buddy activeBuddy;

    private ClientMainBean() {
        logger = Logger.getLogger(ClientMainBean.class);
        this.buddyList = DataAccess.getAllBuddies();
        this.buddyAmountOfMessages = new ConcurrentHashMap<>();
        this.buddy_statusMap = new HashMap<>();
        this.unreadMessagesAtTab = new ConcurrentHashMap<>();
    }

    /**
     * Singleton- Pattern [@TODO: Maybe it should be replaced by CDI]
     */
    private static ClientMainBean instance = null;

    public static ClientMainBean getInstance() {
        if (instance == null) {
            instance = new ClientMainBean();
            logger.info(Utilities.getLogTime() + " Singleton instantiated successfully");
        }
        return instance;
    }

    /**
     * Adds an incoming message to the unreadlist, if it is needed.
     *
     * @param tabId
     * @param m
     */
    public void addMessageToUnreadList(int tabId, Message m) {
        if (!this.unreadMessagesAtTab.containsKey(tabId)) {
            this.unreadMessagesAtTab.put(tabId, new ArrayList<Message>());
        }
        unreadMessagesAtTab.get(tabId).add(m);
        logger.debug("Message added to unread-list");
    }

    public void removeMessagesFromUnreadList(int tabId) {
        for (Message m : unreadMessagesAtTab.get(tabId)) {
            m.setMessageStatus(EnumMessageStatus.READ);
            DataAccess.updateMessage(m); // !!!!!!!
        }
        unreadMessagesAtTab.get(tabId).clear();
        logger.debug("UnreadList cleared");
    }

    /**
     * Inits the List for unread messages.
     *
     * @param tabId
     */
    public void initUnreadList(int tabId) {
        this.unreadMessagesAtTab.put(tabId, new ArrayList<Message>());
    }

    public Map<Integer, List<Message>> getUnreadMessagesAtTabMap() {
        return this.unreadMessagesAtTab;
    }

    public List<Buddy> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(List<Buddy> buddyList) {
        this.buddyList = buddyList;
    }

    public Map<Integer, String> getBuddy_statusList() {
        return buddy_statusMap;
    }

    public void setBuddy_statusList(Map<Integer, String> buddy_statusList) {
        this.buddy_statusMap = buddy_statusList;
    }

    public MediaFile getLastFile() {
        return lastFile;
    }

    public void setLastFile(MediaFile lastFile) {
        logger.info(Utilities.getLogTime() + " File is saved in buffer");
        this.lastFile = lastFile;
    }

    public MemberSettings getSettings() {
        return this.settings;
    }

    public void setSettings(MemberSettings settings) {
        this.settings = settings;
    }

    public Buddy getActiveBuddy() {
        return activeBuddy;
    }

    public void setActiveBuddy(Buddy activeBuddy) {
        this.activeBuddy = activeBuddy;
    }

    public Map<Integer, Integer> getBuddyAmountOfMessages() {
        return buddyAmountOfMessages;
    }

    public void setBuddyAmountOfMessages(Map<Integer, Integer> buddyAmountOfMessages) {
        this.buddyAmountOfMessages = buddyAmountOfMessages;
    }
    
    public void increaseAmountOfMessages(int buddyId){
        int next = buddyAmountOfMessages.get(buddyId)+1;
        this.buddyAmountOfMessages.put(buddyId, next);
        System.out.println(">>>>>>" + buddyAmountOfMessages.get(buddyId));
    }
    
    //IMPR JAVA 8!!!!!!!

    public String getBuddynameById(String id) {
        for (Buddy b : buddyList) {
            if (b.getInternalID().equals(id)) {
                return b.getBuddyName();
            }
        }
        return null;
    }

    public Buddy getBuddyById(String id) {
        for (Buddy b : buddyList) {
            if (b.getInternalID().equals(id)) {
                return b;
            }
        }
        return null;
    }
    
    public Buddy getBuddyByName(String name) {
        for (Buddy b : buddyList) {
            if (b.getBuddyName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    public String getIdFromBuddy(String name) {
        String id = "-1";
        for (Buddy b : buddyList) {
            if (name.equals(b.getBuddyName())) {
                id = b.getInternalID();
                break;
            }
        }
        return id;
    }

}
