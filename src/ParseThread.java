import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ParseThread extends Thread {

    @Override
    public void run() {
        try {
            File file = new File("wc.log");
            Scanner s = new Scanner(file);
            StringBuilder wclog = new StringBuilder();
            while (s.hasNextLine()) {
                wclog.append(s.nextLine());
            }
            s.close();
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

            FileWriter fw = new FileWriter("onlinePlayers.log", true);
            for (String player : onlinePlayers) {
                fw.write(player + "\n");
            }

            fw.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}