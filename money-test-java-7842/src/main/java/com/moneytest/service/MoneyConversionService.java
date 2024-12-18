package com.moneytest.service;

import org.springframework.stereotype.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Api(tags = "Money Conversion Service")
public class MoneyConversionService {

    private static final int MW_MCLIM = 50;
    private static final int MW_MLLIM = 6;

    private BigDecimal amount;
    private char[] moneyChar;
    private String[] moneyLines;
    private int mcx;
    private int mlx;

    private static final String[] ONES_VALUES = {
        "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
        "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"
    };

    private static final String[] TENS_VALUES = {
        "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
    };

    @ApiOperation("Convert numeric amount to text representation")
    public List<String> convertToText(BigDecimal inputAmount) {
        this.amount = inputAmount;
        this.moneyChar = new char[MW_MCLIM];
        this.moneyLines = new String[MW_MLLIM];
        this.mcx = 0;
        this.mlx = 0;

        for (int i = 0; i < MW_MLLIM; i++) {
            moneyLines[i] = "";
        }

        processAmount();

        List<String> result = new ArrayList<>();
        for (String line : moneyLines) {
            if (!line.isEmpty()) {
                result.add(line.trim());
            }
        }
        return result;
    }

    private void processAmount() {
        long dollars = amount.longValue();
        int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        if (dollars >= 1_000_000_000_000L) {
            processThreeDigits(dollars / 1_000_000_000_000L);
            moveWord("TRILLION");
        }

        dollars %= 1_000_000_000_000L;
        if (dollars >= 1_000_000_000) {
            processThreeDigits(dollars / 1_000_000_000);
            moveWord("BILLION");
        }

        dollars %= 1_000_000_000;
        if (dollars >= 1_000_000) {
            processThreeDigits(dollars / 1_000_000);
            moveWord("MILLION");
        }

        dollars %= 1_000_000;
        if (dollars >= 1000) {
            processThreeDigits(dollars / 1000);
            moveWord("THOUSAND");
        }

        dollars %= 1000;
        if (dollars > 0) {
            processThreeDigits(dollars);
        }

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            moveWord("NO");
        }

        moveWord(dollars == 1 ? "DOLLAR" : "DOLLARS");
        moveWord("AND");

        if (cents == 0) {
            moveWord("NO");
        } else {
            processTwoDigits(cents);
        }

        moveWord(cents == 1 ? "CENT**" : "CENTS**");

        finalizeMoneyLine();
    }

    private void processThreeDigits(long number) {
        int hundreds = (int) (number / 100);
        int remainder = (int) (number % 100);

        if (hundreds > 0) {
            moveWord(ONES_VALUES[hundreds - 1]);
            moveWord("HUNDRED");
        }

        processTwoDigits(remainder);
    }

    private void processTwoDigits(int number) {
        if (number >= 20) {
            moveWord(TENS_VALUES[number / 10 - 1]);
            if (number % 10 > 0) {
                moveWord(ONES_VALUES[number % 10 - 1]);
            }
        } else if (number > 0) {
            moveWord(ONES_VALUES[number - 1]);
        }
    }

    private void moveWord(String word) {
        if (mcx + word.length() + 3 > MW_MCLIM) {
            finalizeMoneyLine();
        }

        if (mcx == 0) {
            moneyChar[mcx++] = '*';
            moneyChar[mcx++] = '*';
        } else {
            moneyChar[mcx++] = ' ';
        }

        for (char c : word.toCharArray()) {
            moneyChar[mcx++] = c;
        }
    }

    private void finalizeMoneyLine() {
        if (mcx > 0) {
            moneyChar[mcx++] = '*';
            moneyChar[mcx++] = '*';
            String line = new String(moneyChar, 0, mcx).trim();
            moneyLines[mlx++] = String.format("%-" + MW_MCLIM + "s", line);
            mcx = 0;
        }
    }
}