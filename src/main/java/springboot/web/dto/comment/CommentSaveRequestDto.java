package springboot.web.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import springboot.domain.comment.Comment;
import springboot.domain.posts.Posts;
import springboot.domain.user.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentSaveRequestDto {
    private String comment;
    private Posts posts;

    /* Dto -> Entity */
    public Comment toEntity() {
        return Comment.builder()
                .comment(comment)
                .posts(posts)
                .build();
    }
}
