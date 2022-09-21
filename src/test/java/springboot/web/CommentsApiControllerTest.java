package springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import springboot.domain.comment.CommentRepository;
import springboot.domain.posts.Posts;
import springboot.domain.posts.PostsRepository;
import springboot.domain.user.Role;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;
import springboot.service.UserDetailService;
import springboot.service.comments.CommentService;
import springboot.service.posts.PostsService;
import springboot.web.dto.comment.CommentSaveRequestDto;
import springboot.web.dto.posts.PostsSaveRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CommentsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostsService postsService;


    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentsApiController commentsApiController;

    @Autowired
    private UserDetailService userDetailsService;

    @Autowired
    private WebApplicationContext context;

    @Autowired ObjectMapper objectMapper;

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
        commentRepository.deleteAll();
    }

    @AfterTransaction
    public void tearDownTransaction() throws Exception {
        postsRepository.deleteAll();
        commentRepository.deleteAll();
    }


    @Test
    @WithMockCustomUser
    @Transactional // 프록시 객체에 실제 데이터를 불러올 수 있게 영속성 컨텍스트에서 관리
    public void comment_등록() throws Exception {
        // given
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
        postsRepository.save(requestDto.toEntity());

        String comment = "comment";
        Posts posts = postsRepository.findAll().get(0);

        CommentSaveRequestDto saveRequestDto = CommentSaveRequestDto.builder()
                .comment(comment)
                .posts(posts)
                .build();

        Long id = posts.getId();

//        String url = "http://localhost:"+ port + "/api/v1/posts/" + id + "/comments";

        //when

        commentsApiController.save(saveRequestDto,id);

        //then

        assertThat(commentRepository.findAll().get(0).getComment()).isEqualTo("comment");

//        mvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(objectMapper.writeValueAsString(saveRequestDto)))
//                .andDo(print())
//                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles="USER")
    public void comment_삭제() throws Exception {

        // given
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
        postsRepository.save(requestDto.toEntity());

        String comment = "comment";
        Posts posts = postsRepository.findAll().get(0);

        CommentSaveRequestDto saveRequestDto = CommentSaveRequestDto.builder()
                .comment(comment)
                .posts(posts)
                .build();

        commentRepository.save(saveRequestDto.toEntity());

        Long commentId = commentRepository.findAll().get(0).getId();

        Long id = posts.getId();

        String url = "http://localhost:"+ port + "/api/posts/" + id + "/comments/" + commentId ;

        mvc.perform(delete(url))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(commentRepository.findAll().isEmpty());
    }
}
