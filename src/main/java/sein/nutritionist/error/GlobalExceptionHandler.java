package sein.nutritionist.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<Void>> build(ErrorCode code, String message, String path, Map<String,Object> details) {

        ApiResponse.ApiError err = ApiResponse.ApiError.builder()
                .code(code.name())
                .message(message != null ? message : code.getDefaultMessage())
                .status(code.getStatus().value())
                .path(path)
                .details(details)
                .build();

        return ResponseEntity.status(code.getStatus()).body(ApiResponse.error(err));
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex, HttpServletRequest req) {
        log.warn("BusinessException: {} - {}", ex.getErrorCode(), ex.getMessage());
        return build(ex.getErrorCode(), ex.getMessage(), req.getRequestURI(), ex.getDetails());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, Object> details = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> Optional.of(fe.getDefaultMessage()).orElse("Invalid"),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        return build(ErrorCode.VALIDATION_ERROR, null, req.getRequestURI(), details);
    }

    // 단일 파라미터 검증 오류
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraint(Exception ex, HttpServletRequest req) {
        return build(ErrorCode.VALIDATION_ERROR, ex.getMessage(), req.getRequestURI(), null);
    }

    // 400 계열
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception ex, HttpServletRequest req) {
        return build(ErrorCode.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }


}
