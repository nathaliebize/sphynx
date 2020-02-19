package com.nathaliebize.sphynx.security.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Custom constraint validator for the password field in forms.
 */
public class PasswordValidator  implements ConstraintValidator<PasswordField, Object> {
    
    private String password;
    private String passwordMatch;
    private boolean notEmpty;
    private int min;
    private int max;
    private String messageNotBlank;
    private String messagePasswordMatch;
    private String messageLength;

    @Override
    public void initialize(final PasswordField constraintAnnotation) {
        password = constraintAnnotation.password();
        passwordMatch = constraintAnnotation.passwordMatch();
        notEmpty = constraintAnnotation.notEmpty();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        messageNotBlank = constraintAnnotation.messageNotBlank();
        messagePasswordMatch = constraintAnnotation.messagePasswordMatch();
        messageLength = constraintAnnotation.messageLength();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String passwordValue = (String) new BeanWrapperImpl(value).getPropertyValue(password);
        String passwordMatchValue = (String) new BeanWrapperImpl(value).getPropertyValue(passwordMatch);
        context.disableDefaultConstraintViolation();  
        
        if (notEmpty && passwordValue.isEmpty()) {
            context.buildConstraintViolationWithTemplate(messageNotBlank)
                .addPropertyNode(password)
                .addConstraintViolation();
            if (passwordMatchValue.isEmpty()) {
                context.buildConstraintViolationWithTemplate(messageNotBlank)
                .addPropertyNode(passwordMatch)
                .addConstraintViolation();
            }
            return false;
        }
        if (passwordValue.length() < min || passwordValue.length() > max) {
            context.buildConstraintViolationWithTemplate(messageLength)
                .addPropertyNode(password)
                .addConstraintViolation();
            return false;
        }
        if (!passwordValue.equals(passwordMatchValue)) {
            context.buildConstraintViolationWithTemplate(messagePasswordMatch)
                .addPropertyNode(password)
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
