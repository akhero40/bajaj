package com.bfhl.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error Response DTO returned when an exception occurs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    @JsonProperty("is_success")
    private boolean isSuccess = false;

    @JsonProperty("error")
    private String error;
}
