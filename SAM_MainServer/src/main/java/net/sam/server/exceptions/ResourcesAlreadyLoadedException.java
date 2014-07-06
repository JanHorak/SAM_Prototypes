/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.exceptions;

/**
 *
 * @author janhorak
 */
public class ResourcesAlreadyLoadedException extends RuntimeException{

    public ResourcesAlreadyLoadedException(String message) {
        super(message);
    }
    
    
}
