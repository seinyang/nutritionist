package sein.nutritionist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sein.nutritionist.domain.Like;
import sein.nutritionist.domain.Post;
import sein.nutritionist.domain.User;
import sein.nutritionist.error.BusinessException;
import sein.nutritionist.error.ErrorCode;
import sein.nutritionist.repository.LikeRepository;
import sein.nutritionist.repository.PostRepository;
import sein.nutritionist.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;
    /**
     * 좋아요 토글 기능
     * - 없으면 새로 생성
     * - 있으면 active 값 반전
      **/

    public boolean toggle(Long postId,String userId){
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        //유저 조회
        User user =userRepository.findByUserId(userId)
                .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 기존 좋아요가 있으면 가져오고, 없으면 새 객체 생성
        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseGet(() -> {
                    Like l = new Like();
                    l.setUser(user);
                    l.setPost(post);
                    l.setActive(false); // 기본은 false로
                    return l;
                });

        // 상태 토글
        like.setActive(!like.isActive());
        likeRepository.save(like);

        // 게시글의 likeCount를 실제 DB count 값으로 동기화
        long cnt = likeRepository.countByPostAndActiveTrue(post);
        post.setLikeCount((int) cnt);

        return like.isActive();
    }

    //특정 게시글의 좋아요 개수
    @Transactional(readOnly = true)
    public long count(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new BusinessException(ErrorCode.POST_NOT_FOUND));

        return likeRepository.countByPostAndActiveTrue(post);
    }

    //내가 특정 게시글에 좋아요 했는지 여부
    @Transactional(readOnly = true)
    public boolean myStatus(Long postId, String userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return likeRepository.existsByUserAndPostAndActiveTrue(user,post);
    }
}
