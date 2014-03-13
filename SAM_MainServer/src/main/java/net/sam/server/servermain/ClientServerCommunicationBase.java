/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.servermain;

/**
 *
 * @author janhorak
 * Abstract Class for Client- Server Communication
 */
public interface ClientServerCommunicationBase {
    
    public static final String IP = "127.0.0.1";
    
    public static final String HOST = "localhost";
    
    public static final int PORT = 2222;
    
    public final int TIMEOUTTIMER = 60000;
       
}
