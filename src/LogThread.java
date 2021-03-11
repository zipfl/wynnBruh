import org.json.JSONObject;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class LogThread extends Thread {
    private final String apiEndpoint;
    private final int refreshMillis;
    private final String fileName;
    private final String mode;
    private BufferedReader br;

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
                        br = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = br.readLine()) != null) {
                            Bot.removeFirstLine("onlinePlayers.log");
                            json = Main.bot.readJsonFromUrl(String.format(apiEndpoint, line));
                            new File(fileName);
                            FileWriter fw = new FileWriter(fileName, true);
                            fw.write(json.toString() + "ENDOFPLAYERSTATS\n");
                            fw.close();
                            Thread.sleep(refreshMillis);
                        }
                        br.close();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, 0, 1000);

    }
}

