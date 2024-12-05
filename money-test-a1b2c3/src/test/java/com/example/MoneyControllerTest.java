import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MoneyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MoneyConversionService moneyConversionService;

    @InjectMocks
    private MoneyController moneyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(moneyController).build();
    }

    @Test
    void testConvertMoneyToText() throws Exception {
        BigDecimal amount = new BigDecimal("123.45");
        List<String> convertedText = Arrays.asList("ONE HUNDRED TWENTY-THREE DOLLARS", "AND FORTY-FIVE CENTS");

        when(moneyConversionService.convertToText(amount)).thenReturn(convertedText);

        mockMvc.perform(post("/api/money/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(amount.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"ONE HUNDRED TWENTY-THREE DOLLARS\",\"AND FORTY-FIVE CENTS\"]"));

        verify(moneyConversionService, times(1)).convertToText(amount);
    }

    @Test
    void testDisplayConvertedAmount() throws Exception {
        BigDecimal amount = new BigDecimal("123.45");
        List<String> convertedText = Arrays.asList("ONE HUNDRED TWENTY-THREE DOLLARS", "AND FORTY-FIVE CENTS");

        when(moneyConversionService.convertToText(amount)).thenReturn(convertedText);

        String expectedDisplay = "C O N V E R T E D   D O L L A R   A M O U N T\n\n" +
                                 "ONE HUNDRED TWENTY-THREE DOLLARS\n" +
                                 "AND FORTY-FIVE CENTS\n";

        mockMvc.perform(get("/api/money/display")
                .param("amount", "123.45"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedDisplay));

        verify(moneyConversionService, times(1)).convertToText(amount);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MoneyControllerTest");
    }
}

// Mock implementations of external dependencies
class MoneyConversionService {
    public List<String> convertToText(BigDecimal amount) {
        // This is a mock implementation
        return Arrays.asList(
            amount.toBigInteger().toString() + " DOLLARS",
            "AND " + amount.remainder(BigDecimal.ONE).movePointRight(2).toBigInteger().toString() + " CENTS"
        );
    }
}

// Implementation of the class being tested
class MoneyController {
    private final MoneyConversionService moneyConversionService;

    public MoneyController(MoneyConversionService moneyConversionService) {
        this.moneyConversionService = moneyConversionService;
    }

    public List<String> convertMoneyToText(BigDecimal amount) {
        return moneyConversionService.convertToText(amount);
    }

    public String displayConvertedAmount(BigDecimal amount) {
        List<String> convertedLines = moneyConversionService.convertToText(amount);
        StringBuilder display = new StringBuilder();
        display.append("C O N V E R T E D   D O L L A R   A M O U N T\n\n");
        for (String line : convertedLines) {
            display.append(line).append("\n");
        }
        return display.toString();
    }
}