import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("BusyWait")
public class ParseThread extends Thread {
    private final String mode;
    private final String filename;

    public ParseThread(String filename, String mode) {
        this.filename = filename;
        this.mode = mode;
    }

    @Override
    public void run() {
        try {
            if (mode.equals("player")) {
                if (Bot.getLogLineCount("onlinePlayers.log") == 0) {
                    StringBuilder wclog = Bot.readLog(filename);
                    JSONObject json;
                    json = new JSONObject(wclog.toString());
                    ArrayList<String> onlinePlayers = new ArrayList<>();
                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (key.equals("request"))
                            continue;

                        if (json.get(key) instanceof JSONArray) {
                            for (int i = 0; i < ((JSONArray) json.get(key)).length(); i++) {
                                onlinePlayers.add(((JSONArray) json.get(key)).getString(i));
                            }
                        }
                    }

                    FileWriter fw = new FileWriter("onlinePlayers.log");
                    for (String player : onlinePlayers) {
                        fw.write(player + "\n");
                    }
                    fw.close();
                }
            } else if (mode.equals("chests")) {
                StringBuilder chestLog = Bot.readLog(filename);
                while (chestLog.length() == 0) {
                    Thread.sleep(1000);
                    chestLog = Bot.readLog(filename);
                    if (chestLog.length() != 0) {
                        break;
                    }
                }
                JSONObject json;
                long timestamp;
                String[] allPlayerStats = chestLog.toString().split("\n");
                for (String playerStat : allPlayerStats) {
                    Bot.removeFirstLine("playerStats.log");
                    json = new JSONObject(playerStat);
                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        timestamp = json.getLong("timestamp");
                        if (key.equals("data")) {
                            if (json.get(key) instanceof JSONArray) {
                                String username = json.getJSONArray("data").getJSONObject(0).getString("username");
                                String server = null;
                                if (json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getBoolean("online")) {
                                    server = json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getString("server");
                                }

                                int chests = json.getJSONArray("data").getJSONObject(0).getJSONObject("global").getInt("chestsFound");
                                if (chests < 1500) {
                                    String[] blacklistArr = Bot.readLog("blacklist.log").toString().split("\n");
                                    HashMap<String, Long> blacklist = new HashMap<>();
                                    if (!blacklistArr[0].equals("")) {
                                        for (String blacklistEntry : blacklistArr) {
                                            blacklist.put(blacklistEntry.split(",")[0], Long.parseLong(blacklistEntry.split(",")[1]));
                                        }
                                    }
                                    blacklist.put(username, System.currentTimeMillis());
                                    System.out.println(username + " added to blacklist");
                                    BufferedWriter bf = new BufferedWriter(new FileWriter("blacklist.log"));
                                    for (Map.Entry<String, Long> entry : blacklist.entrySet()) {
                                        bf.write(entry.getKey() + "," + entry.getValue());
                                        bf.newLine();
                                    }
                                    bf.flush();
                                    bf.close();
                                } else {
                                    BufferedWriter bf;
                                    if (server != null && !server.equals("null")) {
                                        bf = new BufferedWriter(new FileWriter("chests_" + server + ".log", true));
                                        bf.write(username + "," + chests + "," + timestamp);
                                        System.out.println(server + ": " + username + "," + chests + "," + timestamp);
                                        bf.newLine();
                                        bf.flush();
                                        bf.close();
                                    } else {
                                        System.out.println(username + " is already offline.. skipping");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}