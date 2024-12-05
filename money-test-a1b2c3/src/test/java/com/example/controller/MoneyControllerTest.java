import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyControllerTest {

    @Mock
    private MoneyConversionService moneyConversionService;

    @InjectMocks
    private MoneyController moneyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertMoneyToText_ValidAmount() {
        BigDecimal amount = new BigDecimal("123.45");
        String expectedText = "ONE HUNDRED TWENTY-THREE DOLLARS AND FORTY-FIVE CENTS";
        when(moneyConversionService.convertToText(amount)).thenReturn(expectedText);

        ResponseEntity<String> response = moneyController.convertMoneyToText(amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedText, response.getBody());
        verify(moneyConversionService).convertToText(amount);
    }

    @Test
    void testConvertMoneyToText_NegativeAmount() {
        BigDecimal amount = new BigDecimal("-10.00");

        ResponseEntity<String> response = moneyController.convertMoneyToText(amount);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid amount. Please enter a value between 0 and 999,999,999,999,999.99", response.getBody());
        verify(moneyConversionService, never()).convertToText(any());
    }

    @Test
    void testConvertMoneyToText_ExceedingMaxAmount() {
        BigDecimal amount = new BigDecimal("1000000000000000.00");

        ResponseEntity<String> response = moneyController.convertMoneyToText(amount);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid amount. Please enter a value between 0 and 999,999,999,999,999.99", response.getBody());
        verify(moneyConversionService, never()).convertToText(any());
    }

    // Mock implementations
    private interface MoneyConversionService {
        String convertToText(BigDecimal amount);
    }

    private static class MoneyController {
        private final MoneyConversionService moneyConversionService;

        public MoneyController(MoneyConversionService moneyConversionService) {
            this.moneyConversionService = moneyConversionService;
        }

        public ResponseEntity<String> convertMoneyToText(BigDecimal amount) {
            if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(new BigDecimal("999999999999999.99")) > 0) {
                return ResponseEntity.badRequest().body("Invalid amount. Please enter a value between 0 and 999,999,999,999,999.99");
            }

            String result = moneyConversionService.convertToText(amount);
            return ResponseEntity.ok(result);
        }
    }

    // Main method to run the tests
    public static void main(String[] args) {
        MoneyControllerTest test = new MoneyControllerTest();
        test.setUp();
        test.testConvertMoneyToText_ValidAmount();
        test.testConvertMoneyToText_NegativeAmount();
        test.testConvertMoneyToText_ExceedingMaxAmount();
        System.out.println("All tests passed successfully!");
    }
}