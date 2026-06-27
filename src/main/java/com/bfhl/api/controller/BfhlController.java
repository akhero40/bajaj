package com.bfhl.api.controller;

import com.bfhl.api.dto.BfhlRequestDto;
import com.bfhl.api.dto.BfhlResponseDto;
import com.bfhl.api.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing the /bfhl POST endpoint.
 */
@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*")
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl
     *
     * Accepts a JSON body with a "data" array and returns categorized results.
     *
     * @param requestDto validated request body
     * @return 200 OK with BfhlResponseDto
     */
    @PostMapping
    public ResponseEntity<BfhlResponseDto> processData(
            @Valid @RequestBody BfhlRequestDto requestDto) {

        BfhlResponseDto response = bfhlService.processData(requestDto);
        return ResponseEntity.ok(response);
    }
}
