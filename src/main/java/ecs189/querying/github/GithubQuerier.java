package ecs189.querying.github;

import ecs189.querying.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 10/1/2017.
 */
public class GithubQuerier {

    private static final String BASE_URL = "https://api.github.com/users/";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        //sb.append("<style> body { background-image: url("githubImage.jpg"); }");
        for (int i = 0; i < response.size(); i++) {
            JSONObject event = response.get(i);
            // Get event type
            String type = event.getString("type");
            int count = 0;

            if (type.equals("PushEvent")) {
                // Get created_at date, and format it in a more pleasant style

                if (count == 10) {
                    break;
                }

                String creationDate = event.getString("created_at");
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
                Date date = inFormat.parse(creationDate);
                String formatted = outFormat.format(date);

                JSONObject obj = event.getJSONObject("payload");
                //JSONObject commits = obj.getJSONObject("commits");
                JSONArray jsonArr = obj.getJSONArray("commits");


                // Add type of event as header
                sb.append("<h3 class=\"type\">");
                sb.append(type);
                sb.append("</h3>");
                // Add formatted date
                sb.append("Commits on ");
                sb.append(formatted);
                sb.append("<br />");
                // Add commits

                for (int j = 0; j < jsonArr.length(); j++) {
                    JSONObject object = jsonArr.getJSONObject(j);
                    String sha = object.getString("sha");
                    String shaSub = sha.substring(0, 7);

                    String message = object.getString("message");

                    sb.append("<p style=\"margin-left: 40px\">");
                    sb.append(shaSub);
                    sb.append("<b>");
                    sb.append(" ");
                    sb.append(message);
                    sb.append("<br />");
                    sb.append("</b></p>");


                }

            }
        }
        //sb.append("</style>");
        sb.append("</div>");
        return sb.toString();

    }

    private static List<JSONObject> getEvents(String user) throws IOException {
        List<JSONObject> eventList = new ArrayList<JSONObject>();
        String url = BASE_URL + user + "/events";
        System.out.println(url);
        JSONObject json = Util.queryAPI(new URL(url));
        System.out.println(json);
        JSONArray events = json.getJSONArray("root");
        for (int i = 0; i < events.length() && i < 10; i++) {
            eventList.add(events.getJSONObject(i));
        }
        return eventList;
    }
}
