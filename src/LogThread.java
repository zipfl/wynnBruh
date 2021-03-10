import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LogThread extends Thread {
    private final String apiEndpoint;
    private final int refreshSeconds;

    public LogThread(String apiEndpoint, int refreshSeconds) {
        this.apiEndpoint = apiEndpoint;
        this.refreshSeconds = refreshSeconds * 1000;
    }

    @Override
    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    JSONObject json = Main.bot.readJsonFromUrl(apiEndpoint);

                    File file = new File("wc.log");
                    FileWriter fw = new FileWriter("wc.log");
                    fw.write(json.toString()+"\n");
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }, 0, refreshSeconds);
        try {
            Thread.sleep(refreshSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

