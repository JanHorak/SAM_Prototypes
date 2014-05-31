/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.beans;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sam.server.entities.Member;
import net.sam.server.interfaces.ClientServerCommunicationBase;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;


/**
 *
 * @author janhorak
 */

public class ServerMainBean implements ClientServerCommunicationBase{
    
    private ServerMainBean(){
        this.loggedInuserList = new ArrayList<Member>();
        this.registeredUserList = new ArrayList<Member>();
        this.socketMap = new ConcurrentHashMap<Integer, Socket>();
        
        logger = org.apache.log4j.Logger.getLogger(ServerMainBean.class);
    }
    
    /**
     * Singleton- Pattern
     * [@TODO: Maybe it should be replaced by CDI]
     */
    private static ServerMainBean instance = null;
    
    public static ServerMainBean getInstance() {
        if (instance == null) {
            instance = new ServerMainBean();
            logger.info(Utilities.getLogTime()+" Singleton instantiated successfully");
        }
        return instance;
    }
    
    private static Logger logger;
    
    private List<Member> loggedInuserList;
    
    private List<Member> registeredUserList;
    
    private Map<Integer, Socket> socketMap;
      
    private int maxUsers;
    
    public boolean isMemberOnline(int id){
        return socketMap.containsKey(id);
    }
    
    public boolean isMemberOnline(String membername){
        boolean isOnline = true;
        for (Member me : this.loggedInuserList){
            if (me.getName().equals(membername)){
                return isOnline;
            }
        }
        return !isOnline;
    }
    
    public Socket returnCommnunicationChannel(int id){
        return socketMap.get(id);
    }
    
    public Member getLoggedInMemberById(int id){
        Member m = new Member();
        for (Member me : this.loggedInuserList){
            if (me.getUserID() == id){
                return me;
            }
        }
        return m;
    }
    
    public Member getRegisteredMemberIdByName(String name){
        Member m = new Member();
        for (Member me : this.registeredUserList){
            if (me.getName().equals(name)){
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
    
    public void addMember_login(Member member, Socket socket){
        this.loggedInuserList.add(member);
        this.socketMap.put(member.getUserID(), socket);
    }
    
    public void addMember_registered(Member member){
        this.registeredUserList.add(member);
    }
    
    public List<Member> getloggedInMembers(){
        return this.loggedInuserList;
    }
    
    public List<Member> getRegisteredMembers(){
        return this.registeredUserList;
    }
    
    public void setRegisteredMembers(List<Member> memberList){
        this.registeredUserList = memberList;
    }
    
    public static List<String> wrapForUI(List<Member> incomingList){
        List<String> resultList = new ArrayList<>();
        for (Member m : incomingList){
            resultList.add("Id: "+m.getUserID() + "| Name: " +m.getName());
        }
        return resultList;
    }
    
    /**
     * This method is cleaning up the bean and logs out the member.
     * The member will be searched in the {@link #loggedInuserList} and 
     * will save the member in the local variable <code>tmp</code>.
     * The Member will be removed from the socketMap, too.
     * 
     * @param m - Member for logout
     */
    public void logoutMember(Member m){
        Member tmp = new Member();
        for (Member me : loggedInuserList){
            if (me.getName().equals(m.getName())){
                tmp = me;
                break;
            }
        }
        this.loggedInuserList.remove(tmp);
        this.socketMap.remove(m.getUserID());
        logger.debug(this.loggedInuserList.toString());
    }
    
    public List<Socket> getAllSockets(){
        List<Socket> result = new ArrayList<Socket>();
        for (Socket s : this.socketMap.values()){
            result.add(s);
        }
        return result;
    }
    
    public Map<Integer, Boolean> getOnlineStatusOfMemberById(String[] ids){
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++){
            idList.add(Integer.decode(ids[i]));
        }
        
        Map<Integer, Boolean> buddy_online_Response = new HashMap<Integer, Boolean>();
        for (int id : idList){
            for (Member m : loggedInuserList){
                if (m.getUserID() == id){
                    buddy_online_Response.put(id,Boolean.TRUE);
                }
            }
        }
        for (Boolean b : buddy_online_Response.values()){
            if (b == null){
                b = Boolean.FALSE;
            }
        }
        return buddy_online_Response;
    }
    
    public String getLoggedInMemberNameById(int id){
        return getLoggedInMemberById(id).getName();
    }
    
    public boolean isTheMemberRegistered(String name){
        boolean contains = false;
        for (Member m : this.registeredUserList){
            if (name.equals(m.getName())){
                contains = true;
                break;
            }
        }
        return contains;
    }
}
