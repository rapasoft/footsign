package ch.erni.community.footsign.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;


/**
 * Created by cepe on 25.02.2015.
 */
public class UserListValidator implements ConstraintValidator<UserList, List<String>> {

    @Override
    public void initialize(UserList userList) {

    }

    @Override
    public boolean isValid(List<String> users, ConstraintValidatorContext cvc) {
        cvc.disableDefaultConstraintViolation();
        if (users == null || users.isEmpty()) {
            cvc.buildConstraintViolationWithTemplate("Users have to be entered").addConstraintViolation();
            return false;

        } else if (users.size() < 1 || users.size() > 2) {
            cvc.buildConstraintViolationWithTemplate("Number of users isn't correct. Correct number is 1 or 2").addConstraintViolation();
            return false;
        } else {
            if (users.size() == 1) {
                String u1 = users.get(0);
                
                if (u1.isEmpty()) {
                    cvc.buildConstraintViolationWithTemplate("User can not be empty").addConstraintViolation();
                    return false;
                }

                
            } else if (users.size() == 2) {
                String u1 = users.get(0);
                String u2 = users.get(1);

                if (u1.isEmpty() || u2.isEmpty()) {

                    cvc.buildConstraintViolationWithTemplate("User can not be empty").addConstraintViolation();
                    return false;
                }
                
                if (u1.equals(u2)) {
                    cvc.buildConstraintViolationWithTemplate("Users can not be equal").addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}
