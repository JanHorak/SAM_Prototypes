/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.jsontests;

import java.io.StringReader;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import net.sam.server.entities.Handshake;
import net.sam.server.entities.Message;
import net.sam.server.enums.EnumHandshakeReason;
import net.sam.server.enums.EnumHandshakeStatus;
import net.sam.server.enums.EnumKindOfMessage;
import net.sam.server.manager.MessageWrapper;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
public class JSONTests {

    @Test
    public void createSomeJSONsTest() {
        Message m1 = new Message(0, 0, EnumKindOfMessage.MESSAGE, "I'm a message!", "other");
        Message m2 = new Message(0, 0, EnumKindOfMessage.KEYEXCHANGE, "I'm a Keyexchange- request!", "other");
        Message m3 = new Message(0, 0, EnumKindOfMessage.REGISTER, "I'm a register- message!", "other");

        Handshake hs = new Handshake();
        hs.setAnswer(true);
        hs.setContent("sdasd");
        hs.setReason(EnumHandshakeReason.REGISTER);
        hs.setStatus(EnumHandshakeStatus.START);
        m1.setHandshake(hs);

        String json1 = MessageWrapper.createJSON(m1);
        String json2 = MessageWrapper.createJSON(m2);
        String json3 = MessageWrapper.createJSON(m3);

        assertNotNull(json1);
        System.out.println(json1);
        assertNotNull(json2);
        System.out.println(json2);
        assertNotNull(json3);
        System.out.println(json3);

        Message reM1 = MessageWrapper.JSON2Message(json1);
        assertNotNull(reM1);
        System.out.println(reM1.toString());
        Message reM2 = MessageWrapper.JSON2Message(json2);
        assertNotNull(reM2);
        System.out.println(reM2.toString());
        Message reM3 = MessageWrapper.JSON2Message(json3);
        assertNotNull(reM3);
        System.out.println(reM3.toString());
    }

    @Test
    public void JSONFilePartTest() {
        String message = "{\"message\":{\"senderId\":0,\"receiverId\":0,\"messageType\":\"FILEPART\",\"content\":\"[-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 25, 0, 0, 0, 25, 8, 2, 0, 0, 0, 75, -117, 18, 52, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 11, 19, 0, 0, 11, 19, 1, 0, -102, -100, 24, 0, 0, 1, -15, 73, 68, 65, 84, 120, -100, -19, -109, -39, 110, 19, 49, 20, -122, 109, -49, -18, 89, 50, 13, 66, -48, 22, 37, 80, 16, 79, 0, 52, -19, 11, 32, 46, 120, 91, 36, -60, 11, -76, -95, -16, 0, -12, -94, -91, 91, 8, 105, 67, -42, 89, -67, 29, 46, 38, 25, 58]\",\"others\":\"other\"}}";
        Message m = MessageWrapper.JSON2Message(message);
        String json = MessageWrapper.createJSON(m);
        System.out.println(json);
    }

    @Test
    public void test2() {
        String json
                = "{\"message\":{\"senderId\":0,\"receiverId\":0,\"messageType\":\"HANDSHAKE\",\"content\":\"I'm a message!\",\"others\":\"other\"},\"handshake\":{\"id\":0,\"answer\":true,\"content\":\"sdasd\",\"reason\":\"REGISTER\",\"status\":\"START\"}}";

        System.out.println(json);
        Message reM3 = MessageWrapper.JSON2Message(json);
        System.out.println(reM3.toString());
    }

    @Test
    @Ignore
    public void testJavaJSONAPI() {
        String json = "{\n"
                + "  \"handshake\": {\n"
                + "    \"id\": 0,\n"
                + "    \"status\": \"START\",\n"
                + "    \"reason\": \"BUDDY_REQUEST\",\n"
                + "    \"answer\": false,\n"
                + "    \"content\": \"Test1\"\n"
                + "  },\n"
                + "  \"messageType\": \"HANDSHAKE\",\n"
                + "  \"content\": \"Test2\",\n"
                + "  \"receiverId\": 0,\n"
                + "  \"senderId\": 2,\n"
                + "  \"others\": \"\",\n"
                + "  \"timestamp\": \"2014-06-04 10:19:05\"\n"
                + "}";
        //create JsonReader object
        JsonReader jsonReader = Json.createReader(new StringReader(json));

        //get JsonObject from JsonReader
        JsonObject jsonObject = jsonReader.readObject();

        //we can close IO resource and JsonReader now
        jsonReader.close();

        Message m = new Message(jsonObject.getInt("senderId"),
                jsonObject.getInt("receiverId"),
                EnumKindOfMessage.valueOf(jsonObject.getString("messageType")),
                jsonObject.getString("content"),
                jsonObject.getString("others"));
        System.out.println("----------" + m.toString());

        Handshake hs = new Handshake();
        JsonObject innerJsonObject = jsonObject.getJsonObject("handshake");

        hs.setAnswer(innerJsonObject.getBoolean("answer"));
        hs.setContent(innerJsonObject.getString("content"));
        hs.setId(innerJsonObject.getInt("id"));
        hs.setReason((EnumHandshakeReason.valueOf(innerJsonObject.getString("reason"))));
        hs.setStatus(EnumHandshakeStatus.valueOf(innerJsonObject.getString("status")));
        System.out.println("---------" + hs.toString());

        JsonObjectBuilder globalBuilder = Json.createObjectBuilder();
        JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
        JsonObjectBuilder handshakeBuilder = Json.createObjectBuilder();

        messageBuilder.add("senderId", m.getSenderId())
                .add("receiverId", m.getReceiverId())
                .add("messageType", m.getMessageType().toString())
                .add("content", m.getContent())
                .add("others", m.getOthers());

        handshakeBuilder.add("id", hs.getId())
                .add("answer", hs.isAnswer())
                .add("content", hs.getContent())
                .add("reason", hs.getReason().toString())
                .add("status", hs.getStatus().toString());

        globalBuilder.add("message", messageBuilder);
        globalBuilder.add("handshake", handshakeBuilder);

        JsonObject empJsonObject = globalBuilder.build();
        System.out.println(empJsonObject);

    }

}
