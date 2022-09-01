package springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT) // 스프링부트의 내장 서버를 랜덤 포트로 띄운다 , TestRestTemplate를 주입받아 테스트한다
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void MainPage_loading() {
        // when
        String body = restTemplate.getForObject("/", String.class);

        // then
        Assertions.assertThat("스프링 부트 테스트");
    }
}
