package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum ErrorTypes {
    AUTHENTICATION_FAILED(
            "AUTH_AUTHENTICATION_FAILED",
            "Authentication Failed",
            "Invalid credentials. Please verify your email and password.",
            401,
            Map.of("reason", "invalid_credentials")
    ),
    BAD_CREDENTIALS(
            "AUTH_BAD_CREDENTIALS",
            "Bad Credentials",
            "Incorrect username or password.",
            401,
            Map.of("field", "password")
    ),
    TOKEN_MISSING(
            "AUTH_TOKEN_MISSING",
            "Missing Bearer Token",
            "Authorization header with a Bearer token is required.",
            401,
            Map.of("header", "Authorization")
    ),
    TOKEN_INVALID(
            "AUTH_TOKEN_INVALID",
            "Invalid Token",
            "The provided token is invalid or malformed.",
            401,
            Map.of("reason", "malformed_or_invalid")
    ),
    TOKEN_EXPIRED(
            "AUTH_TOKEN_EXPIRED",
            "Expired Token",
            "Your session has expired. Please authenticate again.",
            401,
            Map.of("action", "relogin")
    ),
    TOKEN_SIGNATURE_INVALID(
            "AUTH_TOKEN_SIGNATURE_INVALID",
            "Invalid Token Signature",
            "Token signature verification failed.",
            401,
            Map.of("reason", "signature_mismatch")
    ),
    TOKEN_UNSUPPORTED(
            "AUTH_TOKEN_UNSUPPORTED",
            "Unsupported Token",
            "The token type is not supported by this server.",
            401,
            Map.of("supported", "Bearer")
    ),
    TOKEN_REVOKED(
            "AUTH_TOKEN_REVOKED",
            "Revoked Token",
            "This token has been revoked and can no longer be used.",
            401,
            Map.of("action", "relogin")
    ),
    ACCESS_DENIED(
            "AUTH_ACCESS_DENIED",
            "Access Denied",
            "You do not have permission to access this resource.",
            403,
            Map.of("reason", "forbidden")
    ),
    CSRF_INVALID(
            "AUTH_CSRF_INVALID",
            "Invalid CSRF Token",
            "CSRF token is missing or invalid.",
            403,
            Map.of("header_or_cookie", "X-CSRF-TOKEN")
    ),
    CORS_REJECTED(
            "AUTH_CORS_REJECTED",
            "CORS Rejected",
            "The origin is not allowed by CORS policy.",
            403,
            Map.of("action", "check_allowed_origins")
    ),
    SECURITY_CONTEXT_MISSING(
            "AUTH_SECURITY_CONTEXT_MISSING",
            "Security Context Missing",
            "Security context could not be established for this request.",
            401,
            Map.of("reason", "missing_security_context")
    ),
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
