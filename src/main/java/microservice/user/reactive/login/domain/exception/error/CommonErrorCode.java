package microservice.user.reactive.login.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // Validation
    VALIDATION_ERROR("ERR_VALIDATION", "Validation error", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_INPUT("ERR_INVALID_INPUT", "Invalid input data", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_EMAIL("ERR_EMAIL_INVALID", "Invalid email format", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_PHONE("ERR_PHONE_INVALID", "Invalid phone number", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_PASSWORD("ERR_PASSWORD_INVALID", "Invalid password", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_DNI("ERR_DNI_INVALID", "Invalid dni", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_ROLE("ERR_ROLE_INVALID", "Invalid role", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),

    // Resource
    RESOURCE_NOT_FOUND("ERR_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    RESOURCE_ALREADY_EXISTS("ERR_DUPLICATE_RESOURCE", "Resource already exists", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    USER_NOT_FOUND("ERR_USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    USER_NAME_NOT_FOUND("ERR_USER_NAME_NOT_FOUND", "No matches were found.", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    DNI_NOT_FOUND("ERR_DNI_NOT_FOUND", "DNI not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    EMAIL_ALREADY_EXISTS("ERR_EMAIL_EXISTS", "Email already registered", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    DB_EMPTY("ERR_DB_EMPTY", "No data found in the database.", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),

    // Authentication
    UNAUTHORIZED("ERR_UNAUTHORIZED", "Unauthorized access", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),
    INVALID_CREDENTIALS("ERR_INVALID_CREDENTIALS", "Invalid credentials", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),
    INVALID_TOKEN("ERR_INVALID_TOKEN", "Invalid token", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),
    EXPIRED_TOKEN("ERR_EXPIRED_TOKEN", "Token has expired", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),

    // Authorization
    FORBIDDEN("ERR_SECURITY", "Access forbidden", HttpStatus.FORBIDDEN.value(), ErrorCategory.AUTHORIZATION),

    // System
    INTERNAL_ERROR("ERR_INTERNAL_SERVER", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.SYSTEM),
    DATA_ACCESS_ERROR("ERR_DATA_ACCESS", "Data access error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.DATA_ACCESS);

    private final String code;
    private final String message;
    private final int statusCode;
    private final ErrorCategory category;
}
