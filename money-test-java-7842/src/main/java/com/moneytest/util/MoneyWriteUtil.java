package com.moneytest.util;

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
        this.mwMlx = 0;
        this.mwWordText = "";
        this.mwWcx = 0;
        this.mwGx = 0;
        this.mwIx1 = 0;
        this.mwIx2 = 0;
    }

    public void setAmount(BigDecimal amount) {
        this.mwAmount = amount;
        updateAmountFields();
    }

    private void updateAmountFields() {
        long dollars = mwAmount.longValue();
        this.mwAmountCents = mwAmount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        for (int i = MW_GLIM - 1; i >= 0; i--) {
            this.mwAmountGroup[i] = (int) (dollars % 1000);
            dollars /= 1000;
        }
    }

    // Add methods for processing the amount and generating the text representation
    // These methods would implement the logic that was originally in the COBOL program

    // Example method (you would need to implement more based on the COBOL logic):
    private void processGroup(int groupIndex) {
        int groupValue = this.mwAmountGroup[groupIndex];
        if (groupValue == 0) return;

        // Process hundreds
        int hundreds = groupValue / 100;
        if (hundreds > 0) {
            addWord(MW_ONES_WORDS[hundreds - 1]);
            addWord("HUNDRED");
        }

        // Process tens and ones
        int tensAndOnes = groupValue % 100;
        if (tensAndOnes >= 20) {
            addWord(MW_TENS_WORDS[tensAndOnes / 10 - 1]);
            if (tensAndOnes % 10 > 0) {
                addWord(MW_ONES_WORDS[tensAndOnes % 10 - 1]);
            }
        } else if (tensAndOnes > 0) {
            addWord(MW_ONES_WORDS[tensAndOnes - 1]);
        }

        // Add group word (e.g., "MILLION", "THOUSAND")
        if (groupIndex < MW_GLIM - 1) {
            addWord(MW_GROUP_WORDS[groupIndex]);
        }
    }

    private void addWord(String word) {
        // Implementation to add a word to the output
        // This would update mwMoney, mwMcx, etc.
    }

    // Other necessary methods...
}