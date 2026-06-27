package com.bfhl.api.service;

import com.bfhl.api.dto.BfhlRequestDto;
import com.bfhl.api.dto.BfhlResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BfhlService.
 *
 * Processing rules:
 * - Numbers  → split into odd / even arrays (returned as strings)
 * - Alphabets → converted to UPPERCASE; individual characters are extracted
 *               even from multi-character tokens
 * - Special characters → everything that is neither alphanumeric nor a digit
 * - sum       → total numeric value of all number tokens, returned as string
 * - concat_string → all extracted alphabetic characters (in original order)
 *               reversed, then alternating-caps starting with uppercase
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    @Value("${app.user.full-name}")
    private String fullName;

    @Value("${app.user.dob}")
    private String dob;

    @Value("${app.user.email}")
    private String email;

    @Value("${app.user.roll-number}")
    private String rollNumber;

    @Override
    public BfhlResponseDto processData(BfhlRequestDto requestDto) {

        List<String> data = requestDto.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        long         totalSum         = 0;

        // Collect all extracted alpha characters (in traversal order)
        // for building concat_string
        StringBuilder allAlphaChars = new StringBuilder();

        for (String token : data) {
            if (token == null || token.isEmpty()) continue;

            if (isNumeric(token)) {
                // ── Number token ──────────────────────────────────────────
                long value = Long.parseLong(token);
                totalSum += value;

                if (value % 2 == 0) {
                    evenNumbers.add(token);          // keep as original string
                } else {
                    oddNumbers.add(token);
                }

            } else if (isPureAlpha(token)) {
                // ── Pure-alphabet token ───────────────────────────────────
                // Add the token uppercased to alphabets array
                alphabets.add(token.toUpperCase());

                // Also collect every individual character for concat_string
                for (char c : token.toCharArray()) {
                    allAlphaChars.append(c);
                }

            } else {
                // ── Mixed or special-character token ──────────────────────
                // Walk character by character
                boolean hasAlpha   = false;
                boolean hasSpecial = false;
                StringBuilder alphaBuffer = new StringBuilder();

                for (char c : token.toCharArray()) {
                    if (Character.isLetter(c)) {
                        hasAlpha = true;
                        alphaBuffer.append(c);
                        allAlphaChars.append(c);
                    } else if (!Character.isDigit(c)) {
                        hasSpecial = true;
                    }
                }

                if (hasAlpha) {
                    alphabets.add(alphaBuffer.toString().toUpperCase());
                }
                if (hasSpecial) {
                    // Re-extract only the special chars from this token
                    StringBuilder specials = new StringBuilder();
                    for (char c : token.toCharArray()) {
                        if (!Character.isLetterOrDigit(c)) {
                            specials.append(c);
                        }
                    }
                    specialCharacters.add(specials.toString());
                }
            }
        }

        // ── concat_string: reverse all alpha chars, then alternating caps ──
        String reversed  = allAlphaChars.reverse().toString();
        String concatStr = buildAlternatingCaps(reversed);

        // ── Build user_id ─────────────────────────────────────────────────
        String userId = fullName.toLowerCase().replace(" ", "_") + "_" + dob;

        return BfhlResponseDto.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(String.valueOf(totalSum))
                .concatString(concatStr)
                .build();
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Returns true if the token represents a valid integer (including negative).
     */
    private boolean isNumeric(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            Long.parseLong(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns true if every character in the token is an ASCII letter.
     */
    private boolean isPureAlpha(String token) {
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return !token.isEmpty();
    }

    /**
     * Builds an alternating-caps string starting with uppercase.
     * Position 0 → uppercase, 1 → lowercase, 2 → uppercase, …
     */
    private String buildAlternatingCaps(String input) {
        StringBuilder sb = new StringBuilder();
        int capIndex = 0; // tracks position among alphabetic chars only

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                if (capIndex % 2 == 0) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(Character.toLowerCase(c));
                }
                capIndex++;
            } else {
                sb.append(c); // preserve non-alpha as-is (shouldn't occur here)
            }
        }
        return sb.toString();
    }
}
