package com.example.util;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Utility class for writing monetary amounts as words.
 */
public class MoneyWriter {

    private static final String[] LESS_THAN_20 = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private static final String[] TENS = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    private static final String[] THOUSANDS = {"", "Thousand", "Million", "Billion"};

    private final DecimalFormat decimalFormat;

    public MoneyWriter() {
        this.decimalFormat = new DecimalFormat("00");
    }

    /**
     * Writes the given amount as words to the provided Writer.
     *
     * @param writer The Writer to write the amount to.
     * @param amount The monetary amount to be written as words.
     * @throws IOException If an I/O error occurs.
     */
    public void write(Writer writer, BigDecimal amount) throws IOException {
        String[] parts = amount.toString().split("\\.");
        long dollars = Long.parseLong(parts[0]);
        int cents = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

        if (dollars == 0 && cents == 0) {
            writer.write("Zero dollars");
            return;
        }

        if (dollars > 0) {
            writer.write(convertToWords(dollars));
            writer.write(dollars == 1 ? " dollar" : " dollars");
        }

        if (cents > 0) {
            if (dollars > 0) {
                writer.write(" and ");
            }
            writer.write(convertToWords(cents));
            writer.write(cents == 1 ? " cent" : " cents");
        }
    }

    private String convertToWords(long number) {
        if (number == 0) {
            return "";
        }

        if (number < 20) {
            return LESS_THAN_20[(int) number];
        }

        if (number < 100) {
            return TENS[(int) (number / 10)] + (number % 10 != 0 ? " " + convertToWords(number % 10) : "");
        }

        if (number < 1000) {
            return LESS_THAN_20[(int) (number / 100)] + " Hundred" + (number % 100 != 0 ? " " + convertToWords(number % 100) : "");
        }

        for (int i = 3; i >= 0; i--) {
            if (number >= Math.pow(1000, i)) {
                return convertToWords(number / (long) Math.pow(1000, i)) + " " + THOUSANDS[i] + 
                       (number % (long) Math.pow(1000, i) != 0 ? " " + convertToWords(number % (long) Math.pow(1000, i)) : "");
            }
        }

        return "";
    }

    /**
     * Formats the cents part of the amount.
     *
     * @param cents The cents to format.
     * @return A formatted string representation of the cents.
     */
    private String formatCents(int cents) {
        return decimalFormat.format(cents);
    }
}