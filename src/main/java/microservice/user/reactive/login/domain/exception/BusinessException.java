package microservice.user.reactive.login.domain.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import microservice.user.reactive.login.domain.exception.error.CommonErrorCode;
import microservice.user.reactive.login.domain.exception.error.ErrorCode;

@Getter
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;
    private String code;
    private String message;
    private int statusCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.statusCode = errorCode.getStatusCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = message;
        this.statusCode = errorCode.getStatusCode();
    }

    public BusinessException(String code, String message, int statusCode) {
        super(message);
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = CommonErrorCode.INTERNAL_ERROR;
    }
}
