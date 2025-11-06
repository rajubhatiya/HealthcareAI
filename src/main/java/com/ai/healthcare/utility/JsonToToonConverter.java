package com.ai.healthcare.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonToToonConverter {

    public static String jsonToToon(String jsonArrayString, String arrayName) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Parse JSON string into List of Maps
        List<Map<String, Object>> data = mapper.readValue(jsonArrayString, new TypeReference<List<Map<String, Object>>>() {});

        if (data.isEmpty()) {
            return arrayName + "[0]{}";
        }

        // Get keys from first object
        List<String> keys = data.get(0).keySet().stream().toList();
        String header = arrayName + "[" + data.size() + "]{" + String.join(",", keys) + "}:";

        // Convert each object to comma-separated values
        String rows = data.stream()
                .map(obj -> keys.stream().map(k -> obj.get(k).toString()).collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));

        return header + "\n" + rows;
    }

    public static void main(String[] args) throws Exception {
        String json = "[{\"id\":1,\"name\":\"Alice\",\"role\":\"admin\"},{\"id\":2,\"name\":\"Bob\",\"role\":\"user\"}]";
        String toon = jsonToToon(json, "users");
        System.out.println(toon);
    }
}
