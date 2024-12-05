package com.example.controller;

import com.example.service.MoneyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/money")
@Tag(name = "Money Conversion", description = "API for converting numeric amounts to text representation")
public class MoneyController {

    private final MoneyConversionService moneyConversionService;

    @Autowired
    public MoneyController(MoneyConversionService moneyConversionService) {
        this.moneyConversionService = moneyConversionService;
    }

    @GetMapping("/convert")
    @Operation(summary = "Convert amount to text", description = "Converts a numeric dollar amount to its text representation")
    @ApiResponse(responseCode = "200", description = "Successful conversion")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<String> convertMoneyToText(
            @Parameter(description = "Amount to convert (up to $999,999,999,999,999.99)", required = true)
            @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(new BigDecimal("999999999999999.99")) > 0) {
            return ResponseEntity.badRequest().body("Invalid amount. Please enter a value between 0 and 999,999,999,999,999.99");
        }

        String result = moneyConversionService.convertToText(amount);
        return ResponseEntity.ok(result);
    }
}