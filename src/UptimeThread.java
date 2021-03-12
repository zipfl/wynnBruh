import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UptimeThread extends Thread {
    private final HashMap<String, ServerStatus> uptimeMap = new HashMap<>();

    public UptimeThread() {
        for (int i = 0; i < 100; i++) {
            ServerStatus serverStatus = new ServerStatus(false, 0L);
            uptimeMap.put("WC" + i, serverStatus);
        }
    }

    @Override
    public void run() {
        try {
            String[] uptimeArr = Bot.readLog("uptime.log").toString().split("\n");
            if (!uptimeArr[0].equals("")) {
                for (String uptimeEntry : uptimeArr) {
                    boolean aBoolean = Boolean.parseBoolean(uptimeEntry.split(",")[1]);
                    uptimeMap.put(uptimeEntry.split(",")[0], new ServerStatus(aBoolean, Long.parseLong(uptimeEntry.split(",")[2])));
                }
            }
            String wcLog = Bot.readLog("wc.log").toString();
            for (int i = 1; i < 80; i++) {
                if (wcLog.contains("WC" + i)) {
                    if (!uptimeMap.get("WC" + i).isOnline) {
                        uptimeMap.put("WC" + i, new ServerStatus(true, System.currentTimeMillis()));
                    }
                } else {
                    uptimeMap.put("WC" + i, new ServerStatus(false, System.currentTimeMillis()));
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
}

class ServerStatus {
    public boolean isOnline;
    public long changed;

    public ServerStatus(boolean isOnline, long changed) {
        this.isOnline = isOnline;
        this.changed = changed;
    }
}
