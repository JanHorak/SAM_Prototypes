/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.sam.server.entities.Handshake;
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
        if (value.isHandshake()) {
            Handshake hs = value.getHandshake();
            if (hs.getContent() == null
                    || hs.getReason() == null
                    || hs.getStatus() == null) {
                isValid = false;
                System.err.println("One of the HandshakeValues is null!");
            }
        }
        return isValid;
    }

}
