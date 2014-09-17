/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sam.server.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import net.sam.server.validators.IPAddressValidator;


/**
 *
 * @author janhorak
 */

@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPAddressValidator.class)
@Documented
public @interface IPAddress {
    String message() default "Invalid IPAddress";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
