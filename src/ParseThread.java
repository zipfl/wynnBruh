import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

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
                File file = new File(filename);
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder wclog = new StringBuilder();
                String line;
                if ((line = br.readLine()) != null) {
                    wclog.append(line);
                }
                br.close();
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
                File file = new File(filename);
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder chestLog = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    chestLog.append(line);
                }
                br.close();
                JSONObject json;

                String[] allPlayerStats = chestLog.toString().split("ENDOFPLAYERSTATS");
                for (String allPlayerStat : allPlayerStats) {
                    json = new JSONObject(allPlayerStat);

                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (key.equals("data")) {
                            if (json.get(key) instanceof JSONArray) {
                                String username = json.getJSONArray("data").getJSONObject(0).getString("username");
                                int chests = json.getJSONArray("data").getJSONObject(0).getJSONObject("global").getInt("chestsFound");
                                String server = null;
                                if (json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getBoolean("online")) {
                                    server = json.getJSONArray("data").getJSONObject(0).getJSONObject("meta").getJSONObject("location").getString("server");
                                }
                                FileWriter fw = new FileWriter("chests.log", true);
                                if (username != null && server != null)
                                    fw.write(username + "," + server + "," + chests + "," + new Date() + "\n");
                                fw.close();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public int getLogLineCount(String logFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(logFile));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }
}