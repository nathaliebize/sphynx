package com.nathaliebize.sphynx.security.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom annotation for password field in forms.
 *
 */
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Documented
public @interface PasswordField {
    String message() default "Invalid field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String password();
    String passwordMatch();
    boolean notEmpty() default false;
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String messagePasswordMatch() default "Field must match";
    String messageLength() default "Wrong lenght";
    String messageNotBlank() default "Must not be blank";

    
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PasswordField[] value();
    }
}
