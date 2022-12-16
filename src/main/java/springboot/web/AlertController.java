package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertRepository;
import springboot.firebase.NotificationService;
import springboot.service.AlertService;
import springboot.web.dto.alert.alertSaveDto;
import springboot.web.dto.alert.alertResponseDto;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Controller
public class AlertController {

    private final AlertService alertService;
    private final AlertRepository alertRepository;

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
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such alert"));
        if (alert.getAlertType().getKey() == "u_break" || alert.getAlertType().getKey() == "l_break") {
            alertService.AlertUserByPrice(id);
        } else {
            alertService.AlertUserByPercentage(id);
        }
    }
}
