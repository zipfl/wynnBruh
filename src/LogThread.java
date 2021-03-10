import org.json.JSONObject;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class LogThread extends Thread {
    private final String apiEndpoint;
    private final int refreshMillis;
    private final String fileName;
    private final String mode;

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
                        json = Main.bot.readJsonFromUrl(apiEndpoint);
                        new File(fileName);
                        FileWriter fw = new FileWriter(fileName);
                        fw.write(json.toString() + "\n");
                        fw.close();
                    } else {
                        File file = new File("onlinePlayers.log");
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        if ((line = br.readLine()) != null) {
                            json = Main.bot.readJsonFromUrl(String.format(apiEndpoint, line));
                            new File(fileName);
                            FileWriter fw = new FileWriter(fileName, true);
                            fw.write(json.toString() + "\n");
                            fw.close();
                        }
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }, 0, refreshMillis);
        try {
            Thread.sleep(refreshMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

