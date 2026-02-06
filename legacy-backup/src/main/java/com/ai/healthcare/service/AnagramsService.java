package com.ai.healthcare.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnagramsService {

    public  List<List<String>> groupAnagrams(String[] arr) {
        Map<String, List<String>> map = new HashMap<>();

        for (String word : arr) {
            // Convert word to char array
            char[] chars = word.toCharArray();

            // Sort characters
            Arrays.sort(chars);

            // Create key
            String key = new String(chars);

            // Add word to map
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
        }

        // Return grouped anagrams
        return new ArrayList<>(map.values());
    }
}
