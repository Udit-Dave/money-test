package com.moneytest.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MoneyPrintUtil {

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

    public List<String> convertMoneyToWords(BigDecimal amount) {
        List<String> moneyLines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        long dollars = amount.longValue();
        int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        if (dollars == 0) {
            addWordToLine(currentLine, "NO", moneyLines);
        }

        for (int i = 4; i >= 0; i--) {
            int groupValue = (int) (dollars / Math.pow(1000, i) % 1000);
            if (groupValue > 0) {
                convertGroup(groupValue, currentLine, moneyLines);
                if (i > 0) {
                    addWordToLine(currentLine, GROUP_WORDS[i], moneyLines);
                }
            }
        }

        addWordToLine(currentLine, dollars == 1 ? "DOLLAR" : "DOLLARS", moneyLines);
        addWordToLine(currentLine, "AND", moneyLines);

        if (cents == 0) {
            addWordToLine(currentLine, "NO", moneyLines);
        } else {
            convertTwoDigits(cents, currentLine, moneyLines);
        }

        addWordToLine(currentLine, cents == 1 ? "CENT" : "CENTS", moneyLines);

        if (!currentLine.isEmpty()) {
            moneyLines.add(formatLine(currentLine.toString()));
        }

        return moneyLines;
    }

    private void convertGroup(int groupValue, StringBuilder currentLine, List<String> moneyLines) {
        int hundreds = groupValue / 100;
        int tens = groupValue % 100;

        if (hundreds > 0) {
            addWordToLine(currentLine, ONES_WORDS[hundreds], moneyLines);
            addWordToLine(currentLine, "HUNDRED", moneyLines);
        }

        convertTwoDigits(tens, currentLine, moneyLines);
    }

    private void convertTwoDigits(int value, StringBuilder currentLine, List<String> moneyLines) {
        if (value >= 20) {
            addWordToLine(currentLine, TENS_WORDS[value / 10], moneyLines);
            if (value % 10 > 0) {
                addWordToLine(currentLine, ONES_WORDS[value % 10], moneyLines);
            }
        } else if (value > 0) {
            addWordToLine(currentLine, ONES_WORDS[value], moneyLines);
        }
    }

    private void addWordToLine(StringBuilder currentLine, String word, List<String> moneyLines) {
        if (currentLine.length() + word.length() + 3 > 60) {
            moneyLines.add(formatLine(currentLine.toString()));
            currentLine.setLength(0);
        }
        if (currentLine.length() > 0) {
            currentLine.append(" ");
        }
        currentLine.append(word);
    }

    private String formatLine(String line) {
        StringBuilder formattedLine = new StringBuilder("** ");
        formattedLine.append(line);
        while (formattedLine.length() < 62) {
            formattedLine.append(" ");
        }
        formattedLine.append("**");
        return formattedLine.toString();
    }
}