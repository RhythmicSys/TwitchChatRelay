package simplexity.twitchlistener;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import simplexity.twitchlistener.config.Config;

public class Main {
    private static OAuth2Credential credential = null;
    private static TwitchClient twitchClient;

    private static void setUpListenerBot() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnableHelix(true)
                .withChatAccount(credential)
                .build();
    }
    public static void main(String[] args) {
        setUpConfig();
        if (Config.getInstance().getChannelName().isEmpty() || Config.getInstance().getOAuthCode().isEmpty()) {
            System.out.println("PLEASE CONFIGURE YOUR CHANNEL NAME AND ACCESS TOKEN");
            return;
        }
        credential = new OAuth2Credential("twitch", Config.getInstance().getOAuthCode());
        setUpListenerBot();
        twitchClient.getChat().joinChannel(Config.getInstance().getChannelName());
        MessageStuff.getInstance().onMessageEvent();
    }

    private static void setUpConfig() {
        Config.getInstance().initializeProperties();
        Config.getInstance().readProperties();
    }

    public static TwitchClient getTwitchClient() {
        return twitchClient;
    }

}