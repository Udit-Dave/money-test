package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.util.MoneyPrintUtil;
import com.example.util.MoneyWriteUtil;
import java.math.BigDecimal;

@Service
public class MoneyConversionService {

    private final MoneyPrintUtil moneyPrintUtil;
    private final MoneyWriteUtil moneyWriteUtil;

    @Autowired
    public MoneyConversionService(MoneyPrintUtil moneyPrintUtil, MoneyWriteUtil moneyWriteUtil) {
        this.moneyPrintUtil = moneyPrintUtil;
        this.moneyWriteUtil = moneyWriteUtil;
    }

    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        // Hypothetical conversion logic
        double conversionRate = getConversionRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(BigDecimal.valueOf(conversionRate));
        
        // Utilize MoneyPrintUtil to print the conversion result
        moneyPrintUtil.printConversion(amount, fromCurrency, convertedAmount, toCurrency);
        
        // Utilize MoneyWriteUtil to write the conversion result
        moneyWriteUtil.writeConversionResult(amount, fromCurrency, convertedAmount, toCurrency);
        
        return convertedAmount;
    }

    private double getConversionRate(String fromCurrency, String toCurrency) {
        // This method would typically involve calling an external API or database
        // For demonstration, we'll use a placeholder value
        return 1.2; // Example conversion rate
    }
}