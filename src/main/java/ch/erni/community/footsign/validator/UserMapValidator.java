package ch.erni.community.footsign.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;


/**
 * Created by cepe on 25.02.2015.
 */
public class UserMapValidator implements ConstraintValidator<UserMap, Map<String, List<String>>> {

    private String keyPrefix;
    
    @Override
    public void initialize(UserMap userList) {
        this.keyPrefix = userList.keyPrefix();
    }

    @Override
    public boolean isValid(Map<String, List<String>> usersMap, ConstraintValidatorContext cvc) {
        cvc.disableDefaultConstraintViolation();
        boolean isValid = true;
        if (usersMap == null || usersMap.isEmpty()) {
            cvc.buildConstraintViolationWithTemplate("Users have to be entered").addConstraintViolation();
            isValid = false;

        }

        List<String> results1 = usersMap.get(keyPrefix + "1");
        List<String> results2 = usersMap.get(keyPrefix + "2");

        if (results1 == null || results1.isEmpty() || results2 == null || results2.isEmpty()) {
            cvc.buildConstraintViolationWithTemplate("Users have to be entered").addConstraintViolation();
            isValid = false;
        }

        int size1 = results1.size();
        int size2 = results2.size();

        if (size1 != size2) {
            cvc.buildConstraintViolationWithTemplate("Number of users in team have to be equal").addConstraintViolation();
            isValid = false;
        }
        
        if (size1 < 1 || size1 > 2 || size2 < 1 || size2 > 2 ) {
            cvc.buildConstraintViolationWithTemplate("Number of users isn't correct. Correct number is 1 or 2").addConstraintViolation();
            isValid = false;
        }
        
        String userNames = "";
        
        for (String key : usersMap.keySet()) {
            
            List<String> names = usersMap.get(key);
            for (String name : names) {
                
                if (name == null || name.isEmpty()) {
                    cvc.buildConstraintViolationWithTemplate("User domain name can not be empty").addConstraintViolation();
                    isValid = false;
                    continue;
                }
                
                if (userNames.contains(name)) {
                    cvc.buildConstraintViolationWithTemplate("User with domain name \'" + name + "\' already exist in player list").addConstraintViolation();
                    isValid = false;
                }
                
                userNames += name+";";
            }
        }
        
        return isValid;
    }
}
