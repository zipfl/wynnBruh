import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UptimeThread extends Thread {
    private static final HashMap<String, ServerStatus> uptimeMap = new HashMap<>();

    public UptimeThread() {
        for (int i = 0; i < 80; i++) {
            ServerStatus serverStatus = new ServerStatus(false, 0L);
            uptimeMap.put("WC" + i, serverStatus);
        }
    }

    @Override
    public void run() {
        try {
            getUptimeMap(uptimeMap);
            String apiLog = Bot.readLog("wc.log");
            String[] uptimeLog = Bot.readLog("uptime.log").split("\n");
            ArrayList<String> allServers = new ArrayList<>();
            for (String uptimeLogEntry : uptimeLog) {
                allServers.add(uptimeLogEntry.split(",")[0]);
                for (String server : allServers) {
                    if (apiLog.contains(server)) {
                        if (!server.equals("") && !uptimeMap.get(server).isOnline) {
                            uptimeMap.put(server, new ServerStatus(true, System.currentTimeMillis()));
                        }
                    } else {
                        uptimeMap.put(server, new ServerStatus(false, System.currentTimeMillis()));
                    }
                }
            }
            BufferedWriter bf = new BufferedWriter(new FileWriter("uptime.log"));
            for (Map.Entry<String, ServerStatus> entry : uptimeMap.entrySet()) {
                bf.write(entry.getKey() + "," + entry.getValue().isOnline + "," + entry.getValue().changed);
                bf.newLine();
            }
            bf.flush();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getUptimeMap(HashMap<String, ServerStatus> uptimeMap) throws IOException {
        String[] uptimeArr = Bot.readLog("uptime.log").split("\n");
        if (!uptimeArr[0].equals("")) {
            for (String uptimeEntry : uptimeArr) {
                uptimeMap.put(uptimeEntry.split(",")[0], new ServerStatus(Boolean.parseBoolean(uptimeEntry.split(",")[1]), Long.parseLong(uptimeEntry.split(",")[2])));
            }
        }
    }

    public static long getServerUptime(String server) {
        if (uptimeMap.get(server.toUpperCase(Locale.ROOT)).isOnline)
            return System.currentTimeMillis() - uptimeMap.get(server.toUpperCase(Locale.ROOT)).changed;

        return -1;
    }
}

