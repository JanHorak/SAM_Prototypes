/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import java.io.StringReader;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.apache.log4j.Logger;
import sam_testclient.entities.Handshake;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumHandshakeReason;
import sam_testclient.enums.EnumHandshakeStatus;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.enums.EnumMessageStatus;
import sam_testclient.exceptions.NotAHandshakeException;

/**
 * Wrapper class for Messages
 *
 * @author janhorak
 */
public abstract class MessageWrapper {

    public static String createJSON(Message message) {
        Logger logger = Logger.getLogger(MessageWrapper.class);
        Handshake hs = null;
        logger.debug("Incoming Message: \n" + message.toString());
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
        Logger logger = Logger.getLogger(MessageWrapper.class);
        logger.debug("Incoming String: \n" + json);
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

        logger.error("Error in Validation of incoming Message");

        return null;
    }

    private static String create(Message m) {
        return createJSONFromMessage(m);
    }

    private static Message create(String json) {
        return createMessageFromJSON(json);
    }

    private static String returnError(Message message) {
        String errors = "";
        for (String err : ValidationManager.returnAmountOfInvalidFields(message)){
            errors += err + " ";
        }
        return "MessageWrapper: ValidationError of the Message\n"
                + message.toString()+ errors;
    }

    private static String createJSONFromMessage(Message m) {
        System.out.println(m.toString());
        JsonObjectBuilder globalBuilder = Json.createObjectBuilder();
        JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
        JsonObjectBuilder handshakeBuilder = Json.createObjectBuilder();
        JsonObjectBuilder mediaFileBuilder = Json.createObjectBuilder();
        
        messageBuilder.add("id", m.getId())
                .add("senderId", m.getSenderId())
                .add("receiverId", m.getReceiverId())
                .add("messageType", m.getMessageType().toString())
                .add("status", m.getMessageStatus().toString())
                .add("content", m.getContent())
                .add("others", m.getOthers());
        globalBuilder.add("message", messageBuilder);

        if (m.isHandshake()) {
            Handshake hs = null;
            try {
                hs = m.getHandshake();
            } catch (NotAHandshakeException ex) {
                java.util.logging.Logger.getLogger(MessageWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
            handshakeBuilder.add("id", hs.getId())
                    .add("answer", hs.isAnswer())
                    .add("content", hs.getContent())
                    .add("reason", hs.getReason().toString())
                    .add("status", hs.getStatus().toString());
            globalBuilder.add("handshake", handshakeBuilder);
        }

        JsonObject empJsonObject = globalBuilder.build();
        return empJsonObject.toString();
    }

    private static Message createMessageFromJSON(String json) {

        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = jsonReader.readObject();
  
        jsonReader.close();
        JsonObject messageJsonObject = jsonObject.getJsonObject("message");
        Message m = new Message(messageJsonObject.getInt("senderId"),
                messageJsonObject.getInt("receiverId"),
                EnumKindOfMessage.valueOf(messageJsonObject.getString("messageType")),
                messageJsonObject.getString("content"),
                messageJsonObject.getString("others"));
        m.setId(messageJsonObject.getString("id"));
        m.setMessageStatus(EnumMessageStatus.valueOf(messageJsonObject.getString("status")));
        
        if (m.isHandshake()) {
            Handshake hs = new Handshake();
            JsonObject innerJsonObject = jsonObject.getJsonObject("handshake");

            hs.setAnswer(innerJsonObject.getBoolean("answer"));
            hs.setContent(innerJsonObject.getString("content"));
            hs.setId(innerJsonObject.getInt("id"));
            hs.setReason((EnumHandshakeReason.valueOf(innerJsonObject.getString("reason"))));
            hs.setStatus(EnumHandshakeStatus.valueOf(innerJsonObject.getString("status")));
            m.setHandshake(hs);
        }
        
        return m;
    }

}
