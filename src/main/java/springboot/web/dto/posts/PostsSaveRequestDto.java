package springboot.web.dto.posts;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.domain.posts.Posts;
import springboot.domain.user.User;

@Data
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private User user;

    @Builder
    public PostsSaveRequestDto(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
