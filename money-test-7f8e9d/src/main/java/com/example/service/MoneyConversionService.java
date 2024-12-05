package com.example.service;

import org.springframework.stereotype.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Service
@Api(tags = "Money Conversion Service")
public class MoneyConversionService {

    private static final int MW_MCLIM = 50;
    private static final int MW_MLLIM = 6;

    private String[] moneyLines = new String[MW_MLLIM];
    private int mlx = 0;

    private String[] onesValues = {
        "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
        "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"
    };

    private String[] tensValues = {
        "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
    };

    @ApiOperation(value = "Convert numeric amount to words", notes = "Converts a numeric money amount into text lines for check printing")
    public String[] convertToWords(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }

        moneyLines = new String[MW_MLLIM];
        mlx = 0;
        StringBuilder money = new StringBuilder();

        long dollars = amount.longValue();
        int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        if (dollars == 0) {
            appendWord(money, "NO");
        } else {
            convertLargeNumber(money, dollars);
        }

        appendWord(money, dollars == 1 ? "DOLLAR" : "DOLLARS");
        appendWord(money, "AND");

        if (cents == 0) {
            appendWord(money, "NO");
        } else {
            convertTwoDigits(money, cents);
        }
        appendWord(money, cents == 1 ? "CENT**" : "CENTS**");

        finalizeLine(money);

        return moneyLines;
    }

    private void convertLargeNumber(StringBuilder money, long number) {
        long[] divisions = {1_000_000_000_000L, 1_000_000_000, 1_000_000, 1_000, 1};
        String[] names = {"TRILLION", "BILLION", "MILLION", "THOUSAND", ""};

        for (int i = 0; i < divisions.length; i++) {
            long part = number / divisions[i];
            if (part > 0) {
                convertThreeDigits(money, (int) part);
                if (!names[i].isEmpty()) {
                    appendWord(money, names[i]);
                }
            }
            number %= divisions[i];
        }
    }

    private void convertThreeDigits(StringBuilder money, int number) {
        int hundreds = number / 100;
        int remainder = number % 100;

        if (hundreds > 0) {
            appendWord(money, onesValues[hundreds - 1]);
            appendWord(money, "HUNDRED");
        }

        convertTwoDigits(money, remainder);
    }

    private void convertTwoDigits(StringBuilder money, int number) {
        if (number >= 20) {
            appendWord(money, tensValues[number / 10 - 1]);
            if (number % 10 > 0) {
                appendWord(money, onesValues[number % 10 - 1]);
            }
        } else if (number > 0) {
            appendWord(money, onesValues[number - 1]);
        }
    }

    private void appendWord(StringBuilder money, String word) {
        if (money.length() + word.length() + 3 > MW_MCLIM) {
            finalizeLine(money);
            money.setLength(0);
        }

        if (money.length() == 0) {
            money.append("**");
        } else {
            money.append(" ");
        }
        money.append(word);
    }

    private void finalizeLine(StringBuilder money) {
        if (money.length() > 0) {
            money.append("**");
            String line = money.toString();
            int padding = (MW_MCLIM - line.length()) / 2;
            moneyLines[mlx++] = " ".repeat(padding) + line;
        }
    }

    @ApiOperation(value = "Format amount", notes = "Formats the numeric amount with proper separators")
    public String formatAmount(BigDecimal amount) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(amount);
    }
}