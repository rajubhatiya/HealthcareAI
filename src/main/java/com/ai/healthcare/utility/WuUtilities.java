package com.ai.healthcare.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class WuUtilities {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WuUtilities.class);

    /**
     * Generates a Money Transfer Control Number (MTCN) string.
     * The MTCN consists of a series of random two-digit numbers.
     *
     * @param transactionId The ID of the transaction associated with this MTCN.
     * @param count         The number of two-digit segments to generate.
     * @return A space-separated string of random numbers.
     */
    public static String generateMTCN(String transactionId, int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int num = ThreadLocalRandom.current().nextInt(10, 100); // two-digit numbers: 10–99
            result.append(num).append(" ");
        }

        log.info("Transaction Id : {}, Money Transfer Control Number (MTCN) : {}", transactionId,
                result.toString().trim());
        return result.toString().trim();
    }

    /**
     * Gets the current date formatted as "yyyy-MM-dd".
     *
     * @param transactionId The ID of the transaction for logging purposes.
     * @return The formatted current date string.
     */
    public static String getCurrentDate(String transactionId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = currentDate.format(formatter);
        log.info("Transaction Id : {}, Current Date : {}", transactionId, formattedDate);
        return formattedDate;
    }
}
