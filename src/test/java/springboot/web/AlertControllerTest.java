package springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;

import java.util.Locale;

@ExtendWith(SpringExtension.class) // Junit5 : @Test 애노테이션을 junit.jupiter.api.Test 로 사용중
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class AlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlertController alertController;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        ViewResolver viewResolver = new MustacheViewResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(alertController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser
    @Sql(statements = "INSERT INTO user (name, email, role)\n" +
            "VALUES ('testName', 'testEmail@naver.com', 'USER');")
    public void AlertPage_loading_test() throws Exception {
        String body = mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(body).contains("알림 등록");
    }


    @Test
    public void AlertsSave_test() throws Exception {

    }

    @Test
    public void AlertUserbyPrice_test() {

    }

    @Test
    public void AlertUserbyPercentage_test() {

    }
}


