import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class CommandChest extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(Main.bot.settings.getPrefix(event.getGuild().toString())) && msg.getContentRaw().indexOf("c") == Main.bot.settings.getPrefix(event.getGuild().toString()).length()) {
            MessageChannel channel = event.getChannel();
            StringBuilder message = new StringBuilder();
            String server = null;
            if (msg.getContentRaw().trim().contains(" ")) {
                server = msg.getContentRaw().trim().split(" ")[1];
            }

            try {
                if (server != null) {
                    if (Bot.isNumeric(server))
                        server = "WC" + server;
                    server = server.toUpperCase(Locale.ROOT);
                    message.append(Bot.emojiGlobe).append(server).append(" ").append(Bot.emojiClock).append(Bot.parseTimestampToHoursMinutes(UptimeThread.getServerUptime(server))).append("\n\n");
                    message.append(String.format("%1$-20s", Bot.emojiPlayer + "Player")).append(" | ").append(String.format("%1$10s", "Chests" + Bot.emojiChest)).append(" | ").append(Bot.emojiClock).append(" Timestamp").append("\n");
                    message.append("-----------------------------------------------------\n");
                    ArrayList<String> sortedChestLog = getChestList(server);
                    for (String sortedChestLogEntry : sortedChestLog) {
                        long nextTimestamp = Long.parseLong(sortedChestLogEntry.split(",")[0]);
                        String player = sortedChestLogEntry.split(",")[1];
                        int chestCount = Integer.parseInt(sortedChestLogEntry.split(",")[2]);
                        long timestamp = Long.parseLong(sortedChestLogEntry.split(",")[4]);
                        if (chestCount >= 0)
                            message.append(String.format("%1$-20s", player)).append(" | ").append(String.format("%1$9s", chestCount))
                                    .append(Bot.emojiChest).append(" | ").append(Bot.parseTimestampToHoursMinutes(System.currentTimeMillis() - nextTimestamp))
                                    .append(" - ").append(Bot.parseTimestampToHoursMinutes(System.currentTimeMillis() - timestamp)).append("\n");

                    }
                } else {
                    HashMap<String, Integer> serverChestMap = new HashMap<>();

                    ArrayList<String> sortedChestLog = getChestList(null);
                    for (String sortedChestLogEntry : sortedChestLog) {
                        int chestCount = Integer.parseInt(sortedChestLogEntry.split(",")[0]);
                        serverChestMap.put(sortedChestLogEntry.split(",")[1], chestCount);
                    }

                    serverChestMap = Bot.sortByIntValue(serverChestMap);
                    message.append(String.format("%1$-10s", Bot.emojiGlobe + "Server")).append(" | ").append(String.format("%1$9s", "Chests" + Bot.emojiChest)).append(" | ").append(Bot.emojiClock).append("Uptime").append("\n");
                    message.append("-----------------------------------\n");
                    for (Map.Entry<String, Integer> en : serverChestMap.entrySet()) {
                        if (en.getValue() >= 0)
                            message.append(String.format("%1$-10s", Bot.emojiGlobe + en.getKey())).append(" | ").append(String.format("%1$9s", en.getValue() + Bot.emojiChest)).append(" | ").append(Bot.parseTimestampToHoursMinutes(UptimeThread.getServerUptime(en.getKey()))).append("\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bot.sendMessage(channel, message.toString());
        }
    }

    @NotNull
    private ArrayList<String> getChestList(String server) throws IOException {
        String[] chestLogArr = Bot.readLog("chests.log").split("\n");
        ArrayList<String> playerList = new ArrayList<>();
        for (String chestLogEntry : chestLogArr) {
            String currentPlayer = chestLogEntry.split(",")[0];
            if (!playerList.contains(currentPlayer)) {
                playerList.add(currentPlayer);
            }
        }
        HashMap<String, Integer> serverChestMap = new HashMap<>();
        ArrayList<String> sortedChestLog = new ArrayList<>();
        for (String player : playerList) {
            ArrayList<String> currentPlayerChestLog = new ArrayList<>();
            for (String chestLogEntry : chestLogArr) {
                if (chestLogEntry.split(",")[0].equals(player)) {
                    currentPlayerChestLog.add(chestLogEntry);
                }
            }

            int chestCount = 0;
            for (int i = 0; i < currentPlayerChestLog.size() - 1; i++) {
                long timestamp = Long.parseLong(currentPlayerChestLog.get(i).split(",")[2]);
                long nextTimestamp = Long.parseLong(currentPlayerChestLog.get(i + 1).split(",")[2]);
                int chests = Integer.parseInt(currentPlayerChestLog.get(i).split(",")[1]);
                int nextChests = Integer.parseInt(currentPlayerChestLog.get(i + 1).split(",")[1]);
                if (server == null) {
                    server = currentPlayerChestLog.get(i + 1).split(",")[3];
                    if (timestamp < nextTimestamp && chests != nextChests && System.currentTimeMillis() - nextTimestamp < 7200000) {
                        chestCount += nextChests - chests;
                    }
                    if (chestCount != 0) {
                        if (serverChestMap.containsKey(server))
                            serverChestMap.put(server, chestCount + serverChestMap.get(server));
                        else
                            serverChestMap.put(server, chestCount);
                    }
                    server = null;
                    chestCount = 0;
                } else {
                    String nextServer = currentPlayerChestLog.get(i + 1).split(",")[3];
                    if (nextServer.equals(server) && timestamp < nextTimestamp && chests != nextChests && System.currentTimeMillis() - nextTimestamp < 7200000) {
                        chestCount = nextChests - chests;
                    }
                    if (chestCount != 0) {
                        sortedChestLog.add(nextTimestamp + "," + player + "," + chestCount + "," + server + "," + timestamp);
                    }
                }
            }
        }
        if (server == null) {
            for (Map.Entry<String, Integer> entry : serverChestMap.entrySet()) {
                sortedChestLog.add(entry.getValue() + "," + entry.getKey());
            }
        }
        sortedChestLog.sort(Collections.reverseOrder());
        return sortedChestLog;
    }
}
