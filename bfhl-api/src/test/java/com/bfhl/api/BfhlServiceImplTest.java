package com.bfhl.api;

import com.bfhl.api.dto.BfhlRequestDto;
import com.bfhl.api.dto.BfhlResponseDto;
import com.bfhl.api.service.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for BfhlServiceImpl.
 * Covers the three provided examples plus additional edge cases.
 */
class BfhlServiceImplTest {

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
        // Inject @Value fields manually for unit tests
        ReflectionTestUtils.setField(service, "fullName",   "john_doe");
        ReflectionTestUtils.setField(service, "dob",        "17091999");
        ReflectionTestUtils.setField(service, "email",      "john@xyz.com");
        ReflectionTestUtils.setField(service, "rollNumber", "ABCD123");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Example A: ["a", "1", "334", "4", "R", "$"]
    // ──────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Example A - mixed input with number, alpha, special")
    void testExampleA() {
        BfhlRequestDto req = new BfhlRequestDto(
                Arrays.asList("a", "1", "334", "4", "R", "$"));

        BfhlResponseDto res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getUserId()).isEqualTo("john_doe_17091999");
        assertThat(res.getEmail()).isEqualTo("john@xyz.com");
        assertThat(res.getRollNumber()).isEqualTo("ABCD123");

        assertThat(res.getOddNumbers()).containsExactly("1");
        assertThat(res.getEvenNumbers()).containsExactly("334", "4");
        assertThat(res.getAlphabets()).containsExactly("A", "R");
        assertThat(res.getSpecialCharacters()).containsExactly("$");

        assertThat(res.getSum()).isEqualTo("339");
        // alpha chars in order: a, R  → reversed: Ra → alternating caps: Ra
        assertThat(res.getConcatString()).isEqualTo("Ra");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Example B: ["2", "a", "y", "4", "&", "-", "*", "5", "92", "b"]
    // ──────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Example B - multiple specials and alphabets")
    void testExampleB() {
        BfhlRequestDto req = new BfhlRequestDto(
                Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));

        BfhlResponseDto res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("5");
        assertThat(res.getEvenNumbers()).containsExactly("2", "4", "92");
        assertThat(res.getAlphabets()).containsExactly("A", "Y", "B");
        assertThat(res.getSpecialCharacters()).containsExactly("&", "-", "*");
        assertThat(res.getSum()).isEqualTo("103");
        // alpha order: a, y, b → reversed: b, y, a → alternating caps: ByA
        assertThat(res.getConcatString()).isEqualTo("ByA");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Example C: ["A", "ABCD", "DOE"]
    // ──────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Example C - only alphabets, no numbers or specials")
    void testExampleC() {
        BfhlRequestDto req = new BfhlRequestDto(
                Arrays.asList("A", "ABCD", "DOE"));

        BfhlResponseDto res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        // alpha chars: A, A, B, C, D, D, O, E
        // reversed:    E, O, D, D, C, B, A, A
        // alt caps:    E, o, D, d, C, b, A, a  → "EoDdCbAa"
        assertThat(res.getConcatString()).isEqualTo("EoDdCbAa");
    }

    // ──────────────────────────────────────────────────────────────────────
    // Edge cases
    // ──────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Empty data array returns zeroed response")
    void testEmptyArray() {
        BfhlRequestDto req = new BfhlRequestDto(List.of());
        BfhlResponseDto res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Zero is treated as even number")
    void testZeroIsEven() {
        BfhlRequestDto req = new BfhlRequestDto(List.of("0"));
        BfhlResponseDto res = service.processData(req);

        assertThat(res.getEvenNumbers()).containsExactly("0");
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
    }

    @Test
    @DisplayName("Large even number is categorised correctly")
    void testLargeEvenNumber() {
        BfhlRequestDto req = new BfhlRequestDto(List.of("1000000"));
        BfhlResponseDto res = service.processData(req);

        assertThat(res.getEvenNumbers()).containsExactly("1000000");
        assertThat(res.getSum()).isEqualTo("1000000");
    }

    @Test
    @DisplayName("Only special characters")
    void testOnlySpecials() {
        BfhlRequestDto req = new BfhlRequestDto(Arrays.asList("@", "#", "!"));
        BfhlResponseDto res = service.processData(req);

        assertThat(res.getSpecialCharacters()).containsExactly("@", "#", "!");
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Numbers are returned as strings")
    void testNumbersReturnedAsStrings() {
        BfhlRequestDto req = new BfhlRequestDto(Arrays.asList("1", "2", "3"));
        BfhlResponseDto res = service.processData(req);

        // Must be strings, not ints
        assertThat(res.getOddNumbers()).containsExactly("1", "3");
        assertThat(res.getEvenNumbers()).containsExactly("2");
        assertThat(res.getSum()).isInstanceOf(String.class).isEqualTo("6");
    }

    @Test
    @DisplayName("user_id follows full_name_ddmmyyyy pattern in lowercase")
    void testUserId() {
        BfhlRequestDto req = new BfhlRequestDto(List.of("1"));
        BfhlResponseDto res = service.processData(req);

        assertThat(res.getUserId()).matches("[a-z_]+_\\d{8}");
        assertThat(res.getUserId()).isEqualTo("john_doe_17091999");
    }
}
