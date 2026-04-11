package microservice.user.reactive.login.domain.exception;

import microservice.user.reactive.login.domain.exception.error.ErrorCode;

public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(ErrorCode errorCode) { super(errorCode); }
    public DuplicateResourceException(ErrorCode errorCode, String message) { super(errorCode, message); }
}
