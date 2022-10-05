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
import springboot.firebase.NotificationRequest;
import springboot.firebase.NotificationService;
import springboot.utils.WebsocketClientEndpoint;

import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserDetailService userDetailService;

    private final NotificationService notificationService;

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

        final NotificationRequest build = NotificationRequest.builder()
                .title("bitcoin alert")
                .message(SetPrice + "broke down")
                .token(notificationService.getToken(userDetailService.returnUser().getEmail()))
                .build();

        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint();

            Session session = clientEndPoint.connect(new URI("wss://ws.coincap.io/prices?assets=bitcoin"));

            WebsocketClientEndpoint.MessageHandler handler = new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) throws ParseException, IOException {
                    Object obj = jsonParser.parse(message);

                    JSONObject jsonObject = (JSONObject) obj;

                    double price = Double.parseDouble(jsonObject.get("bitcoin").toString());
                    System.out.println(price);

                    if (price < SetPrice) {
                        System.out.println("끝");
                        notificationService.sendNotification(build);
                        session.close();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.err.println("InterruptedException exception: " + ex.getMessage());
                    }
                }
            };

            clientEndPoint.addMessageHandler(handler);

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}