import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class MoneyConversionServiceTest {

    private MoneyConversionService moneyConversionService;

    @BeforeEach
    void setUp() {
        moneyConversionService = new MoneyConversionService();
    }

    @Test
    void testConvertZeroDollarsAndZeroCents() {
        String[] result = moneyConversionService.convertMoneyToText(0.00);
        assertEquals("** NO DOLLARS AND NO CENTS **", result[0].trim());
    }

    @Test
    void testConvertOneDollarAndOneCent() {
        String[] result = moneyConversionService.convertMoneyToText(1.01);
        assertEquals("** ONE DOLLAR AND ONE CENT **", result[0].trim());
    }

    @Test
    void testConvertTwoDollarsAndTwoCents() {
        String[] result = moneyConversionService.convertMoneyToText(2.02);
        assertEquals("** TWO DOLLARS AND TWO CENTS **", result[0].trim());
    }

    @Test
    void testConvertLargeAmount() {
        String[] result = moneyConversionService.convertMoneyToText(1234567.89);
        assertEquals("** ONE MILLION TWO HUNDRED THIRTY FOUR THOUSAND FIVE HUNDRED SIXTY SEVEN DOLLARS AND EIGHTY NINE CENTS **", result[0].trim());
    }

    @Test
    void testConvertWithNocents() {
        String[] result = moneyConversionService.convertMoneyToText(100.00);
        assertEquals("** ONE HUNDRED DOLLARS AND NO CENTS **", result[0].trim());
    }

    @Test
    void testConvertWithOnlyCents() {
        String[] result = moneyConversionService.convertMoneyToText(0.50);
        assertEquals("** NO DOLLARS AND FIFTY CENTS **", result[0].trim());
    }

    public static void main(String[] args) {
        MoneyConversionServiceTest test = new MoneyConversionServiceTest();
        test.setUp();
        test.testConvertZeroDollarsAndZeroCents();
        test.testConvertOneDollarAndOneCent();
        test.testConvertTwoDollarsAndTwoCents();
        test.testConvertLargeAmount();
        test.testConvertWithNocents();
        test.testConvertWithOnlyCents();
        System.out.println("All tests passed successfully!");
    }
}

// Implementation of the class being tested
class MoneyConversionService {

    private static final int MAX_LINE_LENGTH = 80;
    private static final String[] ONES_WORDS = {"", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
            "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
    private static final String[] TENS_WORDS = {"", "", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"};
    private static final String[] GROUP_WORDS = {"", "THOUSAND", "MILLION", "BILLION", "TRILLION"};

    public String[] convertMoneyToText(double amount) {
        StringBuilder moneyText = new StringBuilder();
        String[] moneyLines = new String[5]; // Assuming maximum 5 lines
        int lineIndex = 0;

        long dollars = (long) amount;
        int cents = (int) Math.round((amount - dollars) * 100);

        for (int groupIndex = 4; groupIndex >= 0; groupIndex--) {
            int groupValue = (int) (dollars / Math.pow(1000, groupIndex) % 1000);
            if (groupValue > 0) {
                appendGroup(moneyText, groupValue);
                if (!GROUP_WORDS[groupIndex].isEmpty()) {
                    appendWord(moneyText, GROUP_WORDS[groupIndex]);
                }
            }
        }

        if (dollars == 0) {
            appendWord(moneyText, "NO");
        }

        appendWord(moneyText, dollars == 1 ? "DOLLAR" : "DOLLARS");
        appendWord(moneyText, "AND");

        if (cents == 0) {
            appendWord(moneyText, "NO");
        } else {
            appendTwoDigits(moneyText, cents);
        }

        appendWord(moneyText, cents == 1 ? "CENT" : "CENTS");

        // Center the text and add protection
        String centeredText = centerText(moneyText.toString().trim());
        moneyLines[lineIndex++] = centeredText;

        return moneyLines;
    }

    private void appendGroup(StringBuilder sb, int value) {
        int hundreds = value / 100;
        int tens = value % 100;

        if (hundreds > 0) {
            appendWord(sb, ONES_WORDS[hundreds]);
            appendWord(sb, "HUNDRED");
        }

        appendTwoDigits(sb, tens);
    }

    private void appendTwoDigits(StringBuilder sb, int value) {
        if (value >= 20) {
            appendWord(sb, TENS_WORDS[value / 10]);
            if (value % 10 > 0) {
                appendWord(sb, ONES_WORDS[value % 10]);
            }
        } else if (value > 0) {
            appendWord(sb, ONES_WORDS[value]);
        }
    }

    private void appendWord(StringBuilder sb, String word) {
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(word);
    }

    private String centerText(String text) {
        int padding = (MAX_LINE_LENGTH - text.length()) / 2;
        return "**" + " ".repeat(Math.max(0, padding - 2)) + text +
               " ".repeat(Math.max(0, MAX_LINE_LENGTH - text.length() - padding - 4)) + "**";
    }
}