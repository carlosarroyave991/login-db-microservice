package microservice.user.reactive.login.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCategory {
    VALIDATION("Validation errors"),
    RESOURCE("Resource-related errors"),
    AUTHENTICATION("Authentication errors"),
    AUTHORIZATION("Authorization errors"),
    BUSINESS("Business logic errors"),
    DATA_ACCESS("Data access errors"),
    SYSTEM("System errors"),
    EXTERNAL_SERVICE("External service errors");

    private final String description;
}
