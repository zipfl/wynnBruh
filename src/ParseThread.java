import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class ParseThread extends Thread {

    @Override
    public void run() {
        try {
            File file = new File("wc.log");
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
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}