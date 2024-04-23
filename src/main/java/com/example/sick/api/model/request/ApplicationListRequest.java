package com.example.sick.api.model.request;

import com.example.sick.utils.ApplicationStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public record ApplicationListRequest(
        @NotNull(message = "Page number is required")
        @Min(value = 1, message = "Page number must be greater than 0")
        Long page,
        List<ApplicationStatus> STATUS,
        String searchQuery
) {
}
