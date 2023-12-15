package simplexity.streamchatcli.config;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;


public class ChatConfig {
    private static ChatConfig instance;
    private ChatConfig(){}

    public static ChatConfig getInstance() {
        if (instance == null) instance = new ChatConfig();
        return instance;
    }
    private boolean useTwitch;
    private String twitchChannelName;
    private String twitchOAuthCode;
    private Config config;


    public void loadConfig() {
        Config config = ConfigFactory.load();
        this.config = config;
        useTwitch = config.getBoolean("use-twitch");
        twitchChannelName = config.getString("twitch-channel-name");
        twitchOAuthCode = config.getString("twitch-o-auth-code");
    }

    public boolean isUseTwitch() {
        return useTwitch;
    }
    public Config getConfig() {
        return config;
    }
    
    public void setTwitchCode(String code) {
        Config config = ConfigFactory.load();
        ConfigValue twitchCode = ConfigValueFactory.fromAnyRef(code);
        config = config.withValue("twitch-o-auth-code", twitchCode);
    }

    public String getTwitchChannelName() {
        return twitchChannelName;
    }

    public String getTwitchOAuthCode() {
        return twitchOAuthCode;
    }
}

