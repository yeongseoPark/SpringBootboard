package springboot.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertRepository;
import springboot.domain.user.User;
import springboot.domain.comment.alert.alertResponseDto;
import springboot.domain.comment.alert.alertSaveDto;
import springboot.utils.WebsocketClientEndpoint;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserDetailService userDetailService;

    public long save(@NotNull alertSaveDto alertSaveDto) {
        User user = userDetailService.returnUser();
        alertSaveDto.setUser(user);

        return alertRepository.save(alertSaveDto.toEntity()).getId();
    }

    public ArrayList<alertResponseDto> findByUser(Model model) {
        User user = userDetailService.returnUser();

        List<Alert> alerts = alertRepository.findByUser(user.getId());

        ArrayList<alertResponseDto> alertsDto = new ArrayList<>();

        for (Alert alert : alerts) {
            alertsDto.add(new alertResponseDto(alert));
        }

        model.addAttribute("alerts", alerts);

        return alertsDto;
    }

    public void AlertUser() {
         Alert alert = alertRepository.findAll().get(0);
         double SetPrice = alert.getPrice();
        JSONParser jsonParser = new JSONParser();

        WebsocketClientEndpoint.MessageHandler handler = new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) throws ParseException {

                Object obj = jsonParser.parse(message);
                JSONObject jsonObject = (JSONObject) obj;

                while (true) {
                    double price = Double.parseDouble(jsonObject.get("bitcoin").toString());
                    System.out.println(price);

                    if (price < SetPrice) {
                        System.out.println("끝");
                        break;
                    }
                }

                // 여기에 닫는 코드를..
            }
        };

        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://ws.coincap.io/prices?assets=bitcoin"));

            clientEndPoint.addMessageHandler(handler);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                System.err.println("InterruptedException exception: " + ex.getMessage());
            }

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}