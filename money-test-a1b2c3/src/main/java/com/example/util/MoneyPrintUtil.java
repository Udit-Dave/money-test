package com.example.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting and printing monetary values.
 */
public class MoneyPrintUtil {

    private static final String DEFAULT_CURRENCY = "USD";
    private static final Locale DEFAULT_LOCALE = Locale.US;

    private MoneyPrintUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Formats a BigDecimal amount as a currency string.
     *
     * @param amount The monetary amount to format
     * @return A formatted currency string
     */
    public static String formatMoney(BigDecimal amount) {
        return formatMoney(amount, DEFAULT_CURRENCY, DEFAULT_LOCALE);
    }

    /**
     * Formats a BigDecimal amount as a currency string with specified currency and locale.
     *
     * @param amount   The monetary amount to format
     * @param currency The currency code (e.g., "USD", "EUR")
     * @param locale   The locale to use for formatting
     * @return A formatted currency string
     */
    public static String formatMoney(BigDecimal amount, String currency, Locale locale) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(java.util.Currency.getInstance(currency));
        return currencyFormatter.format(amount);
    }

    /**
     * Prints a formatted monetary value to the console.
     *
     * @param amount The monetary amount to print
     */
    public static void printMoney(BigDecimal amount) {
        System.out.println(formatMoney(amount));
    }

    /**
     * Prints a formatted monetary value with specified currency and locale to the console.
     *
     * @param amount   The monetary amount to print
     * @param currency The currency code (e.g., "USD", "EUR")
     * @param locale   The locale to use for formatting
     */
    public static void printMoney(BigDecimal amount, String currency, Locale locale) {
        System.out.println(formatMoney(amount, currency, locale));
    }
}