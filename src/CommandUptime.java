import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandUptime extends ListenerAdapter {
    private final HashMap<String, ServerStatus> uptimeMap = new HashMap<>();
    private final HashMap<String, Long> onlineMap = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(Main.bot.settings.getPrefix()) && msg.getContentRaw().contains("up")) {
            MessageChannel channel = event.getChannel();
            StringBuilder message = new StringBuilder();
            String server = null;
            if (msg.getContentRaw().trim().contains(" ")) {
                server = msg.getContentRaw().trim().split(" ")[1];
            }
            try {
                UptimeThread.getUptimeMap(uptimeMap);
                for (Map.Entry<String, ServerStatus> entry : uptimeMap.entrySet()) {
                    if (entry.getValue().isOnline) {
                        onlineMap.put(entry.getKey(), entry.getValue().changed);
                    }
                }
                if (server == null || Bot.isNumeric(server)) {
                    HashMap<String, Long> sortedOnlineMap = sortByValues(onlineMap);
                    int amount = Bot.isNumeric(server) ? Integer.parseInt(server): 5;
                    int first = amount;
                    for (Map.Entry<String, Long> entry : sortedOnlineMap.entrySet()) {
                        if (amount == first)
                            message.append(":star: ").append(entry.getKey()).append(": ").append(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - entry.getValue())).append(" min\n");
                        else
                            message.append(":earth_americas: ").append(entry.getKey()).append(": ").append(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - entry.getValue())).append(" min\n");
                        amount--;
                        if (amount == 0) break;
                    }
                } else {
                    message.append(":earth_americas: ").append(server.toUpperCase(Locale.ROOT)).append(": ").append(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - onlineMap.get(server.toUpperCase(Locale.ROOT)))).append(" min\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            String finalMessage = message.toString();
            if (!finalMessage.equals(""))
                channel.sendMessage(finalMessage).queue();
            else
                channel.sendMessage("There was an error processing your request").queue();
        }
    }

    private static HashMap<String, Long> sortByValues(HashMap<String, Long> map) {
        List<Map.Entry<String, Long>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<String, Long> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) sortedHashMap.put(entry.getKey(), entry.getValue());
        return sortedHashMap;
    }
}
