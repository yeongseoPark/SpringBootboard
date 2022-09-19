package springboot.web.dto.comment;

import lombok.Getter;
import springboot.domain.comment.Comment;
import springboot.domain.posts.Posts;
import springboot.domain.user.User;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String userName;
    private Posts posts;

    /* Entity -> Dto */
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.userName = comment.getUser().getName();
        this.posts = comment.getPosts();
    }
}
