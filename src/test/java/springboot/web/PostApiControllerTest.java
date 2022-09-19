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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springboot.domain.posts.Posts;
import springboot.domain.posts.PostsRepository;
import springboot.domain.user.Role;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;
import springboot.service.posts.PostsService;
import springboot.web.dto.posts.PostsSaveRequestDto;
import springboot.web.dto.posts.PostsUpdateRequestDto;

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

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(sharedHttpSession())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();

    }

    @Test
    @WithMockUser(roles="USER") // MockMVC에서만 작동
    public void posts_등록() throws Exception {
        //given
        String title = "title";
        String content = "content";
        User user = userRepository.save(User.builder()
                .name("name")
                .email("fake@naver.com")
                .picture("fakePic.com")
                .role(Role.USER)
                .build());

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        String url = "http://localhost:" + port + "api/v1/posts";

        //when

        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto))) // 문자열 JSON으로 변환
                                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
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

    @Test
    @WithMockUser(roles="USER")
    public void posts_조회() throws Exception { // 추가
        //given
        String title = "gettest";
        String content = "getcontent";
        User user = userRepository.save(User.builder()
                .name("name")
                .email("fake@naver.com")
                .picture("fakePic.com")
                .role(Role.USER)
                .build());

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        postsService.save(requestDto);

       Assertions.assertThat(postsRepository.findAll().size()).isEqualTo(1);

       Posts postex = postsRepository.findAll().get(0);
        System.out.println(postex.getId());

       String url2 = "http://localhost:" + port + "api/v1/posts/" + postex.getId();
       // id가 1로 초기화되지 않고 계속 증가중이어서 이렇게 처리함. 계속 1로 초기화되도록 처리 필요

       mvc.perform(get(url2)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("gettest"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("getcontent"));

    }
}