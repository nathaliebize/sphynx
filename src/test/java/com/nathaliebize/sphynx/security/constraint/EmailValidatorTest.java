package com.nathaliebize.sphynx.security.constraint;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.junit.Before;
import org.junit.Test;

public class EmailValidatorTest {
    EmailValidator emailValidator = new EmailValidator();
    EmailField field = mock(EmailField.class);
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    
    @Before
    public void setUp() {
        when(field.notEmpty()).thenReturn(true);
        when(field.max()).thenReturn(10);
        when(field.regex()).thenReturn("a@b");
        when(field.messageNotEmpty()).thenReturn("message not empty");
        when(field.messageRegex()).thenReturn("message regex");
        when(field.messageMax()).thenReturn("message max");
        emailValidator.initialize(field);
    }

    @Test
    public void testIsValid() {     
        boolean isValid = emailValidator.isValid("a@b", context);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_isEmpty() {
        when(context.buildConstraintViolationWithTemplate("message not empty")).thenReturn(mock(ConstraintViolationBuilder.class));
        
        boolean isValid = emailValidator.isValid("", context);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_isTooLong() {
        when(context.buildConstraintViolationWithTemplate("message max")).thenReturn(mock(ConstraintViolationBuilder.class));
        
        boolean isValid = emailValidator.isValid("a@bbbbbbbbbbbbbb", context);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_regexInvalid() {
        when(context.buildConstraintViolationWithTemplate("message regex")).thenReturn(mock(ConstraintViolationBuilder.class));
        
        boolean isValid = emailValidator.isValid("abc", context);
        
        assertFalse(isValid);
    }
}
