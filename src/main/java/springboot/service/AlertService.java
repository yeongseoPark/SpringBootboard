package springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertRepository;
import springboot.domain.user.User;
import springboot.domain.comment.alert.alertResponseDto;
import springboot.domain.comment.alert.alertSaveDto;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserDetailService userDetailService;

    User user = userDetailService.returnUser();

    public Long save(alertSaveDto alertSaveDto) {

        alertSaveDto.setUser(user);

        return alertRepository.save(alertSaveDto.toEntity()).getId();
    }

    public ArrayList<alertResponseDto> findByUser() {
        List<Alert> alerts = alertRepository.findByUser(user.getId());

        ArrayList<alertResponseDto> alertsDto = new ArrayList<>();

        for (Alert alert : alerts) {
            alertsDto.add(new alertResponseDto(alert));
        }

        return alertsDto;
    }
}