import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

class MoneyConversionServiceTest {

    private MoneyConversionService moneyConversionService;

    @BeforeEach
    void setUp() {
        moneyConversionService = new MoneyConversionService();
    }

    @Test
    void testConvertMoneyToText_ZeroDollars() {
        List<String> result = moneyConversionService.convertMoneyToText(new BigDecimal("0.00"));
        assertEquals(1, result.size());
        assertEquals("**               NO DOLLARS AND NO CENTS               **", result.get(0));
    }

    @Test
    void testConvertMoneyToText_OneDollar() {
        List<String> result = moneyConversionService.convertMoneyToText(new BigDecimal("1.00"));
        assertEquals(1, result.size());
        assertEquals("**               ONE DOLLAR AND NO CENTS               **", result.get(0));
    }

    @Test
    void testConvertMoneyToText_OneCent() {
        List<String> result = moneyConversionService.convertMoneyToText(new BigDecimal("0.01"));
        assertEquals(1, result.size());
        assertEquals("**               NO DOLLARS AND ONE CENT               **", result.get(0));
    }

    @Test
    void testConvertMoneyToText_LargeAmount() {
        List<String> result = moneyConversionService.convertMoneyToText(new BigDecimal("1234567890123.45"));
        assertEquals(1, result.size());
        assertEquals("** ONE TRILLION TWO HUNDRED THIRTY-FOUR BILLION FIVE HUNDRED SIXTY-SEVEN MILLION EIGHT **", result.get(0));
    }

    @Test
    void testConvertMoneyToText_ComplexAmount() {
        List<String> result = moneyConversionService.convertMoneyToText(new BigDecimal("9876543210.99"));
        assertEquals(1, result.size());
        assertEquals("** NINE BILLION EIGHT HUNDRED SEVENTY-SIX MILLION FIVE HUNDRED FORTY-THREE THOUSAND **", result.get(0));
    }

    // Implementation of MoneyConversionService
    private static class MoneyConversionService {

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

        public List<String> convertMoneyToText(BigDecimal amount) {
            List<String> moneyLines = new ArrayList<>();
            StringBuilder moneyText = new StringBuilder();

            long dollars = amount.longValue();
            int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

            if (dollars == 0) {
                appendWord(moneyText, "NO");
            } else {
                convertGroup(moneyText, dollars);
            }

            appendWord(moneyText, dollars == 1 ? "DOLLAR" : "DOLLARS");
            appendWord(moneyText, "AND");

            if (cents == 0) {
                appendWord(moneyText, "NO");
            } else {
                convertTwoDigits(moneyText, cents);
            }

            appendWord(moneyText, cents == 1 ? "CENT" : "CENTS");

            String centeredText = centerText(moneyText.toString());
            moneyLines.add(centeredText);

            return moneyLines;
        }

        private void convertGroup(StringBuilder sb, long number) {
            for (int i = GROUP_WORDS.length - 1; i >= 0; i--) {
                long groupValue = number / (long) Math.pow(1000, i);
                if (groupValue > 0) {
                    convertThreeDigits(sb, groupValue);
                    if (!GROUP_WORDS[i].isEmpty()) {
                        appendWord(sb, GROUP_WORDS[i]);
                    }
                }
                number %= (long) Math.pow(1000, i);
            }
        }

        private void convertThreeDigits(StringBuilder sb, long number) {
            int hundreds = (int) (number / 100);
            int remainder = (int) (number % 100);

            if (hundreds > 0) {
                appendWord(sb, ONES_WORDS[hundreds]);
                appendWord(sb, "HUNDRED");
            }

            convertTwoDigits(sb, remainder);
        }

        private void convertTwoDigits(StringBuilder sb, int number) {
            if (number >= 20) {
                appendWord(sb, TENS_WORDS[number / 10]);
                if (number % 10 > 0) {
                    appendWord(sb, ONES_WORDS[number % 10]);
                }
            } else if (number > 0) {
                appendWord(sb, ONES_WORDS[number]);
            }
        }

        private void appendWord(StringBuilder sb, String word) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(word);
        }

        private String centerText(String text) {
            int totalWidth = 80;
            int textLength = text.length();
            int padding = (totalWidth - textLength) / 2;
            return "**" + " ".repeat(Math.max(0, padding - 2)) + text + " ".repeat(Math.max(0, padding - 2)) + "**";
        }
    }

    public static void main(String[] args) {
        MoneyConversionServiceTest test = new MoneyConversionServiceTest();
        test.setUp();
        test.testConvertMoneyToText_ZeroDollars();
        test.testConvertMoneyToText_OneDollar();
        test.testConvertMoneyToText_OneCent();
        test.testConvertMoneyToText_LargeAmount();
        test.testConvertMoneyToText_ComplexAmount();
        System.out.println("All tests passed successfully!");
    }
}