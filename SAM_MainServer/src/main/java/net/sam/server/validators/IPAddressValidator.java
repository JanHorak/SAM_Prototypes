/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.server.validators;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.sam.server.validations.IPAddress;

/**
 *
 * @author janhorak
 */
public class IPAddressValidator implements ConstraintValidator<IPAddress, String> {

    @Override
    public void initialize(IPAddress constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = true;
        String address = value;
        if (!address.toLowerCase().equals("localhost")) {

            if (isBlacklisted(address)) {
                return !isValid;
            }
            String[] parts = value.split("\\.");
            if (parts.length != 4) {
                System.err.println("Malformed IPAddress detected");
                return !isValid;
            }

            for (int i = 0; i < parts.length; i++) {
                int part = 0;
                try {
                    part = Integer.decode(parts[i]);
                } catch (NumberFormatException ex) {
                    System.err.println("Malformed IPAddress detected: " + ex);
                    return !isValid;
                }

                if (part < 0 || part > 255) {
                    return !isValid;
                }
            }
        }

        return isValid;
    }

    private boolean isBlacklisted(String ipAddress) {
        boolean listed = false;
        if (ipAddress.equals("0.0.0.0")
                || ipAddress.equals("255.255.255.255")) {
            System.err.println("Blacklisted IPAddress detected");
            listed = true;
        }
        return listed;
    }

}
