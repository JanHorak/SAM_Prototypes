/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import com.google.gson.Gson;

import sam_testclient.entities.Message;
import sam_testclient.utilities.Utilities;


/**
 * Wrapper class for Messages
 *
 * @author janhorak
 */
public abstract class MessageWrapper {

    public static String createJSON(Message message) {
        if (ValidationManager.isValid(message)) {
            Gson gson = new Gson();
            return gson.toJson(message);
        } else {
            return Utilities.getLogTime() + "MessageWrapper(creation): ValidationError of the Message\n"
                    + message.toString();
        }
    }

    public static Message JSON2Message(String json) {
        Gson gson = new Gson();
        Message incoming = (Message) gson.fromJson(json, Message.class);
        if (ValidationManager.isValid(incoming)) {
            return incoming;
        } else {
            System.err.println(Utilities.getLogTime() + "MessageWrapper(incoming): ValidationError of the Message\n"
                    + incoming.toString());
            return null;
        }
    }

}
