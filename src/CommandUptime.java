import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommandUptime extends ListenerAdapter {
    private static final HashMap<String, Long> onlineMap = new HashMap<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (msg.startsWith(Main.bot.settings.getPrefix(event.getGuild().toString())) && msg.indexOf("up") == Main.bot.settings.getPrefix(event.getGuild().toString()).length()) {
            MessageChannel channel = event.getChannel();
            StringBuilder message = new StringBuilder();
            String server = null;
            if (msg.trim().contains(" ")) {
                server = msg.trim().split(" ")[1];
            }
            try {
                HashMap<String, ServerStatus> uptimeMap = UptimeThread.getUptimeMap();
                for (Map.Entry<String, ServerStatus> entry : uptimeMap.entrySet()) {
                    if (entry.getValue().isOnline) {
                        onlineMap.put(entry.getKey(), entry.getValue().changed);
                    } else {
                        onlineMap.remove(entry.getKey());
                    }
                }
                if (server == null) {
                    HashMap<String, Long> sortedOnlineMap = Bot.sortByLongValue(onlineMap);
                    for (Map.Entry<String, Long> entry : sortedOnlineMap.entrySet()) {
                        message.append(Bot.emojiGlobe).append(" ").append(String.format("%1$-4s", entry.getKey())).append(" | ").append(Bot.parseTimestampToHoursMinutes(System.currentTimeMillis() - entry.getValue())).append("\n");
                    }
                } else {
                    if (Bot.isNumeric(server))
                        server = "WC" + server;
                    server = server.toUpperCase(Locale.ROOT);
                    if (UptimeThread.isServerOnline(server))
                        message.append(Bot.emojiGlobe).append(" ").append(String.format("%1$-4s", server)).append(" | ").append(Bot.parseTimestampToHoursMinutes(System.currentTimeMillis() - onlineMap.get(server.toUpperCase(Locale.ROOT)))).append("\n");
                    else
                        message.append(server).append(" is offline");
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Bot.writeFile("commandUptime.log", e.getLocalizedMessage() + "\n");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            Bot.sendMessage(channel, message.toString());
        }
    }
}
