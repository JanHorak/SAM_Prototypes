/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import sam_testclient.entities.Handshake;
import sam_testclient.entities.Message;
import sam_testclient.exceptions.NotAHandshakeException;
import sam_testclient.utilities.Utilities;

/**
 * Wrapper class for Messages
 *
 * @author janhorak
 */
public abstract class MessageWrapper {

    public static String createJSON(Message message) {
        Logger logger = Logger.getLogger(MessageWrapper.class);
        Handshake hs = null;
        if (message.isHandshake()) {
            try {
                hs = message.getHandshake();
                System.out.println(hs.toString());
            } catch (NotAHandshakeException ex) {
                logger.error("Error (1001) | Not a Handshake: " + ex);
            }
            System.out.println(message.toString());
            if (ValidationManager.isValid(message)) {
                if (ValidationManager.isValid(hs)) {
                    return create(message);
                }
            } else {
                logger.error("Error (Validation) ");
                return returnError(message);
            }
        } else {
            if (ValidationManager.isValid(message)) {
                return create(message);
            } else {
                logger.error("Error (Validation) ");
                return returnError(message);
            }
        }
        return returnError(message);
    }

    public static Message JSON2Message(String json) {
        Logger logger = Logger.getLogger(MessageWrapper.class
        );
        Message incoming = create(json);
        Handshake hs = null;

        if (incoming.isHandshake()) {
            try {
                hs = incoming.getHandshake();
            } catch (NotAHandshakeException ex) {
                logger.error("Error (1001) | Not a Handshake: " + ex);
            }
            if (ValidationManager.isValid(hs) && ValidationManager.isValid(incoming)) {
                return incoming;
            }
        } else {
            if (ValidationManager.isValid(incoming)) {
                return incoming;
            }
        }

        logger.error(
                "Error in Validation of incoming Message");

        return null;
    }

    private static String create(Message m) {
        Gson gson = new Gson();
        return gson.toJson(m);
    }

    private static Message create(String json) {
        Gson gson = new Gson();

        return (Message) gson.fromJson(json, Message.class
        );
    }

    private static String returnError(Message message) {
        return Utilities.getLogTime() + "MessageWrapper(creation): ValidationError of the Message\n"
                + message.toString();
    }

}
