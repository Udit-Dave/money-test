import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyConversionControllerTest {

    @Mock
    private MoneyConversionService moneyConversionService;

    @InjectMocks
    private MoneyConversionController moneyConversionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertDollarAmount_ZeroAmount() {
        // Arrange
        BigDecimal amount = BigDecimal.ZERO;

        // Act
        ResponseEntity<String> response = moneyConversionController.convertDollarAmount(amount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Conversion process ended.", response.getBody());
        verify(moneyConversionService, never()).convertAmount(any(BigDecimal.class));
    }

    @Test
    void testConvertDollarAmount_NonZeroAmount() {
        // Arrange
        BigDecimal amount = new BigDecimal("1234.56");
        String[] mockConvertedLines = {
            "** ONE THOUSAND TWO HUNDRED THIRTY FOUR      **",
            "** DOLLARS AND FIFTY SIX CENTS               **"
        };
        when(moneyConversionService.convertAmount(amount)).thenReturn(mockConvertedLines);

        // Act
        ResponseEntity<String> response = moneyConversionController.convertDollarAmount(amount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String expectedResponse = "Converted Dollar Amount:\n" +
                                  "** ONE THOUSAND TWO HUNDRED THIRTY FOUR      **\n" +
                                  "** DOLLARS AND FIFTY SIX CENTS               **\n";
        assertEquals(expectedResponse, response.getBody());
        verify(moneyConversionService).convertAmount(amount);
    }

    // Mock implementations
    static class MoneyConversionController {
        private final MoneyConversionService moneyConversionService;

        public MoneyConversionController(MoneyConversionService moneyConversionService) {
            this.moneyConversionService = moneyConversionService;
        }

        public ResponseEntity<String> convertDollarAmount(BigDecimal amount) {
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                return ResponseEntity.ok("Conversion process ended.");
            }

            String[] convertedLines = moneyConversionService.convertAmount(amount);
            StringBuilder response = new StringBuilder("Converted Dollar Amount:\n");
            for (String line : convertedLines) {
                response.append(line).append("\n");
            }

            return ResponseEntity.ok(response.toString());
        }
    }

    interface MoneyConversionService {
        String[] convertAmount(BigDecimal amount);
    }

    // Main method to run the tests
    public static void main(String[] args) {
        MoneyConversionControllerTest test = new MoneyConversionControllerTest();
        test.setUp();
        test.testConvertDollarAmount_ZeroAmount();
        test.testConvertDollarAmount_NonZeroAmount();
        System.out.println("All tests passed successfully!");
    }
}