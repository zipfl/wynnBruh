import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private Properties properties = new Properties();
    private String prefix = "#";

    public Settings() throws IOException {
        File propFile = new File("settings.properties");
        if (propFile.exists() && !properties.getProperty("prefix").equals(null)) {
            properties.load(new FileInputStream("settings.properties"));
            prefix = properties.getProperty("prefix");
        } else {
            propFile.createNewFile();
        }
        System.out.println("Prefix is '" + prefix + "'");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        properties.setProperty("prefix", prefix);
    }
}
