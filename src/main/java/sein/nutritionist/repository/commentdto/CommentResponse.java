package sein.nutritionist.repository.commentdto;

import lombok.Builder;
import lombok.Getter;
import sein.nutritionist.domain.Comment;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long authorId;
    private String authorName;
    private String content;

    public static CommentResponse from(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .postId(c.getPost().getId())
                .authorId(c.getAuthor().getId())
                .authorName(c.getAuthor().getName())
                .content(c.getContent())
                .build();
    }

}
