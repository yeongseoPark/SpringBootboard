package springboot.web.dto.posts;

import lombok.Getter;
import springboot.domain.posts.Posts;
import springboot.web.dto.comment.CommentResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    private List<CommentResponseDto> comments;

    public PostsResponseDto(Posts entity) {
        this.id       = entity.getId();
        this.title    = entity.getTitle();
        this.content  = entity.getContent();
        this.author   = entity.getAuthor();
        this.comments = entity.getComments()
                .stream().map(CommentResponseDto::new)
                .collect(Collectors.toList());
        }
    }
