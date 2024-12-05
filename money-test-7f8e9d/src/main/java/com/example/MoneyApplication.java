package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.CommandLineRunner;
import com.example.service.MoneyConversionService;
import com.example.util.MoneyWriteUtil;
import java.math.BigDecimal;
import java.util.Scanner;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@OpenAPIDefinition(info = @Info(title = "Money Conversion API", version = "1.0", description = "API for converting dollar amounts"))
public class MoneyApplication implements CommandLineRunner {

    private final MoneyConversionService moneyConversionService;
    private final MoneyWriteUtil moneyWriteUtil;

    public MoneyApplication(MoneyConversionService moneyConversionService, MoneyWriteUtil moneyWriteUtil) {
        this.moneyConversionService = moneyConversionService;
        this.moneyWriteUtil = moneyWriteUtil;
    }

    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal amount = BigDecimal.ONE;

        while (amount.compareTo(BigDecimal.ZERO) != 0) {
            process(scanner);
            System.out.print("Enter up to tttbbbmmmtttooo.cc (0 to exit): ");
            String input = scanner.nextLine();
            try {
                amount = new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }
        }

        scanner.close();
    }

    private void process(Scanner scanner) {
        System.out.println("\nC O N V E R T E D   D O L L A R   A M O U N T");
        for (int i = 0; i < 6; i++) {
            System.out.println(moneyWriteUtil.getMoneyLine(i));
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(MoneyApplication app) {
        return args -> app.run(args);
    }
}