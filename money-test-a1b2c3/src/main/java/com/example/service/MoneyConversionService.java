package com.example.service;

import org.springframework.stereotype.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Service
@Api(tags = "Money Conversion Service")
public class MoneyConversionService {

    private static final int MAX_LINE_LENGTH = 80;
    private static final String[] ONES_WORDS = {"", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
            "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
    private static final String[] TENS_WORDS = {"", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"};
    private static final String[] GROUP_WORDS = {"", "THOUSAND", "MILLION", "BILLION", "TRILLION"};

    @ApiOperation("Convert numeric money amount to text representation")
    public String[] convertMoneyToText(double amount) {
        StringBuilder moneyText = new StringBuilder();
        String[] moneyLines = new String[5]; // Assuming maximum 5 lines
        int lineIndex = 0;

        long dollars = (long) amount;
        int cents = (int) Math.round((amount - dollars) * 100);

        for (int groupIndex = 4; groupIndex >= 0; groupIndex--) {
            int groupValue = (int) (dollars / Math.pow(1000, groupIndex) % 1000);
            if (groupValue > 0) {
                appendGroup(moneyText, groupValue);
                if (!GROUP_WORDS[groupIndex].isEmpty()) {
                    appendWord(moneyText, GROUP_WORDS[groupIndex]);
                }
            }
        }

        if (dollars == 0) {
            appendWord(moneyText, "NO");
        }

        appendWord(moneyText, dollars == 1 ? "DOLLAR" : "DOLLARS");
        appendWord(moneyText, "AND");

        if (cents == 0) {
            appendWord(moneyText, "NO");
        } else {
            appendTwoDigits(moneyText, cents);
        }

        appendWord(moneyText, cents == 1 ? "CENT" : "CENTS");

        // Center the text and add protection
        String centeredText = centerText(moneyText.toString().trim());
        moneyLines[lineIndex++] = centeredText;

        return moneyLines;
    }

    private void appendGroup(StringBuilder sb, int value) {
        int hundreds = value / 100;
        int tens = value % 100;

        if (hundreds > 0) {
            appendWord(sb, ONES_WORDS[hundreds]);
            appendWord(sb, "HUNDRED");
        }

        appendTwoDigits(sb, tens);
    }

    private void appendTwoDigits(StringBuilder sb, int value) {
        if (value >= 20) {
            appendWord(sb, TENS_WORDS[value / 10]);
            if (value % 10 > 0) {
                appendWord(sb, ONES_WORDS[value % 10]);
            }
        } else if (value > 0) {
            appendWord(sb, ONES_WORDS[value]);
        }
    }

    private void appendWord(StringBuilder sb, String word) {
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(word);
    }

    private String centerText(String text) {
        int padding = (MAX_LINE_LENGTH - text.length()) / 2;
        return "**" + " ".repeat(Math.max(0, padding - 2)) + text +
               " ".repeat(Math.max(0, MAX_LINE_LENGTH - text.length() - padding - 4)) + "**";
    }
}