package simplexity.streamchatcli.config;

import java.io.*;
import java.util.Properties;

public class Config {
    private static Config instance;
    private Config(){}

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
    private String twitchChannelName;
    private String twitchOAuthCode;
    private String kickChannelName;

    private final String fileName = "config.properties";
    private final File configFile = new File(fileName);

    public void initializeProperties() {
        if (configFile.exists()) return;
        Properties properties = new Properties();
        properties.setProperty("use-twitch", "false");
        properties.setProperty("twitch-o-auth-code", "");
        properties.setProperty("twitch-channel-name", "");
        properties.setProperty("use-kick", "false");
        properties.setProperty("kick-channel-name", "");
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readProperties() {
        Properties properties = new Properties();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        twitchChannelName = properties.getProperty("twitch-channel-name");
        twitchOAuthCode = properties.getProperty("twitch-o-auth-code");
        kickChannelName = properties.getProperty("kick-channel-name");
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTwitchChannelName() {
        return twitchChannelName;
    }

    public String getTwitchOAuthCode() {
        return twitchOAuthCode;
    }
    public String getKickChannelName(){
        return kickChannelName;
    }
}

