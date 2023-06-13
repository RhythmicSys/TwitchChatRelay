package simplexity.streamchatcli;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.sun.net.httpserver.HttpServer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import simplexity.streamchatcli.config.Config;
import simplexity.streamchatcli.kick.MessageListener;
import simplexity.streamchatcli.twitch.MessageStuff;
import simplexity.streamchatcli.util.TwitchStrings;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    private static OAuth2Credential credential = null;
    private static TwitchClient twitchClient;
    private static URL kickURL;
    private static URL kickChatroomURL;
    private static final Set<String> authTypes = Set.of("READ-ONLY", "READ-SEND", "MODERATOR");

    private static void setUpListenerBot() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnableHelix(true)
                .withChatAccount(credential)
                .build();
    }

    private static final OkHttpClient kickHttpClient = new OkHttpClient.Builder().eventListener(new MessageListener()).build();

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        setUpConfig();
        if (!Config.getInstance().isUseKick() && !Config.getInstance().isUseTwitch()) {
            System.out.println("Config has been initialized, please configure settings");
            return;
        }
        if (Config.getInstance().isUseTwitch()) {
            setupTwitchChat();
        }
        if (Config.getInstance().isUseKick()) {
            setupKickChat();
        }
        MessageStuff.getInstance().onMessageEvent();
    }
    private static void setupTwitchChat() throws IOException, InterruptedException {
        if (Config.getInstance().getTwitchChannelName() == null || Config.getInstance().getTwitchChannelName().isEmpty()) {
            System.out.println("You have enabled twitch chat, but have not designated a channel to read from. Please designate a twitch channel");
            return;
        }
        if (Config.getInstance().getTwitchOAuthCode() == null || Config.getInstance().getTwitchOAuthCode().isEmpty()) {
            setupAuth();
        }
        credential = new OAuth2Credential("twitch", Config.getInstance().getTwitchOAuthCode());
        setUpListenerBot();
        twitchClient.getChat().joinChannel(Config.getInstance().getTwitchChannelName());

    }

    private static void setupAuth() throws IOException, InterruptedException {
        System.out.println("This application does not yet have authorization to interact with twitch");
        System.out.println("Note, if your account does not have the permissions in the channel you are trying to interact with, this application will also not have those permissions");
        System.out.println("What level of permission would you like to give this application? Valid responses are: READ-ONLY, READ-SEND, and MODERATOR");
        Scanner oAuthScanner = new Scanner(System.in);
        String requestedLevel;
        requestedLevel = oAuthScanner.nextLine().toUpperCase();
        while (!authTypes.contains(requestedLevel)) {
            System.out.println("Your response did not match any of the options, please be sure you have spelled the option correctly.");
            System.out.println("What level of permission would you like to give this application? Valid responses are: READ-ONLY, READ-SEND, and MODERATOR");
            requestedLevel = oAuthScanner.nextLine().toUpperCase();
        }
        String authAddressString = TwitchStrings.getInstance().getLinkMinusScopes();
        if (requestedLevel.equalsIgnoreCase("READ-ONLY")) {
            authAddressString = authAddressString + TwitchStrings.getInstance().getUserReadOnlyScope();
            //logic
        }
        if (requestedLevel.equalsIgnoreCase("READ-SEND")) {
            authAddressString = authAddressString + TwitchStrings.getInstance().getUserReadSendScope();
            //logic
        }
        if (requestedLevel.equalsIgnoreCase("MODERATOR")) {
            authAddressString = authAddressString + TwitchStrings.getInstance().getUserModerateScope();
            //logic
        }
        System.out.println("Please visit this link to give this application the authorization to interact with Twitch");
        System.out.println(authAddressString);
        setupServer();
    }

    private static void setupServer() throws IOException, InterruptedException {
        HttpServer httpServer = HttpServer.create();
        httpServer.createContext(TwitchStrings.getInstance().getRedirectURI());
        httpServer.start();
        listenForTwitchAuthResponse();
    }

    private static void listenForTwitchAuthResponse() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> stringBodyHandler = HttpResponse.BodyHandlers.ofString();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(TwitchStrings.getInstance().getRedirectURI())).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest, stringBodyHandler);
        System.out.println(response.body());
        System.out.println(response.headers());



    }
    private static void setupKickChat() throws IOException {
        if (Config.getInstance().getKickChannelName() == null || Config.getInstance().getKickChannelName().isEmpty()) {
            System.out.println("You have enabled kick chat, but have not designated a channel to read from. Please designate a kick channel");
            return;
        }
        kickURL = new URL("https://kick.com/api/v2/channels/" + Config.getInstance().getKickChannelName() + "/");
        kickChatroomURL = new URL("https://kick.com/api/v1/channels/" + Config.getInstance().getKickChannelName() + "/");
        Request request = new Request.Builder().url(kickChatroomURL).build();
        kickHttpClient.newCall(request).execute();
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