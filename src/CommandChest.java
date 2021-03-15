import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

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

                    message.append(Bot.emojiChest).append(" ").append(server).append(" chest log\n\n");

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
                    for (String sortedChestLogEntry : sortedChestLog) {
                        long timestamp = Long.parseLong(sortedChestLogEntry.split(",")[0]);
                        String player = sortedChestLogEntry.split(",")[1];
                        int chestCount = Integer.parseInt(sortedChestLogEntry.split(",")[2]);
                        message.append(player).append(": ").append(chestCount).append(Bot.emojiChest).append(" ").append(Bot.parseTimestampToHoursMinutes(System.currentTimeMillis() - timestamp)).append(" ago\n");

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
}
