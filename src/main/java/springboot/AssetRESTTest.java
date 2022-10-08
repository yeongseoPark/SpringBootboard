package springboot;

import com.squareup.okhttp.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.json.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class AssetRESTTest {
    public static void main(String[] args) throws IOException, ParseException {
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

        // JSONObject inside JSONArray
        // 근데 왜 .get 안써짐?? -> JSONObject로 형변환 해줘야 됨

//        System.out.println(jsonArray.get(0).getClass());
        // 아니 여기서 분명 class org.json.simple.JSONObject 로뜨는데

//        System.out.println(((JSONObject) jsonArray.get(0)).get("symbol"));
        // 왜 형변환 필요?

        Iterator<JSONObject> it = jsonArray.iterator();

        ArrayList<String> arr = new ArrayList<>();

        while(it.hasNext()) {
            String ticker;
            ticker = (String) it.next().get("symbol");

            arr.add(ticker);
        }

        System.out.println(arr.toString());
    }
}
