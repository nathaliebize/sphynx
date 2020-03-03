package com.nathaliebize.sphynx.configuration;

import static org.junit.Assert.*;
import org.junit.Test;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

public class ThymeleafConfigurationTest {

    ThymeleafConfiguration thymeleafConfiguration = new ThymeleafConfiguration();
    
    @Test
    public void testTemplateEngine() {
        SpringTemplateEngine template = thymeleafConfiguration.templateEngine();
        
        assertNotNull(template);
        assertNotNull(template.getTemplateResolvers());
        assertTrue(template.getEnableSpringELCompiler());
        assertNotNull(template.getDialects());
    }

    @Test
    public void testThymeleafTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = thymeleafConfiguration.thymeleafTemplateResolver();
        
        assertNotNull(templateResolver);
        assertEquals("classpath:templates/", templateResolver.getPrefix());
        assertEquals(".html", templateResolver.getSuffix());
        assertFalse(templateResolver.isCacheable());
        assertEquals(TemplateMode.HTML, templateResolver.getTemplateMode());
    }
    
    @Test
    public void testThymeleafViewResolver() {
        ThymeleafViewResolver viewResolver = thymeleafConfiguration.thymeleafViewResolver();
        
        assertNotNull(viewResolver.getTemplateEngine());
        assertEquals("UTF-8", viewResolver.getCharacterEncoding());
    }
}
