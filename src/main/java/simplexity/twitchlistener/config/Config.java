package simplexity.twitchlistener.config;

import java.io.*;
import java.util.Properties;

public class Config {
    private static Config instance;
    private Config(){}

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }
    private String channelName;
    private String oAuthCode;

    private final String fileName = "config.properties";
    private final File configFile = new File(fileName);

    public void initializeProperties() {
        if (configFile.exists()) return;
        Properties properties = new Properties();
        properties.setProperty("o-auth-code", "");
        properties.setProperty("channel-name", "");
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
        channelName = properties.getProperty("channel-name");
        oAuthCode = properties.getProperty("o-auth-code");
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public String getOAuthCode() {
        return oAuthCode;
    }
}

