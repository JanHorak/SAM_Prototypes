/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.exceptions;

/**
 *
 * @author janhorak
 */
public class InvalidSettingsException extends Exception{
    
    private static final long serialVersionUID = 4666974874499611218L;

    private String errorCode = "unknown";

    public InvalidSettingsException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
    
}
