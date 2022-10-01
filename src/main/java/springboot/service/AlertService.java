package springboot.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import springboot.WSTest;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertRepository;
import springboot.domain.user.User;
import springboot.domain.comment.alert.alertResponseDto;
import springboot.domain.comment.alert.alertSaveDto;
import springboot.utils.WebsocketClientEndpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserDetailService userDetailService;

    public void save(alertSaveDto alertSaveDto) {
        User user = userDetailService.returnUser();
        alertSaveDto.setUser(user);

        alertRepository.save(alertSaveDto.toEntity());
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

        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://ws.coincap.io/prices?assets=bitcoin"));
            JSONParser jsonParser = new JSONParser();

            while (true) {
                clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                    public void handleMessage(String message) throws ParseException {

                        Object obj = jsonParser.parse(message);
                        JSONObject jsonObject = (JSONObject) obj;

                        double price = (double) jsonObject.get("bitcoin");
                        System.out.println(price);

                        if (price < SetPrice) {
                            System.out.println("Alert!!");
                            return;
                        }
                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.err.println("InterruptedException exception: " + ex.getMessage());
                }
            }
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }


    }


}