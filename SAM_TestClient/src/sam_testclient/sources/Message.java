/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.sources;


/**
 *
 * @author janhorak
 */
public class Message extends TransportObject{
    
    public Message (int senderId, int recieverId, EnumKindOfMessage kind, String content, String others){
        this.setContent(content);
        this.setMessageType(kind);
        this.setRecieverId(recieverId);
        this.setSenderId(senderId);
        this.setOthers(others);
    }
    
    @Override
    public String toString(){
        return this.getSenderId() + " to " +this.getRecieverId() + " " 
                +this.getMessageType() + " "
                +this.getContent() + " "
                +this.getOthers();
    }
}
