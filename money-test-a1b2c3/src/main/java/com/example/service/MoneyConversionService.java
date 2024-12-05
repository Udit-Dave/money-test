package com.example.service;

import org.springframework.stereotype.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Api(tags = "Money Conversion Service")
public class MoneyConversionService {

    private static final String[] ONES_WORDS = {
        "", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
        "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"
    };

    private static final String[] TENS_WORDS = {
        "", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
    };

    private static final String[] GROUP_WORDS = {
        "", "THOUSAND", "MILLION", "BILLION", "TRILLION"
    };

    @ApiOperation(value = "Convert numeric money amount to text", notes = "Converts a numeric money amount into one or more text lines for printing on checks")
    public List<String> convertMoneyToText(BigDecimal amount) {
        List<String> moneyLines = new ArrayList<>();
        StringBuilder moneyText = new StringBuilder();

        long dollars = amount.longValue();
        int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        if (dollars == 0) {
            appendWord(moneyText, "NO");
        } else {
            convertGroup(moneyText, dollars);
        }

        appendWord(moneyText, dollars == 1 ? "DOLLAR" : "DOLLARS");
        appendWord(moneyText, "AND");

        if (cents == 0) {
            appendWord(moneyText, "NO");
        } else {
            convertTwoDigits(moneyText, cents);
        }

        appendWord(moneyText, cents == 1 ? "CENT" : "CENTS");

        // Center the text and add protection
        String centeredText = centerText(moneyText.toString());
        moneyLines.add(centeredText);

        return moneyLines;
    }

    private void convertGroup(StringBuilder sb, long number) {
        for (int i = GROUP_WORDS.length - 1; i >= 0; i--) {
            long groupValue = number / (long) Math.pow(1000, i);
            if (groupValue > 0) {
                convertThreeDigits(sb, groupValue);
                if (!GROUP_WORDS[i].isEmpty()) {
                    appendWord(sb, GROUP_WORDS[i]);
                }
            }
            number %= (long) Math.pow(1000, i);
        }
    }

    private void convertThreeDigits(StringBuilder sb, long number) {
        int hundreds = (int) (number / 100);
        int remainder = (int) (number % 100);

        if (hundreds > 0) {
            appendWord(sb, ONES_WORDS[hundreds]);
            appendWord(sb, "HUNDRED");
        }

        convertTwoDigits(sb, remainder);
    }

    private void convertTwoDigits(StringBuilder sb, int number) {
        if (number >= 20) {
            appendWord(sb, TENS_WORDS[number / 10]);
            if (number % 10 > 0) {
                appendWord(sb, ONES_WORDS[number % 10]);
            }
        } else if (number > 0) {
            appendWord(sb, ONES_WORDS[number]);
        }
    }

    private void appendWord(StringBuilder sb, String word) {
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(word);
    }

    private String centerText(String text) {
        int totalWidth = 80; // Assuming a line width of 80 characters
        int textLength = text.length();
        int padding = (totalWidth - textLength) / 2;
        return "**" + " ".repeat(Math.max(0, padding - 2)) + text + " ".repeat(Math.max(0, padding - 2)) + "**";
    }
}