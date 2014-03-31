/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.sources;

import java.util.Date;


/**
 *
 * @author janhorak
 */
public class Message extends TransportObject{
    
    public Message (int senderId, int recieverId, EnumKindOfMessage kind, String content, String others){
        this.setContent(content);
        this.setMessageType(kind);
        this.setReceiverId(recieverId);
        this.setSenderId(senderId);
        this.setOthers(others);
        this.setTimestamp(new Date());
    }
    
    @Override
    public String toString(){
        return this.getTimestamp() +" "+this.getSenderId() + " to " +this.getReceiverId() + " " 
                +this.getMessageType() + " "
                +this.getContent() + " "
                +this.getOthers();
    }
}
