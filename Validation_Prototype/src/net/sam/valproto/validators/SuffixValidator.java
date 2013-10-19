/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.valproto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.sam.valproto.validation.Suffix;

/**
 *
 * @author janhorak
 */
public class SuffixValidator implements ConstraintValidator<Suffix, String>{

    String suffix;
    
    @Override
    public void initialize(Suffix a) {
        suffix = a.suffix();
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        boolean isValid = true;
        if ( !t.endsWith(suffix) ){
            isValid = false;
        }
        return isValid;
    }   
}
