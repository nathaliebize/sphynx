package com.nathaliebize.sphynx.security.constraint;

import java.net.URL;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<UrlField, String> {

    private String messageUrl = "Must be a valid URL or hostname";
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if(isUrl(value)) {
            return true;
        } else if (isHost(value)) {
            return true;
        } else {
            context.buildConstraintViolationWithTemplate(messageUrl).addConstraintViolation();
            return false;
        }
    }

    private boolean isHost(String value) {
        String regex = "^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,}$";
        return value.matches(regex);
    }

    private boolean isUrl(String value) {
        try {
            new URL(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
