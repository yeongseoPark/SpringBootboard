package springboot.web.dto;

import lombok.Getter;
import springboot.domain.comment.Comment;
import springboot.domain.posts.Posts;
import springboot.domain.user.User;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private User user;
    private Posts posts;

    /* Entity -> Dto */
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.user = comment.getUser();
        this.posts = comment.getPosts();
    }
}
