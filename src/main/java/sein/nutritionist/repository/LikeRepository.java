package sein.nutritionist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sein.nutritionist.domain.Like;
import sein.nutritionist.domain.Post;
import sein.nutritionist.domain.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    //유저+게시글 조합으로 좋아요 조회
    Optional<Like> findByUserAndPost(User user, Post post);

    //특정 게시글 활성화된 좋아요 갯수
    long countByPostAndActiveTrue(Post post);

    //내가 좋아요 했는지 안했는지
    boolean existsByUserAndPostAndActiveTrue(User user, Post post);
}
