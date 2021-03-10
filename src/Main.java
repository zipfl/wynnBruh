import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Main())
                .setActivity(Activity.playing("children block game"))
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String prefix = "!";
        if (msg.getContentRaw().startsWith(prefix) && msg.getContentRaw().contains("wc")) {
            MessageChannel channel = event.getChannel();

            JSONObject json = null;
            try {
                json = readJsonFromUrl("https://api.wynncraft.com/public_api.php?action=onlinePlayers");
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert json != null;

            String cmd = null;
            if (msg.getContentRaw().trim().contains(" ")) {
                cmd = msg.getContentRaw().trim().split(" ")[1];
            }

            StringBuilder message = new StringBuilder();
            Iterator<String> keys = json.keys();

            if (isNumeric(cmd)) {
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.equals("request"))
                        continue;

                    if (json.get(key) instanceof JSONArray && key.equals("WC" + cmd)) {
                        message.append("> :earth_americas: ").append(key).append(": ").append(((JSONArray) json.get(key)).length()).append("\r\n");
                        for (int i = 0; i < ((JSONArray) json.get(key)).length(); i++) {
                            message.append(((JSONArray) json.get(key)).get(i));
                            message.append("\n");
                        }
                    }
                }
            } else {
                HashMap<String, Integer> wcMap = new HashMap<>();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.equals("request"))
                        continue;

                    if (json.get(key) instanceof JSONArray) {
                        if (((JSONArray) json.get(key)).length() != 0) {
                            wcMap.put(key, ((JSONArray) json.get(key)).length());
                        }
                    }
                }
                wcMap = sortByValue(wcMap);
                int i = 1;
                for (Map.Entry<String, Integer> en : wcMap.entrySet()) {
                    message.append(":earth_americas: ").append(en.getKey()).append(": ").append(en.getValue()).append("\r\n");
                    i++;
                    if (i > 10)
                        break;
                }
            }

            String finalMessage = message.toString();
            if (!finalMessage.equals(""))
                channel.sendMessage(finalMessage).queue();
            else
                channel.sendMessage("There was an error processing your request").queue();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
