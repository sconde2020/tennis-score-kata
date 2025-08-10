package com.sconde.kata.infrastructure.exception;

import com.sconde.kata.domain.model.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Pattern ENUM_MSG_PATTERN =
            Pattern.compile("No enum constant .*\\.([A-Za-z0-9_]+)");

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        String originalMsg = ex.getMessage();
        if (originalMsg != null) {
            Matcher matcher = ENUM_MSG_PATTERN.matcher(originalMsg);
            if (matcher.find()) {
                String invalidValue = matcher.group(1);
                String validValues = Arrays.toString(Player.values());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid player code '" + invalidValue + "'. Valid values: " + validValues);
            }
        }

        // fallback for other IllegalArgumentExceptions
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(originalMsg != null ? originalMsg : "Invalid request parameter.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        String message = "Missing required request parameter: '" + paramName + "'";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}

