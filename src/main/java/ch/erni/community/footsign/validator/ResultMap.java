package ch.erni.community.footsign.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cepe on 26.02.2015.
 */

@Constraint(validatedBy = ResultMapValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ResultMap {
    String message() default "{ResultMap}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minResult() default 0;

    int maxResult() default 8;
    
    String keyPrefix() default "team";

    int maxNumberOfWinMatches() default 2;
}
