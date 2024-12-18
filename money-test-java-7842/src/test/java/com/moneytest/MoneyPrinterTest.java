import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyPrinterTest {

    private MoneyPrinter moneyPrinter;

    @BeforeEach
    void setUp() {
        moneyPrinter = new MoneyPrinter();
    }

    @Test
    void testConvertZeroDollars() {
        List<String> result = moneyPrinter.convertToWords(BigDecimal.ZERO);
        assertEquals(1, result.size());
        assertEquals("                        ** NO DOLLARS AND NO CENTS **                         ", result.get(0));
    }

    @Test
    void testConvertOneDollar() {
        List<String> result = moneyPrinter.convertToWords(BigDecimal.ONE);
        assertEquals(1, result.size());
        assertEquals("                      ** ONE DOLLAR AND NO CENTS **                       ", result.get(0));
    }

    @Test
    void testConvertOneCent() {
        List<String> result = moneyPrinter.convertToWords(new BigDecimal("0.01"));
        assertEquals(1, result.size());
        assertEquals("                      ** NO DOLLARS AND ONE CENT **                       ", result.get(0));
    }

    @Test
    void testConvertLargeAmount() {
        List<String> result = moneyPrinter.convertToWords(new BigDecimal("1234567890123.45"));
        assertEquals(3, result.size());
        assertEquals("           ** ONE TRILLION TWO HUNDRED THIRTY FOUR BILLION FIVE HUNDRED SIXTY            ", result.get(0));
        assertEquals("             ** SEVEN MILLION EIGHT HUNDRED NINETY THOUSAND ONE HUNDRED TWENTY         ", result.get(1));
        assertEquals("                    ** THREE DOLLARS AND FORTY FIVE CENTS **                     ", result.get(2));
    }

    // Implementation of MoneyPrinter
    static class MoneyPrinter {
        private static final int MAX_LINE_LENGTH = 80;
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

        public List<String> convertToWords(BigDecimal amount) {
            List<String> moneyLines = new ArrayList<>();
            StringBuilder currentLine = new StringBuilder();

            long dollars = amount.longValue();
            int cents = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

            if (dollars == 0) {
                addWord(currentLine, moneyLines, "NO");
            }

            for (int i = 4; i >= 0; i--) {
                int groupValue = (int) (dollars / Math.pow(1000, i) % 1000);
                if (groupValue > 0) {
                    convertGroup(groupValue, currentLine, moneyLines);
                    if (i > 0) {
                        addWord(currentLine, moneyLines, GROUP_WORDS[i]);
                    }
                }
            }

            addWord(currentLine, moneyLines, dollars == 1 ? "DOLLAR" : "DOLLARS");
            addWord(currentLine, moneyLines, "AND");

            if (cents == 0) {
                addWord(currentLine, moneyLines, "NO");
            } else {
                convertTwoDigits(cents, currentLine, moneyLines);
            }

            addWord(currentLine, moneyLines, cents == 1 ? "CENT" : "CENTS");

            if (!currentLine.isEmpty()) {
                moneyLines.add(centerLine(currentLine.toString()));
            }

            return moneyLines;
        }

        private void convertGroup(int value, StringBuilder currentLine, List<String> moneyLines) {
            int hundreds = value / 100;
            int remainder = value % 100;

            if (hundreds > 0) {
                addWord(currentLine, moneyLines, ONES_WORDS[hundreds]);
                addWord(currentLine, moneyLines, "HUNDRED");
            }

            convertTwoDigits(remainder, currentLine, moneyLines);
        }

        private void convertTwoDigits(int value, StringBuilder currentLine, List<String> moneyLines) {
            if (value >= 20) {
                addWord(currentLine, moneyLines, TENS_WORDS[value / 10]);
                if (value % 10 > 0) {
                    addWord(currentLine, moneyLines, ONES_WORDS[value % 10]);
                }
            } else if (value > 0) {
                addWord(currentLine, moneyLines, ONES_WORDS[value]);
            }
        }

        private void addWord(StringBuilder currentLine, List<String> moneyLines, String word) {
            if (currentLine.length() + word.length() + 3 > MAX_LINE_LENGTH) {
                moneyLines.add(centerLine(currentLine.toString()));
                currentLine.setLength(0);
            }
            if (currentLine.length() == 0) {
                currentLine.append("**");
            } else {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        private String centerLine(String line) {
            line = line + "**";
            int padding = (MAX_LINE_LENGTH - line.length()) / 2;
            return " ".repeat(padding) + line + " ".repeat(padding);
        }
    }

    public static void main(String[] args) {
        MoneyPrinterTest test = new MoneyPrinterTest();
        test.setUp();
        test.testConvertZeroDollars();
        test.testConvertOneDollar();
        test.testConvertOneCent();
        test.testConvertLargeAmount();
        System.out.println("All tests passed successfully!");
    }
}