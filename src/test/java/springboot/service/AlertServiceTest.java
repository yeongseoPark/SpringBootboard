package springboot.service;


import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.BeforeTransaction;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertRepository;
import springboot.domain.alert.AlertType;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;
import springboot.firebase.FcmService;
import springboot.firebase.NotificationRequest;
import springboot.firebase.NotificationService;
import springboot.web.WithMockCustomUser;
import springboot.web.dto.alert.alertSaveDto;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class) // Junit5 : @Test 애노테이션을 junit.jupiter.api.Test 로 사용중
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AlertServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockCustomUser
    @Sql(statements = "INSERT INTO user (name, email, role)\n" +
            "VALUES ('testName', 'testEmail@naver.com', 'USER');")
    public void testAlertUserByPrice() throws Exception {
        // Set up mock dependencies
        AlertRepository mockAlertRepository = mock(AlertRepository.class);
        FcmService mockFcmService = mock(FcmService.class);
        Map<String, String> mockTokenMap = mock(Map.class);
        when(mockTokenMap.get(anyString())).thenReturn("abc123");
        NotificationService mockNotificationService = mock(NotificationService.class);

        UserDetailService mockUserDetailService = new UserDetailService(userRepository);

        // Set up test data
        User user = userRepository.findByEmail("testEmail@naver.com").get();
        Alert alert = Alert.builder().user(user).ticker("bitcoin").alertType(AlertType.Lower_break).price(100.1).percentage(12.2).build();
        mockAlertRepository.save(alert);

        when(mockAlertRepository.findById(1L)).thenReturn(Optional.of(alert));

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .title("ABC alert")
                .message("100.0 broke down")
                .token("abc123")
                .build();

        when(mockNotificationService.getToken(anyString())).thenReturn("abc123");

        // Create an instance of the service and inject the mock dependencies
        AlertService service = new AlertService(mockAlertRepository, mockUserDetailService, mockNotificationService);

        doNothing().when(mockNotificationService).sendNotification(notificationRequest);

        // Call the method under test
        service.AlertUserByPrice(user.getId());
    }
}
