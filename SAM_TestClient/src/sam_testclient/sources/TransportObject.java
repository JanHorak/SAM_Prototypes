/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.sources;



/**
 * Abstract TrasportObject class. 
 * This will be used for the simple transport and wrapping of JSONs
 * @author janhorak
 */
public abstract class TransportObject {
    
    private EnumKindOfMessage messageType;
    
    private String content;
    
    private int recieverId;
    
    private int senderId;
    
    private String others;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
    

    public int getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(int recieverId) {
        this.recieverId = recieverId;
    }

    public EnumKindOfMessage getMessageType() {
        return messageType;
    }

    public void setMessageType(EnumKindOfMessage messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
}
