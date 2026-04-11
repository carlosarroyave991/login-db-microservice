package microservice.user.reactive.login.domain.exception;

import microservice.user.reactive.login.domain.exception.error.ErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorCode errorCode) { super(errorCode); }
    public UnauthorizedException(ErrorCode errorCode, String message) { super(errorCode, message); }
}
