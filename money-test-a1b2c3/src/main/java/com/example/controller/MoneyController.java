package com.example.controller

import com.example.service.MoneyConversionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/money")
@Tag(name = "Money Controller", description = "API endpoints for money conversion operations")
class MoneyController(private val moneyConversionService: MoneyConversionService) {

    @GetMapping("/convert")
    @Operation(
        summary = "Convert money",
        description = "Convert money from one currency to another",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Successful conversion",
                content = [Content(schema = Schema(implementation = BigDecimal::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input")
        ]
    )
    fun convertMoney(
        @Parameter(description = "Amount to convert", required = true)
        @RequestParam amount: BigDecimal,
        @Parameter(description = "Source currency", required = true)
        @RequestParam fromCurrency: String,
        @Parameter(description = "Target currency", required = true)
        @RequestParam toCurrency: String
    ): ResponseEntity<BigDecimal> {
        val convertedAmount = moneyConversionService.convert(amount, fromCurrency, toCurrency)
        return ResponseEntity.ok(convertedAmount)
    }

    @PostMapping("/validate")
    @Operation(
        summary = "Validate currency",
        description = "Validate if the given currency code is supported",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Currency validation result",
                content = [Content(schema = Schema(implementation = Boolean::class))]
            )
        ]
    )
    fun validateCurrency(
        @Parameter(description = "Currency code to validate", required = true)
        @RequestBody currency: String
    ): ResponseEntity<Boolean> {
        val isValid = moneyConversionService.isValidCurrency(currency)
        return ResponseEntity.ok(isValid)
    }
}