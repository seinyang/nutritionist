package sein.nutritionist.repository.logindto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import sein.nutritionist.domain.User;
import sein.nutritionist.domain.UserRole;

import java.time.LocalDateTime;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotNull
    private UserRole role;

    private String name;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .role(this.role)
                .name(this.name)
                .build();
    }
}
