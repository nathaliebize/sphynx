package com.nathaliebize.sphynx.security.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;

/**
 * Custom annotation for the url field in forms.
 */
@Retention(RUNTIME)
@Constraint(validatedBy = UrlValidator.class)
@Target(FIELD)
public @interface UrlField {
    String message() default "Incorrect field";
    String messageUrl() default "Must be a valid URL or hostname";
    public abstract Class<?>[] groups() default {};
    public abstract Class<?>[] payload() default {};

}
