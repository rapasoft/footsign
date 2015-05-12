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
    private int maxNumberOfWinMatches;


    @Override
    public void initialize(ResultMap resultList) {
        min = resultList.minResult();
        max = resultList.maxResult();
        keyPrefix = resultList.keyPrefix();
        maxNumberOfWinMatches = resultList.maxNumberOfWinMatches();
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

        long size1 = results1.stream().filter(r -> r != null && !r.isEmpty()).count();
        long size2 = results2.stream().filter(r -> r != null && !r.isEmpty()).count();

        if ( size1 != size2 ) {
            if(size1 < 2 || size1 > 4 ) {
                cvc.buildConstraintViolationWithTemplate("Match should consist of 2 or 3 games").addConstraintViolation();
                return false;
            }
            cvc.buildConstraintViolationWithTemplate("Size of results have to be equal").addConstraintViolation();
            return false;

        }

        long wins1 = results1.stream().filter(r -> r.equals("8")).count();
        long wins2 = results2.stream().filter(r -> r.equals("8")).count();

        if(wins1 != maxNumberOfWinMatches && wins2 != maxNumberOfWinMatches ) {
            cvc.buildConstraintViolationWithTemplate("Wrong number of win games").addConstraintViolation();
            return false;
        }

        int numberOfVictories = 0;

        for (String key : resultsMap.keySet()) {
            List<String> results = resultsMap.get(key);

            for (String value : results) {
                try {
                    if (value == null || value.isEmpty()) {
                        value = "0";
                    }
                    Integer intVal = Integer.parseInt(value);

                    if (intVal == 8) numberOfVictories++;

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

        if (numberOfVictories < size1) {
            cvc.buildConstraintViolationWithTemplate("Results aren\'t correct. Each game result have to contain one " + max).addConstraintViolation();
            return false;
        }


        return true;
    }
}
