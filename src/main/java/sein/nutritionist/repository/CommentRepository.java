package sein.nutritionist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sein.nutritionist.domain.Comment;
import sein.nutritionist.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByPostOrderByCreateAt(Post post);

    // 페이징/정렬 지원
    Page<Comment> findByPost(Post post, Pageable pageable);
}
