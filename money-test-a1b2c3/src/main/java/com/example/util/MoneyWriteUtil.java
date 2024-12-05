package com.example.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for writing money-related information to files.
 */
public class MoneyWriteUtil {

    private static final Logger logger = LoggerFactory.getLogger(MoneyWriteUtil.class);

    private MoneyWriteUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Writes a list of monetary amounts to a file.
     *
     * @param amounts The list of BigDecimal amounts to write
     * @param filePath The path of the file to write to
     * @throws IOException If an I/O error occurs
     */
    public static void writeAmountsToFile(List<BigDecimal> amounts, String filePath) throws IOException {
        validateInputs(amounts, filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (BigDecimal amount : amounts) {
                writer.write(formatAmount(amount));
                writer.newLine();
            }
            logger.info("Successfully wrote {} amounts to file: {}", amounts.size(), filePath);
        } catch (IOException e) {
            logger.error("Error writing amounts to file: {}", filePath, e);
            throw e;
        }
    }

    private static void validateInputs(List<BigDecimal> amounts, String filePath) {
        if (amounts == null || amounts.isEmpty()) {
            throw new IllegalArgumentException("Amounts list cannot be null or empty");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
    }

    private static String formatAmount(BigDecimal amount) {
        return String.format("$%.2f", amount);
    }
}