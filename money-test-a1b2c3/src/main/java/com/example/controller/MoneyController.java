package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.service.MoneyConversionService;
import io.swagger.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/money")
@Api(value = "Money Conversion Controller", description = "Operations for converting money amounts to text")
public class MoneyController {

    private final MoneyConversionService moneyConversionService;

    @Autowired
    public MoneyController(MoneyConversionService moneyConversionService) {
        this.moneyConversionService = moneyConversionService;
    }

    @PostMapping("/convert")
    @ApiOperation(value = "Convert money amount to text", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully converted amount"),
        @ApiResponse(code = 400, message = "Invalid input")
    })
    public List<String> convertMoneyToText(
            @ApiParam(value = "Amount to convert", required = true) @RequestBody BigDecimal amount) {
        return moneyConversionService.convertToText(amount);
    }

    @GetMapping("/display")
    @ApiOperation(value = "Display converted money amount", response = String.class)
    public String displayConvertedAmount(
            @ApiParam(value = "Amount to convert", required = true) @RequestParam BigDecimal amount) {
        List<String> convertedLines = moneyConversionService.convertToText(amount);
        StringBuilder display = new StringBuilder();
        display.append("C O N V E R T E D   D O L L A R   A M O U N T\n\n");
        for (int i = 0; i < convertedLines.size(); i++) {
            display.append(convertedLines.get(i)).append("\n");
        }
        return display.toString();
    }
}