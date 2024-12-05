import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

class MoneyUtilsTest {

    private MoneyUtils moneyUtils;

    @BeforeEach
    void setUp() {
        moneyUtils = new MoneyUtils();
    }

    @Test
    void testSetAmount() {
        BigDecimal amount = new BigDecimal("1234567.89");
        moneyUtils.setAmount(amount);
        assertEquals(amount, moneyUtils.getMwAmount());
        assertArrayEquals(new int[]{0, 0, 1, 234, 567}, moneyUtils.getMwAmountGroup());
        assertEquals(89, moneyUtils.getMwAmountCents());
    }

    @Test
    void testSetAmountWithZeroCents() {
        BigDecimal amount = new BigDecimal("1000000.00");
        moneyUtils.setAmount(amount);
        assertEquals(amount, moneyUtils.getMwAmount());
        assertArrayEquals(new int[]{0, 0, 1, 0, 0}, moneyUtils.getMwAmountGroup());
        assertEquals(0, moneyUtils.getMwAmountCents());
    }

    @Test
    void testSetAmountWithLargeNumber() {
        BigDecimal amount = new BigDecimal("1234567890123.45");
        moneyUtils.setAmount(amount);
        assertEquals(amount, moneyUtils.getMwAmount());
        assertArrayEquals(new int[]{1, 234, 567, 890, 123}, moneyUtils.getMwAmountGroup());
        assertEquals(45, moneyUtils.getMwAmountCents());
    }

    @Test
    void testConvertToWords() {
        BigDecimal amount = new BigDecimal("1234.56");
        moneyUtils.setAmount(amount);
        String result = moneyUtils.convertToWords();
        assertEquals("ONE THOUSAND TWO HUNDRED THIRTY-FOUR DOLLARS AND FIFTY-SIX CENTS", result);
    }

    @Test
    void testConvertToWordsWithZeroCents() {
        BigDecimal amount = new BigDecimal("1000.00");
        moneyUtils.setAmount(amount);
        String result = moneyUtils.convertToWords();
        assertEquals("ONE THOUSAND DOLLARS AND ZERO CENTS", result);
    }

    @Test
    void testConvertToWordsWithLargeNumber() {
        BigDecimal amount = new BigDecimal("1234567890123.45");
        moneyUtils.setAmount(amount);
        String result = moneyUtils.convertToWords();
        assertEquals("ONE TRILLION TWO HUNDRED THIRTY-FOUR BILLION FIVE HUNDRED SIXTY-SEVEN MILLION EIGHT HUNDRED NINETY THOUSAND ONE HUNDRED TWENTY-THREE DOLLARS AND FORTY-FIVE CENTS", result);
    }

    // Implementation of MoneyUtils class
    public static class MoneyUtils {
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
            "TRILLION", "BILLION", "MILLION", "THOUSAND", ""
        };

        private static final String[] MW_TENS_WORDS = {
            "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY",
            "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
        };

        private static final String[] MW_ONES_WORDS = {
            "ONE", "TWO", "THREE", "FOUR", "FIVE",
            "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
            "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN",
            "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"
        };

        public MoneyUtils() {
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
            BigDecimal dollars = mwAmount.setScale(0, BigDecimal.ROUND_DOWN);
            this.mwAmountCents = mwAmount.subtract(dollars).multiply(BigDecimal.valueOf(100)).intValue();

            String dollarString = dollars.toString();
            int length = dollarString.length();
            for (int i = 0; i < MW_GLIM; i++) {
                int start = Math.max(0, length - (i + 1) * 3);
                int end = length - i * 3;
                this.mwAmountGroup[MW_GLIM - 1 - i] = Integer.parseInt(dollarString.substring(start, end));
            }
        }

        public String convertToWords() {
            StringBuilder result = new StringBuilder();
            boolean isFirstGroup = true;

            for (int i = 0; i < MW_GLIM; i++) {
                if (mwAmountGroup[i] > 0) {
                    if (!isFirstGroup) {
                        result.append(" ");
                    }
                    result.append(convertGroup(mwAmountGroup[i]));
                    result.append(" ").append(MW_GROUP_WORDS[i]);
                    isFirstGroup = false;
                }
            }

            result.append(" DOLLARS AND ");

            if (mwAmountCents == 0) {
                result.append("ZERO CENTS");
            } else {
                result.append(convertGroup(mwAmountCents)).append(" CENTS");
            }

            return result.toString().trim();
        }

        private String convertGroup(int number) {
            if (number == 0) {
                return "";
            } else if (number < 20) {
                return MW_ONES_WORDS[number - 1];
            } else if (number < 100) {
                return MW_TENS_WORDS[number / 10 - 1] + (number % 10 != 0 ? "-" + MW_ONES_WORDS[number % 10 - 1] : "");
            } else {
                return MW_ONES_WORDS[number / 100 - 1] + " HUNDRED" + (number % 100 != 0 ? " " + convertGroup(number % 100) : "");
            }
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
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MoneyUtilsTest");
    }
}