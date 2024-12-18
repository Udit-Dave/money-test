import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyConversionServiceTest {

    private MoneyConversionService moneyConversionService;

    @BeforeEach
    void setUp() {
        moneyConversionService = new MoneyConversionService();
    }

    @Test
    void testConvertZeroDollars() {
        List<String> result = moneyConversionService.convertToWords(BigDecimal.ZERO);
        assertEquals(1, result.size());
        assertEquals("                ** NO DOLLARS AND NO CENTS **                ", result.get(0));
    }

    @Test
    void testConvertOneDollar() {
        List<String> result = moneyConversionService.convertToWords(BigDecimal.ONE);
        assertEquals(1, result.size());
        assertEquals("               ** ONE DOLLAR AND NO CENTS **                ", result.get(0));
    }

    @Test
    void testConvertOneCent() {
        List<String> result = moneyConversionService.convertToWords(new BigDecimal("0.01"));
        assertEquals(1, result.size());
        assertEquals("               ** NO DOLLARS AND ONE CENT **                ", result.get(0));
    }

    @Test
    void testConvertLargeAmount() {
        List<String> result = moneyConversionService.convertToWords(new BigDecimal("1234567890123.45"));
        assertEquals(4, result.size());
        assertEquals("        ** ONE TRILLION TWO HUNDRED THIRTY FOUR         ", result.get(0));
        assertEquals("     ** BILLION FIVE HUNDRED SIXTY SEVEN MILLION        ", result.get(1));
        assertEquals("    ** EIGHT HUNDRED NINETY THOUSAND ONE HUNDRED        ", result.get(2));
        assertEquals("   ** TWENTY THREE DOLLARS AND FORTY FIVE CENTS **      ", result.get(3));
    }

    // Implementation of MoneyConversionService
    static class MoneyConversionService {
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

        public List<String> convertToWords(BigDecimal amount) {
            this.amount = amount;
            this.money = new StringBuilder();
            this.moneyLines = new java.util.ArrayList<>();
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

            addWord(cents == 1 ? "CENT" : "CENTS");

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

    public static void main(String[] args) {
        MoneyConversionServiceTest test = new MoneyConversionServiceTest();
        test.setUp();
        test.testConvertZeroDollars();
        test.testConvertOneDollar();
        test.testConvertOneCent();
        test.testConvertLargeAmount();
        System.out.println("All tests passed successfully!");
    }
}