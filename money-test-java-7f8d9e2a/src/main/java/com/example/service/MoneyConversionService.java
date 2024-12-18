package com.example.service;

import org.springframework.stereotype.Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MoneyConversionService {

    private static final int MW_MCLIM = 50;
    private static final int MW_MLLIM = 6;

    private BigDecimal amount;
    private StringBuilder money;
    private List<String> moneyLines;
    private int mcx;
    private int mlx;

    private static final String[] ONES_VALUES = {
        "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
        "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"
    };

    private static final String[] TENS_VALUES = {
        "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
    };

    @ApiOperation(value = "Convert numeric amount to text representation")
    public List<String> convertToWords(@ApiParam(value = "Amount to convert", required = true) BigDecimal amount) {
        this.amount = amount;
        this.money = new StringBuilder();
        this.moneyLines = new ArrayList<>();
        this.mcx = 0;
        this.mlx = 0;

        processAmount();

        return moneyLines;
    }

    private void processAmount() {
        long dollars = amount.longValue();
        int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        if (dollars == 0) {
            addWord("NO");
        }

        processGroup(dollars / 1_000_000_000_000L, "TRILLION");
        processGroup((dollars / 1_000_000_000) % 1000, "BILLION");
        processGroup((dollars / 1_000_000) % 1000, "MILLION");
        processGroup((dollars / 1000) % 1000, "THOUSAND");
        processGroup(dollars % 1000, "");

        addWord(dollars == 1 ? "DOLLAR" : "DOLLARS");
        addWord("AND");

        if (cents == 0) {
            addWord("NO");
        } else {
            processTwoDigits(cents);
        }

        addWord(cents == 1 ? "CENT**" : "CENTS**");

        finalizeMoneyLine();
    }

    private void processGroup(long value, String groupName) {
        if (value > 0) {
            processThreeDigits((int) value);
            if (!groupName.isEmpty()) {
                addWord(groupName);
            }
        }
    }

    private void processThreeDigits(int value) {
        int hundreds = value / 100;
        int remainder = value % 100;

        if (hundreds > 0) {
            addWord(ONES_VALUES[hundreds - 1]);
            addWord("HUNDRED");
        }

        processTwoDigits(remainder);
    }

    private void processTwoDigits(int value) {
        if (value >= 20) {
            addWord(TENS_VALUES[value / 10 - 1]);
            if (value % 10 > 0) {
                addWord(ONES_VALUES[value % 10 - 1]);
            }
        } else if (value > 0) {
            addWord(ONES_VALUES[value - 1]);
        }
    }

    private void addWord(String word) {
        if (mcx + word.length() + 3 > MW_MCLIM) {
            finalizeMoneyLine();
        }

        if (mcx == 0) {
            money.append("**");
            mcx = 2;
        } else {
            money.append(" ");
            mcx++;
        }

        money.append(word);
        mcx += word.length();
    }

    private void finalizeMoneyLine() {
        if (mcx > 0) {
            money.append("**");
            int padding = (MW_MCLIM - mcx) / 2;
            moneyLines.add(" ".repeat(padding) + money.toString());
            money.setLength(0);
            mcx = 0;
            mlx++;
        }
    }
}