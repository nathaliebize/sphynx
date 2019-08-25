package com.nathaliebize.sphynx.security.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Custom constraint validator for email field in forms.
 *
 */
public class EmailValidator implements ConstraintValidator<EmailField, String> {
    
    private boolean notEmpty;
    private Integer max;
    private String regex;
    private String messageNotEmpty;
    private String messageRegex;
    private String messageMax;
    
    @Override
    public void initialize(EmailField field) {
        notEmpty = field.notEmpty();
        max = field.max();
        regex = field.regex();
        messageNotEmpty = field.messageNotEmpty();
        messageRegex = field.messageRegex();
        messageMax = field.messageMax();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (notEmpty && value.isEmpty()) {
            context.buildConstraintViolationWithTemplate(messageNotEmpty).addConstraintViolation();
            return false;
        }
        if (max < Integer.MAX_VALUE && value.length() > max) {
            context.buildConstraintViolationWithTemplate(messageMax).addConstraintViolation();
            return false;
        }
        if (!value.matches(regex)) {
            context.buildConstraintViolationWithTemplate(messageRegex).addConstraintViolation();
            return false;
        }
        return true;
    }
}
