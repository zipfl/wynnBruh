import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class CommandChest extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(Main.bot.settings.getPrefix()) && msg.getContentRaw().indexOf("c") == Main.bot.settings.getPrefix().length()) {
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

                    message.append(String.format("%1$-20s", Bot.emojiPlayer + "Player")).append(" | ").append(String.format("%1$10s", "Chests" + Bot.emojiChest)).append(" | ").append(Bot.emojiClock).append("Timestamp").append("\n");
                    message.append("-----------------------------------------------\n");
                    ArrayList<String> sortedChestLog = getChestListForServer(server);
                    for (String sortedChestLogEntry : sortedChestLog) {
                        long timestamp = Long.parseLong(sortedChestLogEntry.split(",")[0]);
                        String player = sortedChestLogEntry.split(",")[1];
                        int chestCount = Integer.parseInt(sortedChestLogEntry.split(",")[2]);
                        if(chestCount >= 0)
                            message.append(String.format("%1$-20s", player)).append(" | ").append(String.format("%1$9s", chestCount)).append(Bot.emojiChest).append(" | ").append(Bot.parseTimestampToHoursMinutes(System.currentTimeMillis() - timestamp)).append("\n");

                    }
                } else {
                    HashMap<String, Integer> serverChestMap = new HashMap<>();
                    for (int i = 1; i < 80; i++) {
                        ArrayList<String> sortedChestLog = getChestListForServer("WC" + i);
                        int chestCount = 0;
                        for (String sortedChestLogEntry : sortedChestLog) {
                            chestCount += Integer.parseInt(sortedChestLogEntry.split(",")[2]);
                            serverChestMap.put("WC" + i, chestCount);
                        }
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

            String finalMessage = MarkdownUtil.codeblock(message.toString());
            if (!finalMessage.equals("``````"))
                channel.sendMessage(finalMessage).queue();
            else
                channel.sendMessage("There was an error processing your request").queue();
        }
    }

    @NotNull
    private ArrayList<String> getChestListForServer(String server) throws IOException {
        String[] chestLogArr = Bot.readLog("chests_" + server + ".log").split("\n");
        ArrayList<String> playerList = new ArrayList<>();
        for (String chestLogEntry : chestLogArr) {
            String currentPlayer = chestLogEntry.split(",")[0];
            if (!playerList.contains(currentPlayer)) {
                playerList.add(currentPlayer);
            }
        }

        ArrayList<String> sortedChestLog = new ArrayList<>();
        for (String player : playerList) {
            ArrayList<String> currentPlayerChestLog = new ArrayList<>();
            for (String chestLogEntry : chestLogArr) {
                if (chestLogEntry.split(",")[0].equals(player)) {
                    currentPlayerChestLog.add(chestLogEntry);
                }
            }
            if (currentPlayerChestLog.size() > 1) {
                int chestCount = 0;
                long timestampAgo = 0;
                for (int i = 0; i < currentPlayerChestLog.size() - 1; i++) {
                    long timestamp = Long.parseLong(currentPlayerChestLog.get(i).split(",")[2]);
                    long nextTimestamp = Long.parseLong(currentPlayerChestLog.get(i + 1).split(",")[2]);
                    int chests = Integer.parseInt(currentPlayerChestLog.get(i).split(",")[1]);
                    int nextChests = Integer.parseInt(currentPlayerChestLog.get(i + 1).split(",")[1]);
                    if (timestamp < nextTimestamp && chests != nextChests && System.currentTimeMillis() - timestamp < UptimeThread.getServerUptime(server)) {
                        chestCount += nextChests - chests;
                        timestampAgo = nextTimestamp;
                    }
                    if (i == currentPlayerChestLog.size() - 2 && chestCount != 0) {
                        sortedChestLog.add(timestampAgo + "," + player + "," + chestCount);
                    }
                }
            }
        }
        sortedChestLog.sort(Collections.reverseOrder());
        return sortedChestLog;
    }
}
