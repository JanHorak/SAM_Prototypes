/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.exceptions;

import annotations.FileResource;

/**
 *
 * @author Jan
 */
public class MethodNotAllowedForResourceType extends RuntimeException{
    
    public MethodNotAllowedForResourceType(String message) {
        super(message);
    }
    
    public static String format(FileResource.Type... allowdTypes){
        String fieldList = "";
        for (int i = 0; i < allowdTypes.length; i++){
            if ( (i+1) != allowdTypes.length ){
                fieldList += allowdTypes[i].name()+", ";
            } else {
                fieldList += allowdTypes[i].name();
            }
        }
        return fieldList;
    }
    
}
