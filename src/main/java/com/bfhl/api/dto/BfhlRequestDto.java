package com.bfhl.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for the /bfhl POST endpoint.
 * Accepts an array of strings as input data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BfhlRequestDto {

    @NotNull(message = "data field is required and cannot be null")
    @JsonProperty("data")
    private List<String> data;
}
