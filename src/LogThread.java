import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class LogThread extends Thread {
    private final String apiEndpoint;
    private final String fileName;
    private final String mode;
    private String player;

    public LogThread(String apiEndpoint, String fileName, String mode) {
        this.apiEndpoint = apiEndpoint;
        this.fileName = fileName;
        this.mode = mode;
    }

    @Override
    public void run() {
        try {
            JSONObject json;
            if (mode.equals("online")) {
                json = Bot.readJsonFromUrl(apiEndpoint);
                FileWriter fw = new FileWriter(fileName);
                fw.write(json.toString() + "\n");
                fw.close();
            } else if (mode.equals("player")) {
                if (Bot.getLogLineCount(fileName) == 0)
                    return;
                String[] playerArr = Bot.readLog(fileName).split("\n");
                String[] blacklistArr = Bot.readLog("blacklist.log").split("\n");
                HashMap<String, Long> blacklist = new HashMap<>();
                for (String s : blacklistArr) {
                    if (!s.equals("") && s.split(",").length > 1)
                        blacklist.put(s.split(",")[0], Long.parseLong(s.split(",")[1]));
                }

                long blacklistTimer = 259200000;
                while (blacklist.containsKey(playerArr[0])) {
                    long l = System.currentTimeMillis() - blacklist.get(playerArr[0]);
                    if (l < blacklistTimer) {
                        System.out.println(playerArr[0] + " cooldown");
                        Bot.removeFirstLine(fileName);
                        playerArr = Bot.readLog(fileName).split("\n");
                    } else {
                        break;
                    }
                }
                player = playerArr[0];
                Bot.removeFirstLine(fileName);

                json = Bot.readJsonFromUrl(String.format(apiEndpoint, playerArr[0]));
                FileWriter fw = new FileWriter("playerStats.log", true);
                fw.write(json.toString() + "\n");
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                ParseThread.addToBlacklist(player);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static int getOnlinePlayerCount() {
        //TODO
        return 0;
    }
}


