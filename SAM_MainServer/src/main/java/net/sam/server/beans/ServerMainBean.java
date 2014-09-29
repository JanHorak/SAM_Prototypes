/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.beans;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sam.server.entities.Member;
import net.sam.server.entities.ServerConfig;
import net.sam.server.manager.DataAccess;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;

/**
 *
 * @author janhorak
 */
public class ServerMainBean {

    /**
     * Singleton- Pattern [@TODO: Maybe it should be replaced by CDI]
     */
    private static ServerMainBean instance = null;

    public static ServerMainBean getInstance() {
        if (instance == null) {
            instance = new ServerMainBean();
            logger.info(Utilities.getLogTime() + " Singleton instantiated successfully");
        }
        return instance;
    }

    public ServerMainBean() {
        this.loggedInuserList = new ArrayList<>();
        this.registeredUserList = new ArrayList<>();
        this.socketMap = new ConcurrentHashMap<>();
        this.allConfigs = new ArrayList<>();

        this.socketMap_unsafe = new ConcurrentHashMap<>();
        this.secretBuffer = new ConcurrentHashMap<>();
        reloadAllConfigs();
        logger = Logger.getLogger(ServerMainBean.class);
    }

    private static Logger logger;
    
    private ServerConfig currentConfig;

    private List<Member> loggedInuserList;

    private static List<ServerConfig> allConfigs;

    private List<Member> registeredUserList;

    private Map<Integer, Socket> socketMap;

    private Map<Integer, Socket> socketMap_unsafe;

    private Map<Integer, String> secretBuffer;

    private String serverPassword;

    private int maxUsers;

    public boolean isMemberOnline(int id) {
        return socketMap.containsKey(id);
    }
    
    public static void reloadAllConfigs(){
        allConfigs = DataAccess.getAllServerConfigs();
    }

    public List<ServerConfig> getConfigs() {
        return this.allConfigs;
    }
    
    public void setCurrentConfig(ServerConfig sc){
        currentConfig = sc;
    }

    public boolean isMemberOnline(String membername) {
        boolean isOnline = true;
        for (Member me : this.loggedInuserList) {
            if (me.getName().equals(membername)) {
                return isOnline;
            }
        }
        return !isOnline;
    }
    
    public ServerConfig getConfigByName(String name){
        for (ServerConfig sc : allConfigs){
            if (sc.getName().equals(name)){
                return sc;
            }
        }
        return null;
    }

    public Socket returnCommnunicationChannel(int id) {
        return socketMap.get(id);
    }

    public Socket returnUnsafeCommucationChannel(int id) {
        return socketMap_unsafe.get(id);
    }

    public Member getLoggedInMemberById(int id) {
        Member m = new Member();
        for (Member me : this.loggedInuserList) {
            if (me.getUserID() == id) {
                return me;
            }
        }
        return m;
    }

    public Member getRegisteredMemberByName(String name) {
        Member m = new Member();
        for (Member me : this.registeredUserList) {
            if (me.getName().equals(name)) {
                return me;
            }
        }
        return m;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public void addMember_login(Member member, Socket socket) {
        this.loggedInuserList.add(member);
        this.socketMap.put(member.getUserID(), socket);
    }

    public void addMember_registered(Member member) {
        this.registeredUserList.add(member);
    }

    public List<Member> getloggedInMembers() {
        return this.loggedInuserList;
    }

    public List<Member> getRegisteredMembers() {
        return this.registeredUserList;
    }

    public void setRegisteredMembers(List<Member> memberList) {
        this.registeredUserList = memberList;
    }

    public static List<String> wrapUserListForUI(List<Member> incomingList) {
        List<String> resultList = new ArrayList<>();
        for (Member m : incomingList) {
            resultList.add("Id: " + m.getUserID() + "| Name: " + m.getName());
        }
        return resultList;
    }

    public List<String> wrapServerConfigListForUI() {
        List<String> resultList = new ArrayList<>();
        for (ServerConfig m : allConfigs) {
            resultList.add(m.getName());
        }
        return resultList;
    }

    /**
     * This method is cleaning up the bean and logs out the member. The member
     * will be searched in the {@link #loggedInuserList} and will save the
     * member in the local variable <code>tmp</code>. The Member will be removed
     * from the socketMap, too.
     *
     * @param m - Member for logout
     */
    public void logoutMember(Member m) {
        Member tmp = new Member();
        for (Member me : loggedInuserList) {
            if (me.getName().equals(m.getName())) {
                tmp = me;
                break;
            }
        }
        this.loggedInuserList.remove(tmp);
        this.socketMap.remove(m.getUserID());
        logger.debug(this.loggedInuserList.toString());
    }

    public List<Socket> getAllSockets() {
        List<Socket> result = new ArrayList<Socket>();
        for (Socket s : this.socketMap.values()) {
            result.add(s);
        }
        return result;
    }

    public Member getRegisteredMemberById(int id) {
        for (Member m : getRegisteredMembers()) {
            if (m.getUserID() == id) {
                return m;
            }
        }
        return null;
    }

    public Map<Integer, String> getOnlineStatusOfMemberById(String[] ids) {
        List<Integer> idList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        for (int i = 0; i < ids.length; i++) {
            idList.add(Integer.decode(ids[i]));
        }

        Map<Integer, String> buddy_online_Response = new HashMap<Integer, String>();
        for (int id : idList) {
            for (Member m : loggedInuserList) {
                if (m.getUserID() == id) {
                    buddy_online_Response.put(id, "true");
                }
            }
        }
        for (int id : idList) {
            if (!buddy_online_Response.containsKey(id)) {
                // if for allowed in membersettings is needed
                Member m = DataAccess.getMemberById(id);
                buddy_online_Response.put(id, "false " + sdf.format(m.getLastTimeOnline()));
            }
        }
        return buddy_online_Response;
    }

    public String getLoggedInMemberNameById(int id) {
        return getLoggedInMemberById(id).getName();
    }

    public boolean isTheMemberRegistered(String name) {
        boolean contains = false;
        for (Member m : this.registeredUserList) {
            if (name.equals(m.getName())) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    /**
     * Adds a passed socket and ID of a client to the unsafe- List. See:
     * {@link #socketMap_unsafe}
     *
     * @param id
     * @param socket
     */
    public void addToUnsafeList(Integer id, Socket socket) {
        this.socketMap_unsafe.put(id, socket);
        logger.info("Client is added to unsafe- List:\n ID: "
                + id
                + "\nSocket: " + socket.toString());
    }

    /**
     * Deletes an entry of the unsafe- List by passed id. See:
     * {@link #socketMap_unsafe}
     *
     * @param id
     */
    public void deleteFromUnsafe(Integer id) {
        logger.info("Client will be removed from unsafe- List:\n ID: "
                + id + "\nSocket: "
                + this.socketMap_unsafe.get(id).toString());
        this.socketMap_unsafe.remove(id);
    }

    /**
     * Adds a id (id of client) and a secretground to a map. The secretground is
     * the String which is sent to the Client. Calculated uncleaned String.
     *
     * @param id
     * @param secretground
     */
    public void addToSecretBuffer(Integer id, String secretground) {
        this.secretBuffer.put(id, secretground);
        logger.info("Secret is saved to buffer:\n ID: "
                + id
                + "\nSecret: " + secretground);
    }

    /**
     * Removes the secretground from the map. The secretground is the String
     * which is sent to the Client. Calculated uncleaned String.
     *
     * @param id
     */
    public void deleteFromSecretBuffer(Integer id) {
        logger.info("Secret will be removed from Buffer- List:\n ID: "
                + id + "\nSecret: "
                + this.secretBuffer.get(id));
        this.secretBuffer.remove(id);
    }

    public String getSecretById(Integer id) {
        return this.secretBuffer.get(id);
    }

}
