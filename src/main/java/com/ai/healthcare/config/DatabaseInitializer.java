package com.ai.healthcare.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initVectorDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector");
                System.out.println("Verified 'vector' extension in PostgreSQL.");
            } catch (Exception e) {
                System.err.println(
                        "Failed to Create 'vector' extension. Ensure your database user has permissions. Error: "
                                + e.getMessage());
            }
        };
    }
}
