import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LogThread extends Thread {
    private final String apiEndpoint;
    private final int refreshMillis;
    private final String fileName;
    private final String mode;
    private BufferedReader br;
    String player;

    public LogThread(String apiEndpoint, int refreshMillis, String fileName, String mode) {
        this.apiEndpoint = apiEndpoint;
        this.refreshMillis = refreshMillis;
        this.fileName = fileName;
        this.mode = mode;
    }

    @Override
    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    JSONObject json;
                    if (mode.equals("wc")) {
                        json = Bot.readJsonFromUrl(apiEndpoint);
                        new File(fileName);
                        FileWriter fw = new FileWriter(fileName);
                        fw.write(json.toString() + "\n");
                        fw.close();
                    } else if (mode.equals("player")) {
                        File file = new File("onlinePlayers.log");
                        br = new BufferedReader(new FileReader(file));
                        while ((player = br.readLine()) != null) {
                            String[] blacklistArr = Bot.readLog("blacklist.log").toString().split("\n");
                            HashMap<String, Long> blacklist = new HashMap<>();
                            for (String s: blacklistArr) {
                                if (!s.equals(""))
                                    blacklist.put(s.split(",")[0],Long.parseLong(s.split(",")[1]));
                            }
                            Bot.removeFirstLine("onlinePlayers.log");
                            if(!blacklist.containsKey(player) || System.currentTimeMillis() - blacklist.get(player) > 604800000) {
                                json = Bot.readJsonFromUrl(String.format(apiEndpoint, player));
                                new File(fileName);
                                FileWriter fw = new FileWriter(fileName, true);
                                fw.write(json.toString() + "\n");
                                fw.close();
                                Thread.sleep(refreshMillis);
                            } else {
                                System.out.println(player + " is blacklisted.." + TimeUnit.MILLISECONDS.toHours(Math.abs(System.currentTimeMillis() - blacklist.get(player) - 604800000)) + "hours remaining..skipping");
                            }
                        }
                        br.close();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, refreshMillis);
    }
}

