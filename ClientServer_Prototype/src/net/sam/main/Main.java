/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.main;

import java.io.IOException;
import net.sam.communication.Client;
import net.sam.communication.Server;

/**
 *
 * @author janhorak
 */
public class Main {

    // Please test this Method in Debugmode!
    // Otherwise the speed is too high for reconstruction
    public static void main(String[] args) throws IOException{
        Server s = new Server();
        Client c = new Client();
        c.connect();
        Client c2 = new Client();
        c2.connect();
        s.shutDown();
    }
    
}
