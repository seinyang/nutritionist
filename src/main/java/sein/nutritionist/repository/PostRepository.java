package sein.nutritionist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sein.nutritionist.domain.Post;
import sein.nutritionist.domain.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    //사용자 기준 글 조회
    List<Post> findByAuthor(User author);

    //최신순
    List<Post> findAllByOrderByCreatedAtDesc();

    // 인기순
    List<Post> findAllByOrderByLikeCountDesc();
}
