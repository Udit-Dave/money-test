import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MoneyWriteUtilTest {

    private MoneyWriteUtil moneyWriteUtil;

    @BeforeEach
    void setUp() {
        moneyWriteUtil = new MoneyWriteUtil();
    }

    @Test
    void testSetAmount() {
        BigDecimal amount = new BigDecimal("1234567890123.45");
        moneyWriteUtil.setAmount(amount);
        assertEquals(amount, moneyWriteUtil.getMwAmount());
        assertArrayEquals(new int[]{1, 234, 567, 890, 123}, moneyWriteUtil.getMwAmountGroup());
        assertEquals(45, moneyWriteUtil.getMwAmountCents());
    }

    @Test
    void testProcessGroup() {
        moneyWriteUtil.setAmount(new BigDecimal("1234567890123.45"));
        moneyWriteUtil.processGroup(2); // Testing for millions
        String[] result = moneyWriteUtil.getMwMoneyLines();
        assertTrue(Arrays.stream(result).anyMatch(line -> line.contains("FIVE HUNDRED SIXTY SEVEN MILLION")));
    }

    @Test
    void testAddWord() {
        moneyWriteUtil.addWord("TEST");
        String[] result = moneyWriteUtil.getMwMoneyLines();
        assertTrue(Arrays.stream(result).anyMatch(line -> line.contains("TEST")));
    }

    @Test
    void testConvertToWords() {
        moneyWriteUtil.setAmount(new BigDecimal("1234.56"));
        moneyWriteUtil.convertToWords();
        String[] result = moneyWriteUtil.getMwMoneyLines();
        assertTrue(Arrays.stream(result).anyMatch(line -> line.contains("ONE THOUSAND TWO HUNDRED THIRTY FOUR DOLLARS AND FIFTY SIX CENTS")));
    }

    // Implementation of MoneyWriteUtil
    static class MoneyWriteUtil {
        private static final int MW_GLIM = 5;
        private static final int MW_MCLIM = 50;
        private static final int MW_MLLIM = 6;

        private BigDecimal mwAmount;
        private int[] mwAmountGroup;
        private int mwAmountCents;
        private char[] mwMoney;
        private int mwMcx;
        private String[] mwMoneyLines;
        private int mwMlx;

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
            this.mwMoney = new char[MW_MCLIM];
            this.mwMcx = 0;
            this.mwMoneyLines = new String[MW_MLLIM];
            this.mwMlx = 0;
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

        public void processGroup(int groupIndex) {
            int groupValue = this.mwAmountGroup[groupIndex];
            if (groupValue == 0) return;

            int hundreds = groupValue / 100;
            if (hundreds > 0) {
                addWord(MW_ONES_WORDS[hundreds - 1]);
                addWord("HUNDRED");
            }

            int tensAndOnes = groupValue % 100;
            if (tensAndOnes >= 20) {
                addWord(MW_TENS_WORDS[tensAndOnes / 10 - 1]);
                if (tensAndOnes % 10 > 0) {
                    addWord(MW_ONES_WORDS[tensAndOnes % 10 - 1]);
                }
            } else if (tensAndOnes > 0) {
                addWord(MW_ONES_WORDS[tensAndOnes - 1]);
            }

            if (groupIndex < MW_GLIM - 1) {
                addWord(MW_GROUP_WORDS[groupIndex]);
            }
        }

        public void addWord(String word) {
            if (mwMcx + word.length() + 1 > MW_MCLIM - 4) {
                finalizeMoneyLine();
            }
            if (mwMcx == 0) {
                mwMoney[mwMcx++] = '*';
                mwMoney[mwMcx++] = '*';
            } else {
                mwMoney[mwMcx++] = ' ';
            }
            for (char c : word.toCharArray()) {
                mwMoney[mwMcx++] = c;
            }
        }

        private void finalizeMoneyLine() {
            if (mwMcx > 0) {
                mwMoney[mwMcx++] = '*';
                mwMoney[mwMcx++] = '*';
                mwMoneyLines[mwMlx++] = new String(mwMoney, 0, mwMcx).trim();
                mwMcx = 0;
            }
        }

        public void convertToWords() {
            for (int i = 0; i < MW_GLIM; i++) {
                processGroup(i);
            }
            addWord(mwAmount.longValue() == 1 ? "DOLLAR" : "DOLLARS");
            addWord("AND");
            if (mwAmountCents == 0) {
                addWord("NO");
            } else {
                processGroup(MW_GLIM - 1); // Process cents
            }
            addWord(mwAmountCents == 1 ? "CENT" : "CENTS");
            finalizeMoneyLine();
        }

        // Getter methods for testing
        public BigDecimal getMwAmount() {
            return mwAmount;
        }

        public int[] getMwAmountGroup() {
            return mwAmountGroup;
        }

        public int getMwAmountCents() {
            return mwAmountCents;
        }

        public String[] getMwMoneyLines() {
            return mwMoneyLines;
        }
    }

    public static void main(String[] args) {
        MoneyWriteUtilTest test = new MoneyWriteUtilTest();
        test.setUp();
        test.testSetAmount();
        test.testProcessGroup();
        test.testAddWord();
        test.testConvertToWords();
        System.out.println("All tests passed successfully!");
    }
}