package com.ai.healthcare.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class WuUtilities {
    public static String generateMTCN(String transactionId, int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int num = ThreadLocalRandom.current().nextInt(10, 100); // two-digit numbers: 10–99
            result.append(num).append(" ");
        }

        log.info("Transaction Id : {}, Money Transfer Control Number (MTCN) : {}",transactionId,result.toString().trim());
        return result.toString().trim();
    }

    public static String getCurrentDate(String transactionId) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = currentDate.format(formatter);
        System.out.println(formattedDate);
        log.info("Transaction Id : {}, Current Date : {}",transactionId,formattedDate);
        return formattedDate;
    }
}
