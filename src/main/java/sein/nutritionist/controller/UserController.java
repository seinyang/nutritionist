package sein.nutritionist.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sein.nutritionist.error.ApiResponse;
import sein.nutritionist.repository.logindto.LoginRequestDto;
import sein.nutritionist.repository.logindto.SignupRequestDto;
import sein.nutritionist.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "회원")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequestDto dto) {
        Map<String, String> tokens = userService.login(dto);

        return ResponseEntity.ok(ApiResponse.ok(tokens));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String,Object>>> signup(@RequestBody @Valid SignupRequestDto requestDto){
            userService.signup(requestDto);

        Map<String, Object> body = Map.of(
                "message", "회원가입 성공",
                "userId", requestDto.getUserId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(body));
    }
}
