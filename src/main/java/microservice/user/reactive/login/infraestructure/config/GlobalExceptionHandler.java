package microservice.user.reactive.login.infraestructure.config;

import lombok.extern.slf4j.Slf4j;
import microservice.user.reactive.login.domain.exception.BusinessException;
import microservice.user.reactive.login.domain.exception.error.CommonErrorCode;
import microservice.user.reactive.login.infraestructure.driver.rest.dto.resp.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Captura las excepciones de negocio personalizadas.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        log.error("Business error: code={}, message={}", ex.getCode(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    /**
     * Captura excepciones de Spring (como cuando una ruta no existe o falta un parámetro).
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        String code = "ERR_" + ex.getStatusCode().value();
        String message = ex.getReason() != null ? ex.getReason() : ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(code, message);

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(errorResponse);
    }

    /**
     * Captura cualquier otro error inesperado (NullPointer, etc.).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error caught: ", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.INTERNAL_ERROR.getCode(),
                "An unexpected error occurred: " + ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
