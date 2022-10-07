package springboot.firebase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final FcmService fcmService;
    private final Map<String, String> tokenMap = new HashMap<>();

    public NotificationService(final FcmService fcmService) {
        this.fcmService = fcmService;
    }

    public void register(final String userEmail, final String token) {
        tokenMap.put(userEmail, token);
    }

    public void deleteToken(final String userEmail) {
        tokenMap.remove(userEmail);
    }

    public String getToken(final String userEmail) {
        return tokenMap.get(userEmail);
    }

    public void sendNotification(final NotificationRequest request) {
        try {
            fcmService.send(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }
}
