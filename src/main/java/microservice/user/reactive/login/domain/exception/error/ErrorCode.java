package microservice.user.reactive.login.domain.exception.error;

public interface ErrorCode {
    String getCode();
    String getMessage();
    int getStatusCode();
    ErrorCategory getCategory();
}
