/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sam_testclient.validators;

import javax.swing.ImageIcon;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import sam_testclient.entities.AvatarImage;
import sam_testclient.validations.Avatar;

/**
 *
 * @author janhorak
 */
public class AvatarValidator implements ConstraintValidator<Avatar, AvatarImage>{

    private int minHeight;
    private int maxHeight;
    private int minWidth;
    private int maxWidth;
    
    @Override
    public void initialize(Avatar a) {
        this.minHeight = a.minHeight();
        this.maxHeight = a.maxHeight();
        this.minWidth = a.minWidth();
        this.maxWidth = a.maxWidth();
    }

    @Override
    public boolean isValid(AvatarImage icon, ConstraintValidatorContext cvc) {
        boolean isValid = true;
//        isValid = false;
        if ((icon.getIconHeight() > maxHeight) || (icon.getIconHeight() < minHeight)){
            isValid = false;
        }
        if ((icon.getIconWidth() > maxWidth) || (icon.getIconWidth() < minWidth)){
            isValid = false;
        }
        
        return isValid;
    }
    
}
