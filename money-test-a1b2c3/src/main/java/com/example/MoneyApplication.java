package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
@EnableSwagger2
public class MoneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
        processMoneyConversion();
    }

    private static void processMoneyConversion() {
        Scanner scanner = new Scanner(System.in);
        BigDecimal amount = BigDecimal.ONE;

        while (amount.compareTo(BigDecimal.ZERO) != 0) {
            System.out.println("\nC O N V E R T E D   D O L L A R   A M O U N T");
            // Display money lines (these would be implemented in MoneyUtils)
            for (int i = 1; i <= 6; i++) {
                System.out.println(MoneyUtils.getMoneyLine(i));
            }

            System.out.print("Enter up to tttbbbmmmtttooo.cc (0 to exit): ");
            String input = scanner.nextLine();
            try {
                amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) != 0) {
                    // This would call the MoneyConversionService
                    String convertedAmount = MoneyConversionService.convertMoney(amount);
                    System.out.println("Converted amount: " + convertedAmount);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        scanner.close();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.example"))
          .paths(PathSelectors.any())
          .build();
    }
}