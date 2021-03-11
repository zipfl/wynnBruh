import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
            } else if (mode.equals("chests")) {
                StringBuilder chestLog = Bot.readLog(filename);
                JSONObject json;

                String[] allPlayerStats = chestLog.toString().split("ENDOFPLAYERSTATS");
                for (String playerStat : allPlayerStats) {
                    Bot.removeFirstLine("playerStats.log");
                    json = new JSONObject(playerStat);

                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (key.equals("data")) {
                            if (json.get(key) instanceof JSONArray) {
                                String username = json.getJSONArray("data").getJSONObject(0).getString("username");
                                String server = null;
                                if (json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getBoolean("online")) {
                                    server = json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getString("server");
                                }
                                int chests = json.getJSONArray("data").getJSONObject(0).getJSONObject("global").getInt("chestsFound");

                                FileWriter fw = new FileWriter("chests.log", true);
                                if (server != null)
                                    fw.write(username + "," + server + "," + chests + "," + new Date() + "\n");
                                fw.close();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}