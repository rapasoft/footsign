package ch.erni.community.footsign.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by cepe on 26.02.2015.
 */
public class ResultListValidator implements ConstraintValidator<ResultList, List<String>> {
    
    private int min;
    private int max;
    
    @Override
    public void initialize(ResultList resultList) {
        min = resultList.min();
        max = resultList.max();
    }

    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext cvc) {
        cvc.disableDefaultConstraintViolation();
        
        if (strings == null || strings.isEmpty()) {
            cvc.buildConstraintViolationWithTemplate("Results have to be entered").addConstraintViolation();
            return false;
        }
        
        for (String res : strings) {
            try {
                int value = Integer.parseInt(res);
                if (value < min || value > max) {
                    cvc.buildConstraintViolationWithTemplate("Result \'" + value + "\' isn\'t correct. Min value is: " + min + ", max value is: " + max).addConstraintViolation();
                    return false; 
                }
            } catch (Exception e) {
                cvc.buildConstraintViolationWithTemplate("Result have to be integer in range [" + min + ", " + max + "]").addConstraintViolation();
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
