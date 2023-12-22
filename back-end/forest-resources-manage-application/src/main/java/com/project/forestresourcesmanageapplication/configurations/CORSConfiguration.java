package com.project.forestresourcesmanageapplication.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfiguration {
    
    @Bean
	public WebMvcConfigurer corsConfiguration() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry corsRegistry) {
				corsRegistry.addMapping("/**").allowedMethods("*").allowedOrigins("http://localhost:8080","https://dkhang233.github.io");
			}
		};
	}
}
