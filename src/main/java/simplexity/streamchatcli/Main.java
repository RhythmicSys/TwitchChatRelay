package simplexity.streamchatcli;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import simplexity.streamchatcli.config.Config;
import simplexity.streamchatcli.kick.MessageListener;
import simplexity.streamchatcli.twitch.MessageStuff;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    private static OAuth2Credential credential = null;
    private static TwitchClient twitchClient;
    private static URL kickURL;
    private static URL kickChatroomURL;

    private static void setUpListenerBot() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnableHelix(true)
                .withChatAccount(credential)
                .build();
    }

    private static final OkHttpClient kickHttpClient = new OkHttpClient.Builder().eventListener(new MessageListener()).build();

    public static void main(String[] args) throws IOException {
        setUpConfig();
        if (Config.getInstance().getTwitchChannelName().isEmpty() || Config.getInstance().getTwitchOAuthCode().isEmpty()) {
            System.out.println("PLEASE CONFIGURE YOUR CHANNEL NAME AND ACCESS TOKEN");
            return;
        }
        credential = new OAuth2Credential("twitch", Config.getInstance().getTwitchOAuthCode());
        setUpListenerBot();
        twitchClient.getChat().joinChannel(Config.getInstance().getTwitchChannelName());
        if (Config.getInstance().getKickChannelName() != null && !Config.getInstance().getKickChannelName().isEmpty()) {
            kickURL = new URL("https://kick.com/api/v2/channels/" + Config.getInstance().getKickChannelName() + "/");
            kickChatroomURL = new URL("https://kick.com/api/v1/channels/" + Config.getInstance().getKickChannelName() + "/");
            Request request = new Request.Builder().url(kickChatroomURL).build();
            kickHttpClient.newCall(request).execute();
        }
        MessageStuff.getInstance().onMessageEvent();
    }

    private static void setUpConfig() {
        Config.getInstance().initializeProperties();
        Config.getInstance().readProperties();
    }

    public static TwitchClient getTwitchClient() {
        return twitchClient;
    }
    public static URL getKickURL() {
        if (Config.getInstance().getKickChannelName() == null || Config.getInstance().getKickChannelName().isEmpty()) {
            return null;
        }
        return kickURL;
    }

    public static URL getKickChatroomURL() {
        if (Config.getInstance().getKickChannelName().isEmpty()) {
            return null;
        }
        return kickChatroomURL;
    }

}