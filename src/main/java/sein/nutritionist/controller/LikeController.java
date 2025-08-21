package sein.nutritionist.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sein.nutritionist.error.ApiResponse;
import sein.nutritionist.repository.LikeRepository;
import sein.nutritionist.service.LikeService;

import java.util.Map;


@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
@Tag(name = "좋아요")
public class LikeController {

    private final LikeService likeService;


    private String currentUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    //좋아요 토글
    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggle(@PathVariable Long postId) {

        boolean liked = likeService.toggle(postId, currentUserId());
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "liked", liked,
                "message", liked ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다."
        )));

    }

    //좋아요 갯수
    @GetMapping("/likes/count")
    public ResponseEntity<ApiResponse<Map<String, Object>>> count(@PathVariable long postId){
        long count = likeService.count(postId);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("좋아요 갯수", count)));
    }

    //내가 좋아요 했는지 안했는지에 대한 여부
    @GetMapping("/likes/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> myStatus(@PathVariable Long postId) {
        boolean liked = likeService.myStatus(postId, currentUserId());
        return ResponseEntity.ok(ApiResponse.ok(Map.of("liked", liked)));
    }
}
