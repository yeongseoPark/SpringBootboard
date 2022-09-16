package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springboot.service.comments.CommentService;
import springboot.web.dto.comment.CommentSaveRequestDto;

@RequiredArgsConstructor
@RestController // view 페이지가 아닌, 데이터를 그대로 반환할 수 있도록 하기 위하여 @ResponseBody가 들어간 해당 애노테이션 사용
public class CommentsApiController {

    private final CommentService commentService;

    @PostMapping("api/posts/{id}/comments")
    public ResponseEntity save(@RequestBody CommentSaveRequestDto requestDto, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.commentSave(requestDto, id));
    }

    @DeleteMapping("api/posts/{id}/comments/{CommentId}")
    public Long delete(@PathVariable Long id, @PathVariable Long CommentId) {
        commentService.delete(CommentId);
        return id;
    }
}
