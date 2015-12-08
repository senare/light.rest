package light.rest;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStreamReader;

/**
 * Created by manfre on 09/11/15.
 */
public class Bamboo {

    public static boolean status() {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("http://bamboo.etraveli.net/rest/api/latest/result/ITDEV-FEAT");
            getRequest.addHeader("accept", "application/json");
            getRequest.addHeader("http-user", "XXXXX");
            getRequest.addHeader("http-password", "XXXXX");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            JsonReader reader = new JsonReader(new InputStreamReader((response.getEntity().getContent())));

            long number = 0;
            String state = "";

            long currentNumber = 0;
            String currentState = "unknown";

            while (true) {
                JsonToken token = reader.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        reader.beginArray();
                        break;
                    case END_ARRAY:
                        reader.endArray();
                        break;
                    case BEGIN_OBJECT:
                        reader.beginObject();
                        break;
                    case END_OBJECT:
                        reader.endObject();
                        if (currentNumber > number) {
                            state = currentState;
                            number = currentNumber;
                        }
                        break;
                    case NAME:
                        String name = reader.nextName();


                        if ("state".equalsIgnoreCase(name)) {
                            currentState = reader.nextString();
                        }
                        if ("buildNumber".equalsIgnoreCase(name)) {
                            currentNumber = reader.nextLong();
                        }
                        break;
                    case STRING:
                        String s = reader.nextString();
                        break;
                    case NUMBER:
                        String n = reader.nextString();
                        break;
                    case BOOLEAN:
                        boolean b = reader.nextBoolean();
                        break;
                    case NULL:
                        reader.nextNull();
                        break;
                    case END_DOCUMENT:
                        httpClient.getConnectionManager().shutdown();
                        return "Successful".equals(state);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
