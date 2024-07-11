package kopis.k_backend.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


import kopis.k_backend.global.api_payload.ApiResponse;
import kopis.k_backend.global.api_payload.ErrorCode;
import kopis.k_backend.global.api_payload.ReasonDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(e, ErrorCode.valueOf(errorMessage),
                HttpHeaders.EMPTY, request);
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onThrowException(GeneralException generalException,
                                                   HttpServletRequest request) {
        ReasonDto errorReasonHttpStatus = generalException.getReason();
        return handleExceptionInternal(generalException, errorReasonHttpStatus, null, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage())
                            .orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", "
                                    + newErrorMessage);
                });

        return handleExceptionInternalArgs(e, HttpHeaders.EMPTY, ErrorCode.BAD_REQUEST, request,
                errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();

        return handleExceptionInternalFalse(e, ErrorCode.INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY,
                ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(), request, e.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ReasonDto reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(),
                null);

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e,
                                                                ErrorCode errorCode, HttpHeaders headers, HttpStatus status,
                                                                WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCode.getCode(),
                errorCode.getMessage(),
                errorPoint);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers,
                                                               ErrorCode errorCode, WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCode.getCode(),
                errorCode.getMessage(),
                errorArgs);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCode.getHttpStatus(),
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e,
                                                                     ErrorCode errorCode, HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCode.getCode(),
                errorCode.getMessage(),
                null);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCode.getHttpStatus(),
                request
        );
    }

}
