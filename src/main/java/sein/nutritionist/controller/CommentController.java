package sein.nutritionist.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sein.nutritionist.domain.Comment;
import sein.nutritionist.error.ApiResponse;
import sein.nutritionist.repository.commentdto.CommentCreateRequest;
import sein.nutritionist.repository.commentdto.CommentResponse;
import sein.nutritionist.service.CommentService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 CRUD")
public class CommentController {

    private final CommentService commentService;

    private String currentUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    //댓글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(@PathVariable Long postId,
                                                                  @Valid @RequestBody CommentCreateRequest request){

        CommentResponse response = commentService.add(postId,request,currentUserId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }

    //댓글 목록(페이징)
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> list(@PathVariable Long postId, @ParameterObject Pageable pageable){
        Page<CommentResponse> page = commentService.list(postId, pageable);

        Map<String, Object> meta = Map.of(
                "page", page.getNumber(),
                "size", page.getSize(),
                "totalPages", page.getTotalPages(),
                "totalElements", page.getTotalElements(),
                "sorted", !page.getSort().isUnsorted()
        );

        return ResponseEntity.ok(ApiResponse.ok(page.getContent()));
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> update (@PathVariable Long postId, @PathVariable Long commentId,@Valid @RequestBody CommentCreateRequest request){
        CommentResponse response = commentService.update(postId,commentId,request,currentUserId());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId) {
        commentService.delete(postId,commentId, currentUserId());
        return ResponseEntity.ok(ApiResponse.ok(Map.of("message", "댓글이 삭제되었습니다.")));
    }

}
