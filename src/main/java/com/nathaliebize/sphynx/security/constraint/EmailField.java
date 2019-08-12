package com.nathaliebize.sphynx.security.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation for email field in forms.
 * 
 * @author Nathalie Bize
 *
 */

@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Target(FIELD)
public @interface EmailField {
    String message() default "Incorrect field";
    String messageNotEmpty() default "Must not be blank";
    String messageRegex() default "Wrong pattern";
    String messageMax() default "Wrong lenght";
    boolean notEmpty() default false;
    String regex() default "*";
    int max() default Integer.MAX_VALUE;
    public abstract Class<?>[] groups() default {};
    public abstract Class<?>[] payload() default {};

}
