package springboot.domain.posts;

import javafx.geometry.Pos;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After // 단위 테스트 끝난 이후
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void PostSave_findAll() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        /* save 메서드로 insert/update 쿼리 실행 : id 값 있으면 update, 없으면 insert */
        postsRepository.save(Posts.builder() // .필드(값) ... build()
                .title(title)
                .content(content)
                .author("test@gmail.com")
                .build());

        //when
        List<Posts> postsLists = postsRepository.findAll();

        //then
        Posts posts = postsLists.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

}
