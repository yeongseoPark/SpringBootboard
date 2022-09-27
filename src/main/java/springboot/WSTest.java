package springboot;

import com.fasterxml.jackson.core.JsonParser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import springboot.utils.WebsocketClientEndpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSTest {

    public static void main(String[] args) throws IOException {
        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://ws.coincap.io/prices?assets=bitcoin"));
            JSONParser jsonParser = new JSONParser();

            while (true) {
                clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                    public void handleMessage(String message) throws ParseException {
                        System.out.println("msg = " + message);

                        Object obj = jsonParser.parse(message);
                        JSONObject jsonObject = (JSONObject) obj;
                        System.out.println(jsonObject.get("bitcoin"));
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