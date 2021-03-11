import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class Bot {
    public Settings settings = new Settings();

    public Bot(String token) throws LoginException {
        JDA jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .build();
        jda.addEventListener(new CommandWc());
        jda.addEventListener(new CommandPrefix());

        LogThread wcLogThread = new LogThread("https://api.wynncraft.com/public_api.php?action=onlinePlayers", 60000, "wc.log", "wc");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                wcLogThread.start();
            }
        }, 500);

        ParseThread playerParseThread = new ParseThread("wc.log", "player");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                playerParseThread.start();
            }
        }, 1000);

        LogThread playerStatsLogThread = new LogThread("https://api.wynncraft.com/v2/player/%s/stats", 2500, "playerStats.log", "player");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                playerStatsLogThread.start();
            }
        }, 2000);


        ParseThread chestsParseThread = new ParseThread("playerStats.log", "chests");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                chestsParseThread.run();
            }
        }, 3000, 5000);

        new Timer().schedule(new TimerTask() {
            public void run() {
                try {
                    jda.getPresence().setActivity(Activity.watching(getLogLineCount("playerStats.log") + " player stats"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 5000);
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
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

    @NotNull
    public static StringBuilder readLog(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder string = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            string.append(line);
        }
        br.close();
        return string;
    }

    public static int getLogLineCount(String logFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(logFile));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }
}
