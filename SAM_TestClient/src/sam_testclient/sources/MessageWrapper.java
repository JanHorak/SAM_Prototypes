/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.sources;

import com.google.gson.Gson;
import sam_testclient.entities.Message;


/**
 * Wrapper class for Messages
 * @author janhorak
 */
public abstract class MessageWrapper {
    
    public static String createJSON(Message message){
        Gson gson = new Gson();
        return gson.toJson(message);
    }
    
    public static Message JSON2Message(String json){
        Gson gson = new Gson();
        return (Message) gson.fromJson(json, Message.class);
    }
    
}
