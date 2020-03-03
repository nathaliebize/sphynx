package com.nathaliebize.sphynx.security.constraint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.Before;
import org.junit.Test;

import com.nathaliebize.sphynx.model.view.RegisterUser;

public class PasswordValidatorTest {
    PasswordValidator passwordValidator = new PasswordValidator();
    RegisterUser registerUser = new RegisterUser();
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    PasswordField constraintAnnotation = mock(PasswordField.class);
    
    @Before
    public void setUp() {
        when(constraintAnnotation.password()).thenReturn("password");
        when(constraintAnnotation.passwordMatch()).thenReturn("confirmedPassword");
        when(constraintAnnotation.notEmpty()).thenReturn(true);
        when(constraintAnnotation.min()).thenReturn(6);
        when(constraintAnnotation.max()).thenReturn(12);
        when(constraintAnnotation.messageNotBlank()).thenReturn("message not blank");
        when(constraintAnnotation.messagePasswordMatch()).thenReturn("message password Match");
        when(constraintAnnotation.messageLength()).thenReturn("message lenght");
        passwordValidator.initialize(constraintAnnotation);
    }
    
    @Test
    public void testIsValid() {
        registerUser.setPassword("password");
        registerUser.setConfirmedPassword("password");
        
        boolean isValid = passwordValidator.isValid(registerUser, context);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_notMatch() {
        registerUser.setPassword("password1");
        registerUser.setConfirmedPassword("password2");
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate("message password Match")).thenReturn(builder);
        when(builder.addPropertyNode("password")).thenReturn(mock(NodeBuilderCustomizableContext.class));
        
        boolean isValid = passwordValidator.isValid(registerUser, context);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_passwordIsEmpty() {
        registerUser.setPassword("");
        registerUser.setConfirmedPassword("password2");
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate("message not blank")).thenReturn(builder);
        when(builder.addPropertyNode("password")).thenReturn(mock(NodeBuilderCustomizableContext.class));
        
        boolean isValid = passwordValidator.isValid(registerUser, context);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_tooShort() {
        registerUser.setPassword("pass");
        registerUser.setConfirmedPassword("password2");
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate("message lenght")).thenReturn(builder);
        when(builder.addPropertyNode("password")).thenReturn(mock(NodeBuilderCustomizableContext.class));
        
        boolean isValid = passwordValidator.isValid(registerUser, context);
        
        assertFalse(isValid);
    }
    
    @Test
    public void testIsValid_tooLong() {
        registerUser.setPassword("pass");
        registerUser.setConfirmedPassword("password2");
        ConstraintViolationBuilder builder = mock(ConstraintViolationBuilder.class);
        when(context.buildConstraintViolationWithTemplate("message lenght")).thenReturn(builder);
        when(builder.addPropertyNode("password")).thenReturn(mock(NodeBuilderCustomizableContext.class));
        
        boolean isValid = passwordValidator.isValid(registerUser, context);
        
        assertFalse(isValid);
    }
}
