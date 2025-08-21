package sein.nutritionist.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sein.nutritionist.error.ApiResponse;
import sein.nutritionist.repository.postdto.PostRequestDto;
import sein.nutritionist.repository.postdto.PostResponseDto;
import sein.nutritionist.service.PostService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "게시글 CRUD")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private String getCurrentUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    //게시글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String,Object>>> createPost(@RequestBody PostRequestDto dto){
        postService.createPost(dto,getCurrentUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(Map.of("message", "게시글이 작성되었습니다.")));
    }

    //게시글 전체 조회 (최신순)
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllPosts() {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPosts()));
    }

    //게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPost(id)));
    }

    //게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updatePost(@PathVariable Long id,
                                             @RequestBody PostRequestDto dto) {
        postService.updatePost(id, dto, getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "게시글이 수정되었습니다.")));
    }

    //게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deletePost(@PathVariable Long id){
        postService.deletePost(id,getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "게시글이 삭제되었습니다.")));
    }
}

