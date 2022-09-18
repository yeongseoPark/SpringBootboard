package springboot.web.dto.posts;

import lombok.Getter;
import springboot.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {
    private Long id;
    private String title;
    private String userName;
    private LocalDateTime modifiedDate;

    public PostsListResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.userName = entity.getUser().getName();
        this.modifiedDate = entity.getModifiedDate();
    }
}
