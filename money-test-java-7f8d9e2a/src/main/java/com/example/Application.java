package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Money Conversion API",
        version = "1.0",
        description = "API for converting monetary amounts to text representation"
    )
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}