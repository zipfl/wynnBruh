import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParseThread extends Thread {
    private final String mode;
    private final String inputFile;

    public ParseThread(String inputFile, String mode) {
        this.inputFile = inputFile;
        this.mode = mode;
    }

    @Override
    public void run() {
        try {
            if (mode.equals("online")) {
                if (Bot.getLogLineCount(inputFile) == 0 || Bot.getLogLineCount("onlinePlayers.log") != 0)
                    return;
                JSONObject json = new JSONObject(Bot.readLog(inputFile));
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

            } else if (mode.equals("player")) {
                if (Bot.getLogLineCount(inputFile) == 0)
                    return;
                String playerStats = Bot.readLog(inputFile);
                String[] allPlayerStats = playerStats.split("\n");
                for (String playerStat : allPlayerStats) {
                    Bot.removeFirstLine(inputFile);
                    JSONObject json = new JSONObject(playerStat);
                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        long timestamp = json.getLong("timestamp");
                        if (key.equals("data")) {
                            String username = json.getJSONArray("data").getJSONObject(0).getString("username");
                            String server = null;
                            if (json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getBoolean("online")) {
                                server = json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getString("server");
                            }
                            int chests = json.getJSONArray("data").getJSONObject(0).getJSONObject("global").getInt("chestsFound");

                            if (getHighestCombatLevel(json) < 100 || !hasQuest(json, "One Thousand Meters Under") || chests < 1500) {
                                addToBlacklist(username);
                            } else {
                                if (server != null && !server.equals("null")) {
                                    BufferedWriter bf = new BufferedWriter(new FileWriter("chests.log", true));
                                    bf.write(username + "," + chests + "," + timestamp + "," + server);
                                    System.out.println("[CST] " + server + ": " + username + "," + chests + "," + timestamp);
                                    bf.newLine();
                                    bf.flush();
                                    bf.close();
                                } else {
                                    System.out.println("[OFF] " + username + " is already offline..");
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Bot.writeFile("parseThread.log", e.getLocalizedMessage() + "\n");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static synchronized void addToBlacklist(String username) throws IOException {
        String[] blacklistArr = Bot.readLog("blacklist.log").split("\n");
        HashMap<String, Long> blacklist = new HashMap<>();
        if (!blacklistArr[0].equals("")) {
            for (String blacklistEntry : blacklistArr) {
                if (blacklistEntry.split(",").length > 1)
                    blacklist.put(blacklistEntry.split(",")[0], Long.parseLong(blacklistEntry.split(",")[1]));
            }
        }
        blacklist.put(username, System.currentTimeMillis());
        System.out.println("[BLK] " + username + " added to blacklist");
        BufferedWriter bf = new BufferedWriter(new FileWriter("blacklist.log"));
        for (Map.Entry<String, Long> entry : blacklist.entrySet()) {
            bf.write(entry.getKey() + "," + entry.getValue());
            bf.newLine();
        }
        bf.flush();
        bf.close();
    }

    public static boolean isOnBlacklist(String player) throws IOException {
        String[] blacklistArr = Bot.readLog("blacklist.log").split("\n");
        if (!blacklistArr[0].equals("")) {
            for (String blacklistEntry : blacklistArr) {
                if (blacklistEntry.split(",")[0].equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasQuest(JSONObject playerJSON, String questName) {
        Iterator<String> keys = playerJSON.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("data")) {
                JSONArray classes = playerJSON.getJSONArray("data").getJSONObject(0).getJSONArray("classes");
                for (int i = 0; i < classes.length(); i++) {
                    JSONArray completedQuests = classes.getJSONObject(i).getJSONObject("quests").getJSONArray("list");
                    for (int j = 0; j < completedQuests.length(); j++) {
                        String name = (String) completedQuests.get(j);
                        if (name.equals(questName))
                            return true;
                    }
                }

            }
        }
        return false;
    }

    public static int getHighestCombatLevel(JSONObject playerJSON) throws IOException {
        Iterator<String> keys = playerJSON.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("data")) {
                JSONArray classes = playerJSON.getJSONArray("data").getJSONObject(0).getJSONArray("classes");
                int highestLevel = 0;
                for (int i = 0; i < classes.length(); i++) {
                    try {
                        int level = classes.getJSONObject(i).getJSONObject("professions").getJSONObject("combat").getInt("level");
                        if (level > highestLevel) {
                            highestLevel = level;
                        }
                    } catch (JSONException ignored) {
                    }

                }
                return highestLevel;
            }
        }
        return -1;
    }
}