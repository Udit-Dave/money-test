package com.example.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Utility class for writing money-related information to files.
 */
@Component
public class MoneyWriteUtil {

    private static final String DEFAULT_OUTPUT_FILE = "money_output.txt";

    /**
     * Writes a list of BigDecimal values to a file.
     *
     * @param amounts The list of BigDecimal amounts to write.
     * @throws IOException If an I/O error occurs.
     */
    public void writeAmountsToFile(List<BigDecimal> amounts) throws IOException {
        writeAmountsToFile(amounts, DEFAULT_OUTPUT_FILE);
    }

    /**
     * Writes a list of BigDecimal values to a specified file.
     *
     * @param amounts  The list of BigDecimal amounts to write.
     * @param fileName The name of the file to write to.
     * @throws IOException If an I/O error occurs.
     */
    public void writeAmountsToFile(List<BigDecimal> amounts, String fileName) throws IOException {
        if (amounts == null || amounts.isEmpty()) {
            throw new IllegalArgumentException("The list of amounts cannot be null or empty.");
        }

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("The file name cannot be null or empty.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (BigDecimal amount : amounts) {
                writer.write(amount.toPlainString());
                writer.newLine();
            }
        }
    }

    /**
     * Appends a single BigDecimal value to the default file.
     *
     * @param amount The BigDecimal amount to append.
     * @throws IOException If an I/O error occurs.
     */
    public void appendAmountToFile(BigDecimal amount) throws IOException {
        appendAmountToFile(amount, DEFAULT_OUTPUT_FILE);
    }

    /**
     * Appends a single BigDecimal value to a specified file.
     *
     * @param amount   The BigDecimal amount to append.
     * @param fileName The name of the file to append to.
     * @throws IOException If an I/O error occurs.
     */
    public void appendAmountToFile(BigDecimal amount, String fileName) throws IOException {
        if (amount == null) {
            throw new IllegalArgumentException("The amount cannot be null.");
        }

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("The file name cannot be null or empty.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(amount.toPlainString());
            writer.newLine();
        }
    }
}