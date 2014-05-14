/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.sources;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author janhorak
 */
public abstract class ValidationManager {
    
    /**
     * This static method validates an Object
     * @param ob - the Object we want to validate
     */
    public static void validate(Object ob) {
        Set<ConstraintViolation<Object>> constraintViolations = returnConstraintViolantionSet(ob);
        for (ConstraintViolation<Object> violation : constraintViolations) {
            System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
        }
    }
    
    /**
     * This method returns the Set of ConstraintViolation for testing and the validate-method
     * @param ob - the Object we want to validate
     * @return - the Set of Constraints
     */
    public static Set<ConstraintViolation<Object>> returnConstraintViolantionSet(Object ob) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(ob);
        return constraintViolations;
    }
    
    /**
     * This static method validates an Object and returns the fields with errors
     * @param ob - the Object we want to validate
     * @return errorFields - a List of Fields they have errors
     */
    public static List<String> returnAmountOfInvalidFields(Object ob) {
        List<String> errorFields = new ArrayList<>();
        Set<ConstraintViolation<Object>> constraintViolations = returnConstraintViolantionSet(ob);
        for (ConstraintViolation<Object> violation : constraintViolations) {
            if ( violation.getPropertyPath().toString().isEmpty() ){
                errorFields.add("Error in global Constraint!");
            } else {
                errorFields.add(violation.getPropertyPath().toString());
            }
        }
        return errorFields;
    }
    
    /**
     * Returns the validation of the passed Object
     * @param ob - the Object we want to validate
     * @return - the boolean, if the Object is valid
     */
    public static boolean isValid(Object ob){
        return returnConstraintViolantionSet(ob).isEmpty();
    }
    
}
