package com.example.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting and printing money-related information.
 */
public class MoneyPrintUtil {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    /**
     * Formats the given amount as a currency string.
     * 
     * @param amount The amount to format.
     * @return A formatted currency string.
     */
    public static String formatAsCurrency(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        return CURRENCY_FORMAT.format(amount);
    }

    /**
     * Prints the formatted currency amount to the console.
     * 
     * @param amount The amount to print.
     */
    public static void printCurrency(BigDecimal amount) {
        System.out.println("Currency amount: " + formatAsCurrency(amount));
    }

    /**
     * Formats the given amount as a percentage string.
     * 
     * @param amount The amount to format as a percentage.
     * @return A formatted percentage string.
     */
    public static String formatAsPercentage(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        return percentFormat.format(amount);
    }

    /**
     * Prints the formatted percentage to the console.
     * 
     * @param amount The amount to print as a percentage.
     */
    public static void printPercentage(BigDecimal amount) {
        System.out.println("Percentage: " + formatAsPercentage(amount));
    }
}