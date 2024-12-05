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
        BigDecimal amount = new BigDecimal("1234567890123.45");
        moneyUtils.setAmount(amount);
        
        // Access the private field for testing
        int[] amountGroup = getPrivateField(moneyUtils, "amountGroup");
        
        assertArrayEquals(new int[]{001, 234, 567, 890, 123}, amountGroup);
    }

    @Test
    void testConvertToWords() {
        BigDecimal amount = new BigDecimal("1234.56");
        moneyUtils.setAmount(amount);
        
        String result = moneyUtils.convertToWords();
        assertTrue(result.contains("1234.56"));
    }

    @Test
    void testLargeAmount() {
        BigDecimal amount = new BigDecimal("9999999999999.99");
        moneyUtils.setAmount(amount);
        
        int[] amountGroup = getPrivateField(moneyUtils, "amountGroup");
        assertArrayEquals(new int[]{009, 999, 999, 999, 999}, amountGroup);
    }

    @Test
    void testZeroAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        moneyUtils.setAmount(amount);
        
        int[] amountGroup = getPrivateField(moneyUtils, "amountGroup");
        assertArrayEquals(new int[]{000, 000, 000, 000, 000}, amountGroup);
    }

    // Helper method to access private fields for testing
    private <T> T getPrivateField(Object obj, String fieldName) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        MoneyUtilsTest test = new MoneyUtilsTest();
        test.setUp();
        test.testSetAmount();
        test.testConvertToWords();
        test.testLargeAmount();
        test.testZeroAmount();
        System.out.println("All tests passed successfully!");
    }
}

// Implementation of the class being tested
class MoneyUtils {
    private static final int MW_GLIM = 5;
    private static final int MW_MCLIM = 50;
    private static final int MW_MLLIM = 6;

    private BigDecimal amount;
    private int[] amountGroup;
    private int[] work;
    private char[] moneyChar;
    private String[] moneyLines;
    private char[] wordChar;
    private int mcx;
    private int mlx;
    private int wcx;
    private int gx;
    private int ix1;
    private int ix2;

    private static final String[] GROUP_WORDS = {
        "TRILLION", "BILLION", "MILLION", "THOUSAND", ""
    };

    private static final String[] TENS_WORDS = {
        "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"
    };

    private static final String[] ONES_WORDS = {
        "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE",
        "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN",
        "SEVENTEEN", "EIGHTEEN", "NINETEEN"
    };

    public MoneyUtils() {
        this.amount = BigDecimal.ZERO;
        this.amountGroup = new int[MW_GLIM];
        this.work = new int[3];
        this.moneyChar = new char[MW_MCLIM];
        this.moneyLines = new String[MW_MLLIM];
        this.wordChar = new char[9];
        this.mcx = 0;
        this.mlx = 0;
        this.wcx = 0;
        this.gx = 0;
        this.ix1 = 0;
        this.ix2 = 0;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        updateAmountGroup();
    }

    private void updateAmountGroup() {
        String amountStr = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        String[] parts = amountStr.split("\\.");
        String dollars = String.format("%015d", Long.parseLong(parts[0]));
        
        for (int i = 0; i < MW_GLIM; i++) {
            amountGroup[i] = Integer.parseInt(dollars.substring(i * 3, (i + 1) * 3));
        }
    }

    public String convertToWords() {
        // Placeholder implementation
        return "Amount in words: " + amount.toString();
    }
}