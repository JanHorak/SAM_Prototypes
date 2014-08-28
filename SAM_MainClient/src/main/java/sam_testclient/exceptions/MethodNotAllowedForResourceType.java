/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.exceptions;

import sam_testclient.annotations.FileResource;

/**
 *
 * @author Jan
 */
public class MethodNotAllowedForResourceType extends RuntimeException{
    
    public MethodNotAllowedForResourceType(String message) {
        super(message);
    }
    
    public static String format(Class<?>... allowdTypes){
        String fieldList = "";
        for (int i = 0; i < allowdTypes.length; i++){
            if ( (i+1) != allowdTypes.length ){
                fieldList += allowdTypes[i].toString()+", ";
            } else {
                fieldList += allowdTypes[i].toString();
            }
        }
        return fieldList;
    }
    
}
