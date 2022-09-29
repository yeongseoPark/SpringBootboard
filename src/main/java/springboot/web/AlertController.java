package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springboot.domain.alert.AlertRepository;
import springboot.service.AlertService;
import springboot.domain.comment.alert.alertSaveDto;
import springboot.domain.comment.alert.alertResponseDto;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AlertController {

    private final AlertService alertService;
    private final AlertRepository alertRepository;

    @PostMapping("/alerts/save")
    public Long AlertSave(@RequestBody alertSaveDto alertSaveDto) {
        return alertService.save(alertSaveDto);
    }

    @GetMapping("/alerts")
    public String findAlert(Model model) {
        List<alertResponseDto> alerts = alertService.findByUser(model);

        if (alerts != null) {
            model.addAttribute("alerts", alerts);
        }

        return "alert";
    }

    @GetMapping("/alerts/1")
    public void AlertUser() {
        alertService.AlertUser();
    }
}
