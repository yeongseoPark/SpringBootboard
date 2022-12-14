package springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.domain.BaseTimeEntity;
import springboot.domain.comment.Comment;
import springboot.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE) // 기본이 지연로딩
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Builder
    public Posts(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.author = user;
    }

    public void update(String title , String content) {
        this.title = title;
        this.content = content;
    }
}
