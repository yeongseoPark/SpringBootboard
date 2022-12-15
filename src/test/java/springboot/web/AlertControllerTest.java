package springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.security.test.context.support.WithMockUser;
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

import java.util.Locale;

//@RunWith(SpringRunner.class) -> Junit4
@ExtendWith(SpringExtension.class) // Junit5 : @Test 애노테이션을 junit.jupiter.api.Test 로 사용중이기에.
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class AlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlertController alertController;

    @Before
    public void setup() {
        ViewResolver viewResolver = new MustacheViewResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(alertController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @WithMockUser(username = "email=1park4170@gmail.com,")
    public void AlertPage_loading() throws Exception {
        String body = mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(body).contains("알림 등록");
    }
}


