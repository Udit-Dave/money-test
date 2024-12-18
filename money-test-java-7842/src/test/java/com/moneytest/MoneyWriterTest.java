import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MoneyWriterTest {

    private MoneyWriter moneyWriter;

    @BeforeEach
    void setUp() {
        moneyWriter = new MoneyWriter();
    }

    @Test
    void testZeroDollars() {
        moneyWriter.setAmount(BigDecimal.ZERO);
        String[] result = moneyWriter.convertToWords();
        assertEquals(1, result.length);
        assertTrue(result[0].contains("ZERO DOLLARS AND ZERO CENTS"));
    }

    @Test
    void testOneDollar() {
        moneyWriter.setAmount(BigDecimal.ONE);
        String[] result = moneyWriter.convertToWords();
        assertEquals(1, result.length);
        assertTrue(result[0].contains("ONE DOLLAR AND ZERO CENTS"));
    }

    @Test
    void testOneCent() {
        moneyWriter.setAmount(new BigDecimal("0.01"));
        String[] result = moneyWriter.convertToWords();
        assertEquals(1, result.length);
        assertTrue(result[0].contains("ZERO DOLLARS AND 01 CENTS"));
    }

    @Test
    void testLargeAmount() {
        moneyWriter.setAmount(new BigDecimal("1234567890123.45"));
        String[] result = moneyWriter.convertToWords();
        assertTrue(result.length > 1);
        assertTrue(result[0].contains("ONE TRILLION"));
        assertTrue(result[1].contains("TWO HUNDRED THIRTY FOUR BILLION"));
        assertTrue(result[2].contains("FIVE HUNDRED SIXTY SEVEN MILLION"));
        assertTrue(result[3].contains("EIGHT HUNDRED NINETY THOUSAND"));
        assertTrue(result[4].contains("ONE HUNDRED TWENTY THREE DOLLARS"));
        assertTrue(result[5].contains("AND 45 CENTS"));
    }

    // Implementation of MoneyWriter
    static class MoneyWriter {
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

            boolean isZero = true;
            for (int groupIndex = 0; groupIndex < MW_GLIM; groupIndex++) {
                if (amountGroup[groupIndex] > 0) {
                    isZero = false;
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

            if (isZero) {
                appendWord("ZERO");
            }

            appendWord(amount.compareTo(BigDecimal.ONE) == 0 ? "DOLLAR" : "DOLLARS");
            appendWord("AND");

            if (amountCents == 0) {
                appendWord("ZERO");
            } else {
                appendWord(String.format("%02d", amountCents));
            }
            appendWord("CENTS");

            if (charIndex > 0) {
                moneyLine[lineIndex] = new String(moneyChar).trim();
                lineIndex++;
            }

            return Arrays.copyOf(moneyLine, lineIndex);
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

        private void appendWord(String word) {
            if (getNextCharIndex() + word.length() + 1 >= MW_MCLIM) {
                moneyLine[getNextLineIndex()] = new String(moneyChar).trim();
                Arrays.fill(moneyChar, ' ');
            }
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

        private int getNextLineIndex() {
            for (int i = 0; i < moneyLine.length; i++) {
                if (moneyLine[i] == null || moneyLine[i].isEmpty()) {
                    return i;
                }
            }
            return moneyLine.length - 1;
        }
    }

    public static void main(String[] args) {
        MoneyWriterTest test = new MoneyWriterTest();
        test.setUp();
        test.testZeroDollars();
        test.testOneDollar();
        test.testOneCent();
        test.testLargeAmount();
        System.out.println("All tests passed successfully!");
    }
}