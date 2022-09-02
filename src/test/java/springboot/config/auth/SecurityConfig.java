package springboot.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import springboot.domain.user.Role;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@RequiredArgsConstructor
@EnableWebSecurity /* Spring security 설정들 활성화 */
@Configuration
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;

    // WebSecurityConfigurerAdapter가 deprecate되어 Bean을 통해 SecuriyFilterChain 등록하는 방식으로 전환
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()  /* URL별 권한관리 설정의 시작점 : 이게 있어야 antMatchers 사용가능 */
                    // 권한 관리 대상 지정(URL, HTTP 메소드별)
                    .antMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // USER권한을 가진 사람만 "/api/v1/**" 주소 가진 API 사용 가능
                    .anyRequest().authenticated() // 설정된 값들 이외에 나머지 URL들은 모두 인증된 사용자들에게만 허용

                .and()
                    .logout()
                        .logoutSuccessUrl("/") // 로그아웃 성공시 / 주소로 이동
                .and()
                    .oauth2Login() // OAuth2 로그인 기능에 대한 설정의 진입점
                        .userInfoEndpoint() // 사용자 정보 가져올 때의 설정
                            .userService(customOAuth2UserService);
                            // 로그인 성공 후 후속 조치 진행할 UserService인터페이스의 구현체 등록
    }
}
