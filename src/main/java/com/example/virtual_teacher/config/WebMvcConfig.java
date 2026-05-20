package com.example.virtual_teacher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/contact.html", "/about/contacts");
        registry.addRedirectViewController("/about.html", "/about");
        registry.addRedirectViewController("/courses.html", "/courses");
        registry.addRedirectViewController("/teacher.html", "/about/teachers");
        registry.addRedirectViewController("/index.html", "/");
    }
}
