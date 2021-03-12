import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CommandWc extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(Main.bot.settings.getPrefix()) && msg.getContentRaw().indexOf("wc") == Main.bot.settings.getPrefix().length()) {
            MessageChannel channel = event.getChannel();

            JSONObject json = null;
            try {
                json = Bot.readJsonFromUrl("https://api.wynncraft.com/public_api.php?action=onlinePlayers");
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

            if (Bot.isNumeric(cmd)) {
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
            } else if (cmd != null && cmd.trim().equals("chest")) {
                //TODO
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
                wcMap = Main.bot.sortByValue(wcMap);
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
}
