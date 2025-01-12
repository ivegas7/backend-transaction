package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.interceptor.RateLimitInterceptor;

//Políticas de CORS
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    //Permitir o denegar el acceso de un dominio a recursos de otro dominio.
    
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Asegúrate de que sea el path correcto
                .allowedOrigins("http://localhost:3000")  // Permite solicitudes desde el frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  
                .allowedHeaders("*");  // Puedes restringir los encabezados 
    }
    
    
    private final RateLimitInterceptor rateLimitInterceptor;
    
    public WebConfig(RateLimitInterceptor rateLimitInterceptor) {
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**"); // Apply interceptor to all /api/* endpoints
    }
    
    
}
