package com.nathaliebize.sphynx.controller;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nathaliebize.sphynx.model.User;
import com.nathaliebize.sphynx.model.view.ForgotPasswordUser;
import com.nathaliebize.sphynx.model.view.RegisterUser;
import com.nathaliebize.sphynx.model.view.ResetPasswordUser;
import com.nathaliebize.sphynx.routing.SiteMap;
import com.nathaliebize.sphynx.service.EmailService;
import com.nathaliebize.sphynx.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
           
    @MockBean
    private UserService userService;
    
    @MockBean
    private EmailService emailService;
    
    @Before
    public void setUp() {
      mockMvc = MockMvcBuilders
              .webAppContextSetup(webApplicationContext)
              .apply(springSecurity())
              .build();
    }
    
    @Test
    public void testShowLoginPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/user/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_LOGIN.getPath()))
                .andExpect(unauthenticated());
    }
    

    @Test
    public void testShowRegisterPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/user/register")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_REGISTER.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email@email.com")
                .param("password", "password")
                .param("confirmedPassword", "password")
                .param("acceptedTerms", "on")
                .header("host", "http://sphynx.dev"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_VERIFY.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister_incorrectEmail() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email")
                .param("password", "password")
                .param("confirmedPassword", "password")
                .param("acceptedTerms", "on")
                .header("host", "http://sphynx.dev"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_REGISTER.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister_incorrectPassword() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email@email.com")
                .param("password", "p")
                .param("confirmedPassword", "p")
                .param("acceptedTerms", "on")
                .header("host", "http://sphynx.dev"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_REGISTER.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister_unmatchedPassword() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email@email.com")
                .param("password", "password1")
                .param("confirmedPassword", "password2")
                .param("acceptedTerms", "on")
                .header("host", "http://sphynx.dev"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_REGISTER.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister_missingAcceptedTerms() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email@email.com")
                .param("password", "password")
                .param("confirmedPassword", "password")
                .header("host", "http://sphynx.dev"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_REGISTER.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister_userAlreadyExisting() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email@email.com")
                .param("password", "password")
                .param("confirmedPassword", "password")
                .header("host", "http://sphynx.dev"))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_REGISTER.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testRegister_unvalidRequest() throws Exception {
        when(this.userService.registerNewUser((RegisterUser) any(RegisterUser.class))).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/register")
                .param("email", "email@email.com")
                .param("password", "password")
                .param("confirmedPassword", "password"))
                .andExpect(status().is(400))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testShowResetPasswordRequestPage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/user/reset-password-request")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_RESET_PASSWORD_REQUEST.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testSendPasswordResetLink() throws Exception {
        ForgotPasswordUser forgotPasswordUser = new ForgotPasswordUser();
        forgotPasswordUser.setEmail("email@email.com");
        User user = new User("email@email.com", "password");
        user.setRegistrationKey("1234");
        when(userService.updateRegistrationKey((ForgotPasswordUser) any(ForgotPasswordUser.class))).thenReturn(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/reset-password-request")
                .header("host", "http://sphynx.dev")
                .flashAttr("forgotPasswordUser", forgotPasswordUser))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_VERIFY.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testSendPasswordResetLink_bindingError() throws Exception {
        ForgotPasswordUser forgotPasswordUser = new ForgotPasswordUser();
        forgotPasswordUser.setEmail("email");
        User user = new User("email@email.com", "password");
        user.setRegistrationKey("1234");
        when(userService.updateRegistrationKey((ForgotPasswordUser) any(ForgotPasswordUser.class))).thenReturn(user);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/reset-password-request")
                .header("host", "http://sphynx.dev")
                .flashAttr("forgotPasswordUser", forgotPasswordUser))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_RESET_PASSWORD_REQUEST.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testSendPasswordResetLink_userNotFound() throws Exception {
        ForgotPasswordUser forgotPasswordUser = new ForgotPasswordUser();
        forgotPasswordUser.setEmail("email@email.com");
        when(userService.updateRegistrationKey((ForgotPasswordUser) any(ForgotPasswordUser.class))).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/reset-password-request")
                .header("host", "http://sphynx.dev")
                .flashAttr("forgotPasswordUser", forgotPasswordUser))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.ERROR.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testShowResetPasswordPage() throws Exception {
        when(userService.verifyUser("email@email.com", "key")).thenReturn(new User());
        this.mockMvc.perform(MockMvcRequestBuilders
              .get("/user/reset-password")
              .param("email", "email@email.com")
              .param("key", "key")
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(view().name(SiteMap.USER_RESET_PASSWORD.getPath()))
              .andExpect(unauthenticated());
        
    }
    
    @Test
    public void testShowResetPasswordPage_userNotFound() throws Exception {
        when(userService.verifyUser("email@email.com", "key")).thenReturn(null);
        this.mockMvc.perform(MockMvcRequestBuilders
              .get("/user/reset-password")
              .param("email", "email@email.com")
              .param("key", "key")
              .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(view().name(SiteMap.ERROR.getPath()))
              .andExpect(unauthenticated());
        
    }
    
    @Test
    public void testResetPassword() throws Exception {
        when(userService.updatePassword((ResetPasswordUser) any(ResetPasswordUser.class))).thenReturn(true);
        ResetPasswordUser resetPasswordUser = new ResetPasswordUser("email@email.com", "1234");
        resetPasswordUser.setPassword("password");
        resetPasswordUser.setConfirmedPassword("password");
        
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/reset-password")
                .flashAttr("resetPasswordUser", resetPasswordUser)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(view().name(SiteMap.USER_RESET_PASSWORD_CONFIRMATION.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testResetPassword_bindingError() throws Exception {
        ResetPasswordUser resetPasswordUser = new ResetPasswordUser("email@email.com", "1234");
        resetPasswordUser.setPassword("password");
        resetPasswordUser.setConfirmedPassword("wrong");
        
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/reset-password")
                .flashAttr("resetPasswordUser", resetPasswordUser)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name(SiteMap.USER_RESET_PASSWORD.getPath()))
                .andExpect(unauthenticated());
    }
    
    @Test
    public void testResetPassword_updateFailed() throws Exception {
        when(userService.updatePassword((ResetPasswordUser) any(ResetPasswordUser.class))).thenReturn(false);
        ResetPasswordUser resetPasswordUser = new ResetPasswordUser("email@email.com", "1234");
        resetPasswordUser.setPassword("password");
        resetPasswordUser.setConfirmedPassword("password");
        
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/user/reset-password")
                .flashAttr("resetPasswordUser", resetPasswordUser)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(view().name(SiteMap.ERROR.getPath()))
                .andExpect(unauthenticated());
    }
    
    
    

}
