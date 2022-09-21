package springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import springboot.domain.posts.Posts;
import springboot.domain.posts.PostsRepository;
import springboot.domain.user.Role;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;
import springboot.service.posts.PostsService;
import springboot.web.dto.posts.PostsResponseDto;
import springboot.web.dto.posts.PostsSaveRequestDto;
import springboot.web.dto.posts.PostsUpdateRequestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsService postsService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PostsApiController postsApiController;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build();
    }

    @Before
    public void setUser() {
        User user = new User("fakeUser","1park5@naver.com","fakePic.com",Role.USER);
        // 유저가 있어야 UserDetailService의 returnUser가 유저 가져올 수 있음

        userRepository.deleteAll();
        userRepository.save(user);
    }


    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @AfterTransaction // 조회 기능 테스트 후 teardown
    public void tearDownTransaction() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser
    public void posts_등록() throws Exception {
        //given
        String title = "postTitle";
        String content = "postContent";


        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .build();

//        String url = "http://localhost:" + port + "api/v1/posts";

        //when

        postsApiController.save(requestDto);

//        mvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(new ObjectMapper().writeValueAsString(requestDto))) // 문자열 JSON으로 변환
//                .andDo(print())
//                                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockCustomUser
    @Transactional
    public void posts_조회() throws Exception { // 추가
        //given
        String title = "gettest";
        String content = "getcontent";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        postsService.save(requestDto);

        Assertions.assertThat(postsRepository.findAll().size()).isEqualTo(1);

        Posts postex = postsRepository.findAll().get(0);
        System.out.println(postex.getId());

//       String url2 = "http://localhost:" + port + "api/v1/posts/" + postex.getId();

        PostsResponseDto post = postsApiController.findById(postex.getId());
        // id가 1로 초기화되지 않고 계속 증가중이어서 이렇게 처리함.

        assertThat(post.getContent()).isEqualTo("getcontent");
        assertThat(post.getTitle()).isEqualTo("gettest");


//       mvc.perform(get(url2)
//                       .contentType(MediaType.APPLICATION_JSON))
//               .andExpect(status().isOk())
//               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("gettest"))
//               .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("getcontent"));

    }


    @Test
    @WithMockUser(roles = "USER")
    public void posts_수정() throws Exception {
        // given
        User user = userRepository.save(User.builder()
                .name("name")
                .email("fake@naver.com")
                .picture("fakePic.com")
                .role(Role.USER)
                .build());

        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .user(user)
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
