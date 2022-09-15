package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springboot.service.comments.CommentService;
import springboot.service.posts.PostsService;
import springboot.web.dto.CommentSaveRequestDto;

@RequiredArgsConstructor
@Controller
public class CommentsApiController {

    private final CommentService commentService;

    @PostMapping("api/posts/{id}/comments")
    public ResponseEntity save(@RequestBody CommentSaveRequestDto requestDto, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.commentSave(requestDto, id));
    }
}
