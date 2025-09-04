package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum ErrorTypes {
    ALREADY_EXISTS_EXCEPTION(
            "AUTH_ALREADY_EXISTS",
            "Resource Already Exists",
            "The resource you are trying to create already exists. Please use a different value (e.g., email must be unique).",
            409,
            Map.of("field", "email")
    ),
    NOT_FOUND_EXCEPTION(
            "AUTH_NOT_FOUND",
            "Resource Not Found",
            "The requested resource could not be found. Please verify the information and try again.",
            404,
            Map.of("resource", "user")
    ),
    VALIDATION_EXCEPTION(
            "AUTH_VALIDATION_EXCEPTION",
            "Validation Failed",
            "Oops! Some of the data you sent doesn’t look right. Please review the fields and try again.",
            400,
            null
    ),
    INTERNAL_SERVER_ERROR(
            "AUTH_INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            "Something went wrong on our side. Please try again later or contact support if the issue persists.",
            500,
            Map.of("server", "Unexpected error occurred")
    );

    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final Map<String, String> errors;
}
