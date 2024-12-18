package com.example.controller;

import com.example.service.MoneyConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/money-conversion")
@Tag(name = "Money Conversion", description = "API for money conversion operations")
public class MoneyConversionController {

    private final MoneyConversionService moneyConversionService;

    @Autowired
    public MoneyConversionController(MoneyConversionService moneyConversionService) {
        this.moneyConversionService = moneyConversionService;
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert dollar amount", description = "Converts a given dollar amount to its text representation")
    @ApiResponse(responseCode = "200", description = "Successful conversion")
    public ResponseEntity<String> convertDollarAmount(
            @Parameter(description = "Dollar amount to convert (up to tttbbbmmmtttooo.cc)")
            @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.ok("Conversion process ended.");
        }

        String[] convertedLines = moneyConversionService.convertAmount(amount);
        StringBuilder response = new StringBuilder("Converted Dollar Amount:\n");
        for (String line : convertedLines) {
            response.append(line).append("\n");
        }

        return ResponseEntity.ok(response.toString());
    }
}