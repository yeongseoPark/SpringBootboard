package springboot.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertRepository;
import springboot.domain.user.User;
import springboot.web.dto.alert.alertResponseDto;
import springboot.web.dto.alert.alertSaveDto;
import springboot.firebase.NotificationRequest;
import springboot.firebase.NotificationService;
import springboot.utils.WebsocketClientEndpoint;

import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserDetailService userDetailService;

    private final NotificationService notificationService;

    public ArrayList findAllTickers(Model model){
        ArrayList<String> arr = null;
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            Request request = new Request.Builder()
                    .url("http://api.coincap.io/v2/assets")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(response.body().string());
            JSONArray jsonArray = (JSONArray) ((JSONObject) obj).get("data");

            Iterator<JSONObject> it = jsonArray.iterator();

            arr = new ArrayList<>();

            while (it.hasNext()) {
                String ticker;
                ticker = (String) it.next().get("id");

                arr.add(ticker);
            }

        } catch (IOException ie) {
            System.err.println("IOExcption " + ie.getMessage());
        } catch (ParseException pe) {
            System.err.println("ParseException " + pe.getMessage());
        }

        model.addAttribute("tickers",arr);

        return arr;
    }

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

    public void AlertUser(Long id) {

        Alert alert = alertRepository.findById(id).orElseThrow(() -> new NoSuchElementException());

        double SetPrice = alert.getPrice();
         String ticker = alert.getTicker();

        JSONParser jsonParser = new JSONParser();

        final NotificationRequest build = NotificationRequest.builder()
                .title(ticker + " alert")
                .message(SetPrice + "broke down")
                .token(notificationService.getToken(userDetailService.returnUser().getEmail()))
                .build();

        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint();

            Session session = clientEndPoint.connect(new URI("wss://ws.coincap.io/prices?assets=" + ticker));

            WebsocketClientEndpoint.MessageHandler handler = new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) throws ParseException, IOException {
                    Object obj = jsonParser.parse(message);

                    JSONObject jsonObject = (JSONObject) obj;

                    double price = Double.parseDouble(jsonObject.get(ticker).toString());

                    System.out.println("가격 : " + price);

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