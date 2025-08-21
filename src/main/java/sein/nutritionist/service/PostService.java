package sein.nutritionist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sein.nutritionist.domain.Post;
import sein.nutritionist.domain.User;
import sein.nutritionist.error.BusinessException;
import sein.nutritionist.error.ErrorCode;
import sein.nutritionist.repository.PostRepository;
import sein.nutritionist.repository.UserRepository;
import sein.nutritionist.repository.postdto.PostRequestDto;
import sein.nutritionist.repository.postdto.PostResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    //게시글 작성
    public void createPost(PostRequestDto dto,String userId){

        User author = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    //게시글 조회 (최신순)
    public List<PostResponseDto> getAllPosts(){
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    //게시글 상세 조회
    public PostResponseDto getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        return PostResponseDto.from(post);
    }

    @Transactional
    //게시글 삭제
    public void deletePost(Long id, String userId){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        postRepository.delete(post);
    }

    @Transactional
    //게시글 수정
    public void updatePost(Long id,PostRequestDto dto , String userId){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_AUTHOR);
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
    }
}
