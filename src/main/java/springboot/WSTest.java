package springboot;

import springboot.utils.WebsocketClientEndpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSTest {

    public static void main(String[] args) throws IOException {
        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://ws.coincap.io/prices?assets=bitcoin"));

            clientEndPoint.addMessageHandler (new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println("msg = " + message);
                }
            });

            // send message to websocket
//            clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");

            // wait seconds for messages from websocket
            Thread.sleep(10000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}