package ch.erni.community.footsign.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;

/**
 * Created by cepe on 26.02.2015.
 */
public class ResultMapValidator implements ConstraintValidator<ResultMap, Map<String, List<String>>> {
    
    private int min;
    private int max;
    private String keyPrefix;
    
    @Override
    public void initialize(ResultMap resultList) {
        min = resultList.minResult();
        max = resultList.maxResult();
        keyPrefix = resultList.keyPrefix();
    }

    @Override
    public boolean isValid(Map<String, List<String>> resultsMap, ConstraintValidatorContext cvc) {
        cvc.disableDefaultConstraintViolation();
        
        if (resultsMap == null || resultsMap.isEmpty()) {
            cvc.buildConstraintViolationWithTemplate("Results have to be entered").addConstraintViolation();
            return false;
        }
        
        List<String> results1 = resultsMap.get(keyPrefix + "1");
        List<String> results2 = resultsMap.get(keyPrefix + "2");
        
        if (results1 == null || results1.isEmpty() || results2 == null || results2.isEmpty()) {
            cvc.buildConstraintViolationWithTemplate("Results have to be entered").addConstraintViolation();
            return false;
        }
        
        int size1 = results1.size();
        int size2 = results2.size();
        
        if (size1 != size2) {
            cvc.buildConstraintViolationWithTemplate("Size of results have to be equal").addConstraintViolation();
            return false;
        }
        
        for (String key : resultsMap.keySet()) {
            List<String> results = resultsMap.get(key);
            
            for (String value : results) {
                try {
                    if (value == null || value.isEmpty()) value = "0";
                    Integer intVal = Integer.parseInt(value);

                    if (intVal < min || intVal > max) {
                        cvc.buildConstraintViolationWithTemplate("Result \'" + value + "\' isn\'t correct. Min value is: " + min + ", maxResult value is: " + max).addConstraintViolation();
                        return false;
                    }
                } catch (Exception e) {
                    cvc.buildConstraintViolationWithTemplate("Result \'" + value + "\' isn't correct. Result have to be integer in range [" + min + ", " + max + "]").addConstraintViolation();
                    e.printStackTrace();
                    return false;
                }
            }
        }
        
        
        return true;
    }
}
