package com.example.dtsec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DtsecApplication implements WebMvcConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(DtsecApplication.class);

	public static void main(String[] args) {
		logger.info("Initialisiere Applikation");
		SpringApplication.run(DtsecApplication.class, args);
	}

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    }

}
