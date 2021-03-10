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
                            removeFirstLine("onlinePlayers.log");
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

        }, 0, refreshMillis);
        try {
            Thread.sleep(refreshMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void removeFirstLine(String fileName) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        //Initial write position
        long writePosition = raf.getFilePointer();
        raf.readLine();
        // Shift the next lines upwards.
        long readPosition = raf.getFilePointer();

        byte[] buff = new byte[1024];
        int n;
        while (-1 != (n = raf.read(buff))) {
            raf.seek(writePosition);
            raf.write(buff, 0, n);
            readPosition += n;
            writePosition += n;
            raf.seek(readPosition);
        }
        raf.setLength(writePosition);
        raf.close();
    }
}

