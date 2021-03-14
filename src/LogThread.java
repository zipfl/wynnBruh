import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("BusyWait")
public class LogThread extends Thread {
    private final String apiEndpoint;
    private final String fileName;
    private final String mode;
    String player;

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
                    if (!blacklist.containsKey(player) || System.currentTimeMillis() - blacklist.get(player) > 604800000) {
                        json = Bot.readJsonFromUrl(String.format(apiEndpoint, player));
                        new File(fileName);
                        FileWriter fw = new FileWriter(fileName, true);
                        fw.write(json.toString() + "\n");
                        fw.close();
                        Thread.sleep(2400);
                    } else {
                        System.out.println("[BLK] " + player + " already blacklisted.." + TimeUnit.MILLISECONDS.toHours(Math.abs(System.currentTimeMillis() - blacklist.get(player) - 604800000)) + "hours remaining..");
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


