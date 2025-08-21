package sein.nutritionist.repository.commentdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {

    @NotBlank(message = "댓글 내용을 입력하세요.")
    private String content;

}
