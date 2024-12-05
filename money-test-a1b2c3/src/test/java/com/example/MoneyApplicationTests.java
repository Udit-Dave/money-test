import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MoneyApplicationTest {

    @Mock
    private SpringApplication mockSpringApplication;

    @Mock
    private ConfigurableApplicationContext mockContext;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testMainMethod() {
        // Arrange
        String input = "100.00\n0\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        when(mockSpringApplication.run(MoneyApplication.class)).thenReturn(mockContext);

        // Act
        MoneyApplication.main(new String[]{});

        // Assert
        verify(mockSpringApplication, times(1)).run(MoneyApplication.class);
        assertTrue(outContent.toString().contains("C O N V E R T E D   D O L L A R   A M O U N T"));
        assertTrue(outContent.toString().contains("Enter up to tttbbbmmmtttooo.cc (0 to exit):"));
    }

    @Test
    void testControlProcess() {
        // Arrange
        String input = "100.00\n0\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        // Act
        MoneyApplication.controlProcess();

        // Assert
        assertTrue(outContent.toString().contains("C O N V E R T E D   D O L L A R   A M O U N T"));
        assertTrue(outContent.toString().contains("Enter up to tttbbbmmmtttooo.cc (0 to exit):"));
    }

    // Mock implementation of MoneyUtils
    static class MoneyUtils {
        private static BigDecimal amount;

        public static String getMoneyLine(int lineNumber) {
            return "Mock Money Line " + lineNumber;
        }

        public static void setAmount(BigDecimal newAmount) {
            amount = newAmount;
        }

        public static void convertMoney() {
            // Mock implementation
        }
    }

    // Mock implementation of SpringApplication
    static class SpringApplication {
        public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
            return mock(ConfigurableApplicationContext.class);
        }
    }

    // Mock implementation of Docket for Swagger
    static class Docket {
        public Docket(DocumentationType documentationType) {
            // Mock constructor
        }

        public Docket select() {
            return this;
        }

        public Docket apis(Object o) {
            return this;
        }

        public Docket paths(Object o) {
            return this;
        }

        public Docket build() {
            return this;
        }
    }

    // Mock implementation of DocumentationType
    enum DocumentationType {
        SWAGGER_2
    }

    // Mock implementation of RequestHandlerSelectors
    static class RequestHandlerSelectors {
        public static Object basePackage(String basePackage) {
            return null;
        }
    }

    // Mock implementation of PathSelectors
    static class PathSelectors {
        public static Object any() {
            return null;
        }
    }

    // Mock implementation of SpringBootApplication annotation
    @interface SpringBootApplication {}

    // Mock implementation of EnableSwagger2 annotation
    @interface EnableSwagger2 {}

    // Mock implementation of Bean annotation
    @interface Bean {}

    public static void main(String[] args) {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            MoneyApplicationTest test = new MoneyApplicationTest();
            test.setUp();
            test.testMainMethod();
            test.testControlProcess();
        });
        System.out.println("All tests passed successfully!");
    }
}