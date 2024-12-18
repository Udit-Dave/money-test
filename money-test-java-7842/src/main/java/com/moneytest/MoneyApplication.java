package com.moneytest;

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

    private static BigDecimal wsAmount = BigDecimal.ONE;

    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
        process();
    }

    private static void process() {
        Scanner scanner = new Scanner(System.in);

        while (wsAmount.compareTo(BigDecimal.ZERO) != 0) {
            System.out.println("\nC O N V E R T E D   D O L L A R   A M O U N T");
            // Assuming MoneyWriter class is implemented to handle MW-MONEY-LINE
            for (int i = 1; i <= 6; i++) {
                System.out.println(MoneyWriter.getMoneyLine(i));
            }

            System.out.print("Enter up to tttbbbmmmtttooo.cc (0 to exit): ");
            String input = scanner.nextLine();

            try {
                wsAmount = new BigDecimal(input);
                if (wsAmount.compareTo(BigDecimal.ZERO) != 0) {
                    // Assuming MoneyConversionService is implemented to handle the conversion
                    MoneyConversionService.convertMoney(wsAmount);
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
                .apis(RequestHandlerSelectors.basePackage("com.moneytest"))
                .paths(PathSelectors.any())
                .build();
    }
}