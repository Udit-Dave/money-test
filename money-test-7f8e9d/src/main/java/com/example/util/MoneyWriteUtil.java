package com.example.util;

import java.math.BigDecimal;
import java.util.Arrays;

public class MoneyWriteUtil {
    private static final int MW_GLIM = 5;
    private static final int MW_MCLIM = 50;
    private static final int MW_MLLIM = 6;

    private BigDecimal mwAmount;
    private int[] mwAmountGroup;
    private int mwAmountCents;
    private int mwWork;
    private char[] mwMoney;
    private int mwMcx;
    private String[] mwMoneyLines;
    private int mwMlx;
    private String mwWordText;
    private int mwWcx;
    private int mwGx;
    private int mwIx1;
    private int mwIx2;

    private static final String[] MW_GROUP_WORDS = {
        "TRILLION", "BILLION ", "MILLION ", "THOUSAND", "         "
    };

    private static final String[] MW_TENS_WORDS = {
        "TEN    ", "TWENTY ", "THIRTY ", "FORTY  ", "FIFTY  ",
        "SIXTY  ", "SEVENTY", "EIGHTY ", "NINETY "
    };

    private static final String[] MW_ONES_WORDS = {
        "ONE      ", "TWO      ", "THREE    ", "FOUR     ", "FIVE     ",
        "SIX      ", "SEVEN    ", "EIGHT    ", "NINE     ", "TEN      ",
        "ELEVEN   ", "TWELVE   ", "THIRTEEN ", "FOURTEEN ", "FIFTEEN  ",
        "SIXTEEN  ", "SEVENTEEN", "EIGHTEEN ", "NINETEEN "
    };

    public MoneyWriteUtil() {
        this.mwAmount = BigDecimal.ZERO;
        this.mwAmountGroup = new int[MW_GLIM];
        this.mwAmountCents = 0;
        this.mwWork = 0;
        this.mwMoney = new char[MW_MCLIM];
        this.mwMcx = 0;
        this.mwMoneyLines = new String[MW_MLLIM];
        Arrays.fill(this.mwMoneyLines, "");
        this.mwMlx = 0;
        this.mwWordText = "";
        this.mwWcx = 0;
        this.mwGx = 0;
        this.mwIx1 = 0;
        this.mwIx2 = 0;
    }

    public void setAmount(BigDecimal amount) {
        this.mwAmount = amount;
        updateAmountGroups();
    }

    private void updateAmountGroups() {
        BigDecimal dollars = this.mwAmount.setScale(0, BigDecimal.ROUND_DOWN);
        this.mwAmountCents = this.mwAmount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        for (int i = MW_GLIM - 1; i >= 0; i--) {
            this.mwAmountGroup[i] = dollars.remainder(BigDecimal.valueOf(1000)).intValue();
            dollars = dollars.divide(BigDecimal.valueOf(1000));
        }
    }

    public String[] getMoneyLines() {
        return this.mwMoneyLines;
    }

    // Add other methods as needed to replicate the functionality of the COBOL program

    // For example:
    private void addWord(String word) {
        // Implementation to add a word to mwMoney
    }

    private void startNewLine() {
        // Implementation to start a new line in mwMoneyLines
    }

    public void convertToWords() {
        // Implementation of the main conversion logic
        // This method would use the various fields and helper methods to convert the amount to words
    }
}