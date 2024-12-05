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

    private static BigDecimal wsAmount = BigDecimal.ONE;

    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
        controlProcess();
    }

    private static void controlProcess() {
        while (wsAmount.compareTo(BigDecimal.ZERO) != 0) {
            process();
        }
    }

    private static void process() {
        System.out.println("\nC O N V E R T E D   D O L L A R   A M O U N T");
        // Assuming MoneyUtils class has been created with the necessary methods
        System.out.println(MoneyUtils.getMoneyLine(1));
        System.out.println(MoneyUtils.getMoneyLine(2));
        System.out.println(MoneyUtils.getMoneyLine(3));
        System.out.println(MoneyUtils.getMoneyLine(4));
        System.out.println(MoneyUtils.getMoneyLine(5));
        System.out.println(MoneyUtils.getMoneyLine(6));

        System.out.print("Enter up to tttbbbmmmtttooo.cc (0 to exit): ");
        Scanner scanner = new Scanner(System.in);
        wsAmount = new BigDecimal(scanner.nextLine());

        MoneyUtils.setAmount(wsAmount);
        MoneyUtils.convertMoney();
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