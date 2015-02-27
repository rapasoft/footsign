package ch.erni.community.footsign.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by cepe on 25.02.2015.
 */

@Constraint(validatedBy = UserMapValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserMap {
    String message() default "{StringMap}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String keyPrefix() default "team";
}
