package com.orbitshop.dummy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO used to simulate validation scenarios.
 */
@Schema(description = "Dummy request used to validate input and trigger validation errors.")
public record DummyRequest(

        @NotBlank(message = "Name must not be blank")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        @Schema(description = "User's name", example = "John Doe")
        String name,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        @Schema(description = "User's email", example = "john.doe@example.com")
        String email
) { }