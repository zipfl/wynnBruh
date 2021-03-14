import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("BusyWait")
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
                new File(fileName);
                FileWriter fw = new FileWriter(fileName);
                fw.write(json.toString() + "\n");
                fw.close();
            } else if (mode.equals("player")) {
                File file = new File("onlinePlayers.log");
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((player = br.readLine()) != null) {
                    String[] blacklistArr = Bot.readLog("blacklist.log").split("\n");
                    HashMap<String, Long> blacklist = new HashMap<>();
                    for (String s : blacklistArr) {
                        if (!s.equals("") && s.split(",").length > 1)
                            blacklist.put(s.split(",")[0], Long.parseLong(s.split(",")[1]));
                    }
                    Bot.removeFirstLine("onlinePlayers.log");

                    long blacklistTimer = 259200000;
                    if (!blacklist.containsKey(player) || System.currentTimeMillis() - blacklist.get(player) > blacklistTimer) {
                        json = Bot.readJsonFromUrl(String.format(apiEndpoint, player));
                        new File(fileName);
                        FileWriter fw = new FileWriter(fileName, true);
                        fw.write(json.toString() + "\n");
                        fw.close();
                        Thread.sleep(2400);
                    }
                }
                br.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            try {
                ParseThread.addToBlacklist(player);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}


