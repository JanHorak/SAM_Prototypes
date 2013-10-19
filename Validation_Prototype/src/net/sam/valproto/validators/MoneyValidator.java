/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sam.valproto.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.sam.valproto.validation.Money;

/**
 *
 * @author janhorak
 */
public class MoneyValidator implements ConstraintValidator<Money, Integer>{

    @Override
    public void initialize(Money a) {
        
    }

    @Override
    public boolean isValid(Integer t, ConstraintValidatorContext cvc) {
        boolean isValid = true;
        if ( t <= 500 ){
            isValid = false;
        }
        return isValid;
    }


    
}
