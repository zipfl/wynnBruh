import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CommandForceUpdate extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (msg.startsWith(Main.bot.settings.getPrefix(event.getGuild().toString())) && msg.indexOf("fu") == Main.bot.settings.getPrefix(event.getGuild().toString()).length()) {
            String arg = null;
            if (msg.trim().contains(" ")) {
                arg = msg.trim().split(" ")[1];
            }
            try {
                if (Bot.isNumeric(arg)) {
                    arg = "WC" + arg;
                    int playerCount = 0;
                    if (UptimeThread.isServerOnline(arg)) {
                        ArrayList<String> onlinePlayers = new ArrayList<>();
                        JSONObject json = new JSONObject(Bot.readLog("wc.log"));
                        for (int i = 0; i < ((JSONArray) json.get(arg)).length(); i++) {
                            onlinePlayers.add(((JSONArray) json.get(arg)).getString(i));
                        }

                        StringBuilder onlineLog = new StringBuilder(Bot.readLog("onlinePlayers.log"));
                        FileWriter fw = new FileWriter("onlinePlayers.log", false);
                        for (String player : onlinePlayers) {
                            if (!ParseThread.isOnBlacklist(player)) {
                                onlineLog.insert(0, player + "\n");
                                playerCount += 1;
                            }
                        }
                        fw.write(onlineLog.toString());
                        fw.close();

                        Bot.sendMessage(event.getChannel(), "Prioritising " + Bot.emojiGlobe + arg + " with " +
                                playerCount + " players, ignoring " + (onlinePlayers.size() - playerCount) + " blacklisted players\n" +
                                "Ready in ~" + TimeUnit.MILLISECONDS.toSeconds(playerCount * 2400L) + " seconds", true);
                    } else {
                        Bot.sendMessage(event.getChannel(), arg + " is offline", true);
                    }
                } else {
                    StringBuilder onlineLog = new StringBuilder(Bot.readLog("onlinePlayers.log"));
                    FileWriter fw = new FileWriter("onlinePlayers.log", false);
                    onlineLog.insert(0, arg + "\n");
                    fw.write(onlineLog.toString());
                    fw.close();

                    Bot.sendMessage(event.getChannel(), "Force updating " + arg, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Bot.writeFile("commandForceUpdate.log", e.getLocalizedMessage() + "\n");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
