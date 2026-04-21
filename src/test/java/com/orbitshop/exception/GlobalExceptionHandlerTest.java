package com.orbitshop.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbitshop.common.exception.BusinessException;
import com.orbitshop.common.exception.GlobalExceptionHandler;
import com.orbitshop.common.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.testng.annotations.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.DummyController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @RestController
    @RequestMapping("/api/v1/test")
    static class DummyController {

        @GetMapping("/not-found")
        public void notFound() {
            throw new ResourceNotFoundException("Product not found");
        }

        @GetMapping("/business")
        public void business() {
            throw new BusinessException("Business rule violated");
        }

        @GetMapping("/forbidden")
        public void forbidden() {
            throw new AccessDeniedException("Access denied");
        }

        @PostMapping("/validation")
        public void validation(@Valid @RequestBody DummyRequest request) {
        }
    }

    static class DummyRequest {
        @NotBlank(message = "name must not be blank")
        public String name;
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Product not found"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.correlationId").exists());
    }

    @Test
    void shouldReturnBusinessException() throws Exception {
        mockMvc.perform(get("/api/v1/test/business"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Business rule violated"));
    }

    @Test
    void shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/test/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.title").value("Forbidden"));
    }

    @Test
    void shouldReturnValidationErrors() throws Exception {
        mockMvc.perform(post("/api/v1/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DummyRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("name must not be blank"));
    }
}