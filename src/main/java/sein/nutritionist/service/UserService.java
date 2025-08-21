package sein.nutritionist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sein.nutritionist.domain.User;
import sein.nutritionist.error.BusinessException;
import sein.nutritionist.error.ErrorCode;
import sein.nutritionist.jwt.JwtTokenProvider;
import sein.nutritionist.repository.UserRepository;
import sein.nutritionist.repository.logindto.LoginRequestDto;
import sein.nutritionist.repository.logindto.SignupRequestDto;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Map<String, String> login(LoginRequestDto dto) {
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @Transactional
    public void signup(SignupRequestDto requestDto){

        userRepository.findByUserId(requestDto.getUserId()).ifPresent(user -> {
            throw new BusinessException(ErrorCode.DUPLICATE_USER);
        });

        User user = requestDto.toEntity(passwordEncoder);

        userRepository.save(user);
    }

}
