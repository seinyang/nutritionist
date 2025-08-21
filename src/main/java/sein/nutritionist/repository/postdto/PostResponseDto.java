package sein.nutritionist.repository.postdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sein.nutritionist.domain.Post;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;
    private int likeCount;

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getAuthor().getName())
                .authorId(post.getAuthor().getId())
                .likeCount(post.getLikeCount())
                .build();
    }
}
