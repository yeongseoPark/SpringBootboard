package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.firebase.NotificationService;
import springboot.service.AlertService;
import springboot.web.dto.alert.alertSaveDto;
import springboot.web.dto.alert.alertResponseDto;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class AlertController {

    private final AlertService alertService;

    private final NotificationService notificationService;

    @PostMapping("/alerts/save")
    @ResponseBody
    public long AlertSave(@RequestBody alertSaveDto alertSaveDto) {
        return alertService.save(alertSaveDto);
    }

    @GetMapping("/alerts")
    public String findAlert(Model model) {

        List<alertResponseDto> alerts = alertService.findByUser(model);

        ArrayList<String> tickers = alertService.findAllTickers(model);

        if (alerts != null) {
            model.addAttribute("alerts", alerts);
        }

        model.addAttribute("tickers", tickers);

        return "alert";
    }

    @GetMapping("/alerts/{id}")
    @ResponseBody
    public void AlertUser(@PathVariable Long id) {
        alertService.AlertUser(id);
    }
}
