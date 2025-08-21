package sein.nutritionist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sein.nutritionist.domain.Comment;
import sein.nutritionist.domain.Post;
import sein.nutritionist.domain.User;
import sein.nutritionist.error.BusinessException;
import sein.nutritionist.error.ErrorCode;
import sein.nutritionist.repository.CommentRepository;
import sein.nutritionist.repository.PostRepository;
import sein.nutritionist.repository.UserRepository;
import sein.nutritionist.repository.commentdto.CommentCreateRequest;
import sein.nutritionist.repository.commentdto.CommentResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 작성
    public CommentResponse add(Long postId, CommentCreateRequest request,String userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        User author = userRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Comment c = new Comment();
        c.setPost(post);
        c.setAuthor(author);
        c.setContent(request.getContent());

        Comment saved = commentRepository.save(c);
        return CommentResponse.from(saved);
    }

    //댓글 목록
    @Transactional(readOnly = true)
    public Page<CommentResponse> list(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findByPost(post, pageable)
                .map(CommentResponse::from);
    }


    //댓글 수정
    public  CommentResponse update(Long postId, Long commentId, CommentCreateRequest request, String userId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 해당 댓글이 지정된 postId에 속하는지 확인
        if (!comment.getPost().getId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_POST_NOT_FOUND);
        }

        //본인확인
        if (!comment.getAuthor().getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_EDIT_AUTHOR);
        }

        comment.setContent(request.getContent());
        return CommentResponse.from(comment);
    }

    //댓글 삭제
    public void delete(Long postId, Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(postId)) {
            throw new BusinessException(ErrorCode.COMMENT_POST_NOT_FOUND);
        }

        if (!comment.getAuthor().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_AUTHOR);
        }

        commentRepository.delete(comment);
    }

}
