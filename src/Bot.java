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
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Bot {
    public Settings settings = new Settings();

    public static String emojiGlobe = "\uD83C\uDF0E";
    public static String emojiStar = "⭐";
    public static String emojiChest = "\uD83D\uDCBC";

    public Bot(String token) throws LoginException, IOException {
        JDA jda = JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .build();
        jda.addEventListener(new CommandWc());
        jda.addEventListener(new CommandUptime());
        jda.addEventListener(new CommandChest());
        jda.addEventListener(new CommandPrefix());

        LogThread onlineLogThread = new LogThread("https://api.wynncraft.com/public_api.php?action=onlinePlayers", "wc.log", "online");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                onlineLogThread.run();
            }
        }, 1000, 2000);

        UptimeThread uptimeThread = new UptimeThread();
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                uptimeThread.run();
            }
        }, 2000, 2000);

        ParseThread onlineParseThread = new ParseThread("wc.log", "online");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                onlineParseThread.run();
            }
        }, 2000, 5000);

        LogThread playerStatsLogThread = new LogThread("https://api.wynncraft.com/v2/player/%s/stats","playerStats.log", "player");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                playerStatsLogThread.run();
            }
        }, 3000, 2400);

        ParseThread playerStatsParseThread = new ParseThread("playerStats.log", "player");
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                playerStatsParseThread.run();
            }
        }, 4000, 2400);

        new Timer().schedule(new TimerTask() {
            public void run() {
                try {
                    jda.getPresence().setActivity(Activity.watching(getLogLineCount("chests.log") + " chest stats"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3000, 5000);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());
        list.sort(Map.Entry.comparingByValue());
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    private static final Pattern patternNumber = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return patternNumber.matcher(strNum).matches();
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
    public static String readLog(String filename) throws IOException {
        File file = new File(filename);
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder string = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            string.append(line).append("\n");
        }
        br.close();
        return string.toString();
    }

    public static int getLogLineCount(String logFile) throws IOException {
        File file = new File(logFile);
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        BufferedReader reader = new BufferedReader(new FileReader(logFile));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    public static String parseTimestampToHoursMinutes(long timestamp) {
        long hours = TimeUnit.MILLISECONDS.toMinutes(timestamp) / 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timestamp) % 60;
        return hours + "h " + minutes + "m";
    }
}
