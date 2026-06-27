package com.bfhl.api.service;

import com.bfhl.api.dto.BfhlRequestDto;
import com.bfhl.api.dto.BfhlResponseDto;

/**
 * Service interface defining the contract for BFHL data processing logic.
 */
public interface BfhlService {

    /**
     * Processes the incoming data array and returns categorized results.
     *
     * @param requestDto the incoming request containing the data array
     * @return BfhlResponseDto with categorized numbers, alphabets, special chars,
     *         sum, and alternating-caps concatenation
     */
    BfhlResponseDto processData(BfhlRequestDto requestDto);
}
