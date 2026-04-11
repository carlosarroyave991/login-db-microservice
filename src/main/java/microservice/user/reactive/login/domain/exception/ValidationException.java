package microservice.user.reactive.login.domain.exception;

import microservice.user.reactive.login.domain.exception.error.ErrorCode;

public class ValidationException extends BusinessException {
    public ValidationException(ErrorCode errorCode) { super(errorCode); }
    public ValidationException(ErrorCode errorCode, String message) { super(errorCode, message); }
}
