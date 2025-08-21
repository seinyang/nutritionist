package sein.nutritionist.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sein.nutritionist.error.ApiResponse;
import sein.nutritionist.error.BusinessException;
import sein.nutritionist.error.ErrorCode;
import sein.nutritionist.jwt.JwtTokenProvider;

import java.util.Map;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
@Tag(name = "jwt 인증")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/secure")
    @SecurityRequirement(name = "jwtToken")
    public ResponseEntity<ApiResponse<Map<String,Object>>> secureEndpoint() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(
                ApiResponse.ok(Map.of(
                        "message", "Hello, " + auth.getName(),
                        "userId", auth.getName()
                ))
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refresh(@RequestHeader(value = "Authorization", required = false) String authorization) {

        // 헤더 존재/형식 체크
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED, "Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }

        // Bearer 제거
        String token = authorization.substring(7).trim();

        // 검증 및 만료/변조 구분 (JwtTokenProvider에서 구분 가능하면 예외 매핑)
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        String username = jwtTokenProvider.getUserIdFromToken(token);
        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        return ResponseEntity.ok(
                ApiResponse.ok(Map.of("accessToken", newAccessToken))
        );
    }
}
