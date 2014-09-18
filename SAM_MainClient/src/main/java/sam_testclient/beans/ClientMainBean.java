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
import sam_testclient.entities.MediaFile;
import sam_testclient.entities.MemberSettings;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.exceptions.InvalidSettingsException;
import sam_testclient.services.HistoricizationService;
import sam_testclient.sources.FileManager;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class ClientMainBean {

    private static Logger logger;
    private Map<Integer, String> buddyList;
    private Map<Integer, String> buddy_statusMap;
    private MediaFile lastFile;

    private Map<Integer, List<Message>> unreadMessagesAtTab;

    // Map with content: <BuddyName <MessageID, Message>>
    private Map<String, Map<String, Message>> currentHistoryMap;

    private MemberSettings settings;

    private ClientMainBean() {
        logger = Logger.getLogger(ClientMainBean.class);
        this.buddyList = (Map<Integer, String>) FileManager.deserialize("resources/buddyList.data");
        this.buddy_statusMap = new HashMap<>();
        this.currentHistoryMap = new ConcurrentHashMap<>();
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
        List<Message> ml = unreadMessagesAtTab.get(tabId);
        ml.add(m);
        logger.debug("Message added to unread-list");
    }

    public void removeMessagesFromUnreadList(int tabId) {
        this.unreadMessagesAtTab.get(tabId).clear();
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

    /**
     * Updates the status of an incoming message for the histService and the
     * message- status.
     *
     * @param m
     */
    public void updateMessageStatus(Message m) {
        for (String buddy : currentHistoryMap.keySet()) {
            Map<String, Message> messagesFromBuddy = currentHistoryMap.get(buddy);
            for (Message message : messagesFromBuddy.values()) {
                if (m.getMessageType() == EnumKindOfMessage.MESSAGE_STATUS) {
                    if (message.getId().equals(m.getContent())) {

                        //  .put() is not allowed because this would be replaceing the MessageInformation
                        Message update = message;
                        update.setMessageStatus(m.getMessageStatus());
                        messagesFromBuddy.put(message.getId(), update);

                        HistoricizationService.serializeSpecificHistory(messagesFromBuddy, buddy);
                        logger.debug("Message is updated: " + m.toString());
                        break;
                    }
                } else {
                    if (message.getId().equals(m.getId())) {

                        Message update = message;
                        update.setMessageStatus(m.getMessageStatus());
                        messagesFromBuddy.put(message.getId(), update);

                        HistoricizationService.serializeSpecificHistory(messagesFromBuddy, buddy);
                        logger.debug("Message is updated (Type: Message): " + m.toString());
                        break;
                    }
                }

            }

        }
    }
    
    public void initCurrentHistoryMap(String buddyName){
        currentHistoryMap.put(buddyName, new HashMap<String, Message>());
    }

    /**
     * Inits the message- status.
     *
     * @param m
     * @param buddyName
     */
    public void initMessageStatus(Message m, String buddyName) {
        currentHistoryMap.get(buddyName).put(m.getId(), m);
        logger.debug("Status " + m.getMessageStatus().toString() + " is set for Message: " + m.getId());
    }

    public Map<Integer, String> getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(Map<Integer, String> buddyList) {
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
    
    public Map<String, Map<String, Message>> getCurrentHistoryMap() {
        return currentHistoryMap;
    }

    public void setCurrentHistoryMap(Map<String, Map<String, Message>> currentHistoryMap) {
        this.currentHistoryMap = currentHistoryMap;
    }

}
