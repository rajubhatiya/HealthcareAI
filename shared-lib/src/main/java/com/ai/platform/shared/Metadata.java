package com.ai.platform.shared;

import java.util.Map;

public record Metadata(
        String department,
        int year,
        String docType,
        String securityLevel,
        String patientGroup,
        Map<String, Object> extra) {
}
