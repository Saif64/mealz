package com.pixel.mealz.mealz.configs.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.pixel.mealz.mealz.features.auth.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors for request bodies annotated with @Valid.
     * Provides detailed feedback on which fields failed validation.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @return A ResponseEntity with a 400 Bad Request status and specific error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Collect all validation errors into a single, easy-to-read string.
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("'%s': %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        ApiResponse apiResponse = new ApiResponse(false, "Validation failed: " + errorMessage);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles errors from a missing, malformed, or unparsable request body.
     * It inspects the root cause to provide a more specific error message.
     *
     * @param ex The HttpMessageNotReadableException instance.
     * @return A ResponseEntity with a 400 Bad Request status and a detailed error message.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "There was an issue with the request body. Please check the format.";
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            // Catches errors where the provided value type doesn't match the expected type.
            String fieldPath = ife.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .collect(Collectors.joining("."));
            message = String.format("Invalid value '%s' provided for field '%s'. Expected a value compatible with type '%s'.",
                    ife.getValue(), fieldPath, ife.getTargetType().getSimpleName());
        } else if (cause instanceof MismatchedInputException mie) {
            // Catches general type mismatches, like providing an array where an object is expected.
            String fieldPath = mie.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .collect(Collectors.joining("."));
            if (fieldPath.isEmpty()) {
                message = "The request body is missing or is not in the expected JSON format.";
            } else {
                message = String.format("Incorrect data structure for field '%s'. Expected format is incompatible with the provided value.",
                        fieldPath);
            }
        } else if (cause instanceof JsonProcessingException jpe) {
            // A catch-all for other JSON processing errors.
            message = "Failed to process JSON request. " + jpe.getOriginalMessage();
        }

        ApiResponse apiResponse = new ApiResponse(false, message);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}