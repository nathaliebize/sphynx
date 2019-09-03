package com.nathaliebize.sphynx.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nathaliebize.sphynx.controller.SiteMap;
import com.nathaliebize.sphynx.service.SphynxUserDetailsService;

/**
 * The SecurityConfiguration sets the security configuration settings.
 * 
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private SphynxUserDetailsService userDetailsService;
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new Pbkdf2PasswordEncoder("JDfidlqmepzRE34fdjsklWWrIfj"));
        provider.setAuthoritiesMapper(authoritiesMapper());
        return provider;
    }
    
    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setDefaultAuthority("USER");
        return authorityMapper;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/", "/index", "/css/*", "/terms", "/user/*", "/error").permitAll()
            .antMatchers(HttpMethod.POST, "/user/*").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage(SiteMap.USER_LOGIN.getPath()).permitAll()
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/sites", true)
            .and()
            .logout().invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher((new AntPathRequestMatcher("/logout")))
            .logoutSuccessUrl("/");
    }
}
