package springboot.domain.posts;

import javafx.geometry.Pos;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import springboot.config.auth.SecurityConfig;
import springboot.domain.user.Role;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;
import springboot.web.HelloController;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest

public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    @After // 단위 테스트 끝난 이후
    public void cleanup() {
        postsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void PostSave_findAll() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";
        User user = userRepository.save(User.builder()
                        .name("name")
                .email("fake@naver.com")
                .picture("fakePic.com")
                .role(Role.USER)
                .build());

        /* save 메서드로 insert/update 쿼리 실행 : id 값 있으면 update, 없으면 insert */
        postsRepository.save(Posts.builder() // .필드(값) ... build()
                .title(title)
                .content(content)
                .user(user)
                .build());

        //when
        List<Posts> postsLists = postsRepository.findAll();

        //then
        Posts posts = postsLists.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);

        User user = userRepository.save(User.builder()
                .name("name")
                .email("fake@naver.com")
                .picture("fakePic.com")
                .role(Role.USER)
                .build());

        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .user(user)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println(">>>>>> createdDate = " + posts.getCreatedDate()+ ", modifiedDate = " + posts.getModifiedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
