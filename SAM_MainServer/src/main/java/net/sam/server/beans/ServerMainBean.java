/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.beans;

import java.util.ArrayList;
import java.util.List;
import net.sam.server.entities.Member;
import net.sam.server.servermain.ClientServerCommunicationBase;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.Logger;


/**
 *
 * @author janhorak
 */

public class ServerMainBean implements ClientServerCommunicationBase{
    
    private static Logger logger;
    
    private ServerMainBean(){
        this.loggedInuserList = new ArrayList<Member>();
        this.registeredUserList = new ArrayList<Member>();
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
    
    private List<Member> loggedInuserList;
    
    private List<Member> registeredUserList;
    
    private int maxUsers;

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }
    
    public void addMember_login(Member member){
        this.loggedInuserList.add(member);
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
    
    public void logoutMember(Member m){
        Member tmp = new Member();
        for (Member me : loggedInuserList){
            if (me.getName().equals(m.getName())){
                tmp = me;
                break;
            }
        }
        this.loggedInuserList.remove(tmp);
        logger.debug(this.loggedInuserList.toString());
    }
}
