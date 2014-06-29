/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import sam_testclient.validators.AvatarValidator;

/**
 *
 * @author janhorak
 */
@Constraint(validatedBy = AvatarValidator.class)
@Target({ElementType.LOCAL_VARIABLE, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Avatar {

    public int minHeight() default 10;

    public int maxHeight() default 50;

    public int minWidth() default 10;

    public int maxWidth() default 50;

    public String message() default "This Avatar is not valid";
    
    // Required by validation by runtime
    Class<? extends Payload>[] payload() default {};
    
    Class<?>[] groups() default {};
}
