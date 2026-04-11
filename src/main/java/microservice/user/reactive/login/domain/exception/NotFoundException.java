package microservice.user.reactive.login.domain.exception;

import microservice.user.reactive.login.domain.exception.error.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(ErrorCode errorCode) { super(errorCode); }
    public NotFoundException(ErrorCode errorCode, String message) { super(errorCode, message); }
}
