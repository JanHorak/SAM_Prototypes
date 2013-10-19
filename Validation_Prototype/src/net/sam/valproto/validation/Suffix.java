/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.valproto.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import net.sam.valproto.validators.SuffixValidator;

/**
 *
 * @author janhorak
 */
@Constraint( validatedBy = SuffixValidator.class )
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Suffix
{
  String message() default "Suffix ungültig! Keine unserer Mailadressen!";
  String suffix();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}