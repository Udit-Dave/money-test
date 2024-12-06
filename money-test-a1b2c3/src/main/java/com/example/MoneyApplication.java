package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main entry point for the Money Application.
 * This class initializes the Spring Boot application and enables Swagger documentation.
 */
@SpringBootApplication
@EnableSwagger2
public class MoneyApplication {

    /**
     * The main method which serves as the entry point for the Spring Boot application.
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
    }
}