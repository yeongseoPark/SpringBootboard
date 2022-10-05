package springboot.firebase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.Login.LoginUser;
import springboot.config.auth.dto.SessionUser;

@RestController
public class NotificationApiController {

    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody String token, @LoginUser SessionUser sessionUser) {
        notificationService.register(sessionUser.getEmail(), token);
        return ResponseEntity.ok().build();
    }
}
