package sein.nutritionist.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T>{

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ApiError error;

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return ApiResponse.<T>builder().success(false).error(error).build();
    }


    @Getter @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class ApiError {
        private String code;
        private String message;     // 사용자 메시지
        private int status;         // HTTP status
        private String path;        // 요청 경로
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Map<String, Object> details; // 필드 오류
    }
}
