package com.orbitshop.dummy.controller;

import com.orbitshop.common.exception.BusinessException;
import com.orbitshop.common.exception.ForbiddenException;
import com.orbitshop.common.exception.ResourceNotFoundException;
import com.orbitshop.common.exception.UnauthorizedException;
import com.orbitshop.dummy.dto.DummyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Dummy controller used to validate Swagger/OpenAPI documentation
 * and integration with the GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/v1/dummy")
@Tag(name = "Dummy", description = "Endpoints for testing Swagger documentation and exception handling")
public class DummyController {

    @GetMapping("/success")
    @Operation(summary = "Successful operation", description = "Returns a simple success message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<String> success() {
        return ResponseEntity.ok("Dummy endpoint executed successfully.");
    }

    @GetMapping("/not-found")
    @Operation(summary = "Simulate resource not found", description = "Throws ResourceNotFoundException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
    })
    public ResponseEntity<Void> notFound() {
        throw new ResourceNotFoundException("Dummy resource not found.");
    }

    @GetMapping("/business-error")
    @Operation(summary = "Simulate business rule violation", description = "Throws BusinessException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Business rule violation",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
    })
    public ResponseEntity<Void> businessError() {
        throw new BusinessException("Dummy business rule violated.");
    }

    @GetMapping("/unauthorized")
    @Operation(summary = "Simulate unauthorized access", description = "Throws UnauthorizedException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
    })
    public ResponseEntity<Void> unauthorized() {
        throw new UnauthorizedException("User is not authenticated.");
    }

    @GetMapping("/forbidden")
    @Operation(summary = "Simulate forbidden access", description = "Throws ForbiddenException.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
    })
    public ResponseEntity<Void> forbidden() {
        throw new ForbiddenException("User does not have permission to access this resource.");
    }

    @PostMapping("/validation")
    @Operation(summary = "Simulate validation error",
            description = "Triggers validation errors using Hibernate Validator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request is valid"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
    })
    public ResponseEntity<String> validation(@Valid @RequestBody DummyRequest request) {
        return ResponseEntity.ok("Validation successful.");
    }
}