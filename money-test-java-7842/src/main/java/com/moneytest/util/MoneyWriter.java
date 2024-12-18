package com.moneytest.util;

import java.math.BigDecimal;
import java.util.Arrays;

public class MoneyWriter {
    private static final int MW_GLIM = 5;
    private static final int MW_MCLIM = 50;
    private static final int MW_MLLIM = 6;

    private BigDecimal amount;
    private int[] amountGroup;
    private int amountCents;
    private char[] moneyChar;
    private String[] moneyLine;
    private String[] groupWords;
    private String[] tensWords;
    private String[] onesWords;

    public MoneyWriter() {
        this.amount = BigDecimal.ZERO;
        this.amountGroup = new int[MW_GLIM];
        this.amountCents = 0;
        this.moneyChar = new char[MW_MCLIM];
        this.moneyLine = new String[MW_MLLIM];
        
        this.groupWords = new String[]{
            "TRILLION", "BILLION", "MILLION", "THOUSAND", ""
        };
        
        this.tensWords = new String[]{
            "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY",
            "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
        };
        
        this.onesWords = new String[]{
            "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
            "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN",
            "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"
        };
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        updateAmountGroups();
    }

    private void updateAmountGroups() {
        long dollars = amount.longValue();
        this.amountCents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();
        
        for (int i = MW_GLIM - 1; i >= 0; i--) {
            this.amountGroup[i] = (int) (dollars % 1000);
            dollars /= 1000;
        }
    }

    public String[] convertToWords() {
        Arrays.fill(moneyLine, "");
        int lineIndex = 0;
        int charIndex = 0;

        for (int groupIndex = 0; groupIndex < MW_GLIM; groupIndex++) {
            if (amountGroup[groupIndex] > 0) {
                processGroup(amountGroup[groupIndex], groupIndex);
                appendGroupWord(groupIndex);
            }

            if (charIndex + moneyChar.length > MW_MCLIM) {
                moneyLine[lineIndex] = new String(moneyChar).trim();
                lineIndex++;
                charIndex = 0;
                Arrays.fill(moneyChar, ' ');
            }
        }

        processCents();

        if (charIndex > 0) {
            moneyLine[lineIndex] = new String(moneyChar).trim();
        }

        return Arrays.copyOf(moneyLine, lineIndex + 1);
    }

    private void processGroup(int groupValue, int groupIndex) {
        int hundreds = groupValue / 100;
        int tens = (groupValue % 100) / 10;
        int ones = groupValue % 10;

        if (hundreds > 0) {
            appendWord(onesWords[hundreds - 1]);
            appendWord("HUNDRED");
        }

        if (tens == 1) {
            appendWord(onesWords[ones + 9]);
        } else {
            if (tens > 1) {
                appendWord(tensWords[tens - 1]);
            }
            if (ones > 0) {
                appendWord(onesWords[ones - 1]);
            }
        }
    }

    private void appendGroupWord(int groupIndex) {
        if (!groupWords[groupIndex].isEmpty()) {
            appendWord(groupWords[groupIndex]);
        }
    }

    private void processCents() {
        if (amountCents > 0) {
            appendWord("AND");
            appendWord(String.format("%02d", amountCents));
            appendWord("CENTS");
        }
    }

    private void appendWord(String word) {
        for (char c : word.toCharArray()) {
            moneyChar[getNextCharIndex()] = c;
        }
        moneyChar[getNextCharIndex()] = ' ';
    }

    private int getNextCharIndex() {
        for (int i = 0; i < moneyChar.length; i++) {
            if (moneyChar[i] == 0 || moneyChar[i] == ' ') {
                return i;
            }
        }
        return moneyChar.length - 1;
    }
}