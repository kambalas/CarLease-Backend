package com.example.sick.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurity implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Allows CORS requests to all endpoints
            .allowedOrigins("https://ci-cd-angular.onrender.com", "http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*");
  }


}
