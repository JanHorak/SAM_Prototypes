/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.validators;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.sam.server.entities.Handshake;
import net.sam.server.exceptions.NotAHandshakeException;
import net.sam.server.validation.Message;

/**
 *
 * @author janhorak
 */
public class MessageValidator implements ConstraintValidator<Message, net.sam.server.entities.Message> {

    @Override
    public void initialize(Message constraintAnnotation) {

    }

    @Override
    public boolean isValid(net.sam.server.entities.Message value, ConstraintValidatorContext context) {
        boolean isValid = true;
        Handshake handshake;
        try {
            if (value.isHandshake()) {
                handshake = value.getHandshake();
                if (handshake.getContent() == null
                        || handshake.getReason() == null
                        || handshake.getStatus() == null) {
                    isValid = false;
                    System.err.println("One of the HandshakeValues is null!");
                }
            }
        } catch (NotAHandshakeException ex) {
            Logger.getLogger(MessageValidator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isValid;
    }

}
