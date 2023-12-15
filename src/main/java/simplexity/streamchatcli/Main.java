package simplexity.streamchatcli;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import simplexity.streamchatcli.config.ChatConfig;
import simplexity.streamchatcli.twitch.MessageStuff;
import simplexity.streamchatcli.twitch.TwitchAuthServer;
import simplexity.streamchatcli.twitch.TwitchUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class Main {
    
    private static OAuth2Credential twitchCredential = null;
    private static TwitchClient twitchClient;
    private static final Set<String> authTypes = Set.of("1", "2", "3");
    private static final Scanner scanner = new Scanner(System.in);
    
    private static void setUpListenerBot() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnableHelix(true)
                .withChatAccount(twitchCredential)
                .build();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        setUpConfig();
        if (!ChatConfig.getInstance().isUseTwitch()) {
            System.out.println("ChatConfig has been initialized, please configure settings");
            return;
        }
        if (ChatConfig.getInstance().isUseTwitch()) {
            setupTwitchChat();
        }
        MessageStuff.getInstance().onMessageEvent();
    }
    
    private static void setupTwitchChat() throws IOException, InterruptedException {
        if (ChatConfig.getInstance().getTwitchChannelName() == null || ChatConfig.getInstance().getTwitchChannelName().isEmpty()) {
            System.out.println("You have enabled twitch chat, but have not designated a channel to read from. " +
                    "\nPlease designate a twitch channel");
            return;
        }
        if (ChatConfig.getInstance().getTwitchOAuthCode() == null || ChatConfig.getInstance().getTwitchOAuthCode().isEmpty()) {
            setupAuth();
        }
        twitchCredential = new OAuth2Credential("twitch", ChatConfig.getInstance().getTwitchOAuthCode());
        setUpListenerBot();
        twitchClient.getChat().joinChannel(ChatConfig.getInstance().getTwitchChannelName());
    }
    
    private static void setupAuth() throws IOException {
        System.out.println("This application does not yet have authorization to interact with twitch");
        System.out.println("Note, if your account does not have the permissions in the channel you are trying to interact with, " +
                "this application will also not have those permissions");
        displayAuthMenu();
        String requestedLevel;
        requestedLevel = scanner.nextLine().toUpperCase();
        while (!authTypes.contains(requestedLevel)) {
            requestedLevel = scanner.nextLine().toUpperCase();
        }
        String authAddressString = TwitchUtil.getLinkMinusScopes();
        if (requestedLevel.equalsIgnoreCase("1")) {
            authAddressString = authAddressString + TwitchUtil.getUserReadOnlyScope();
            //logic
        }
        if (requestedLevel.equalsIgnoreCase("2")) {
            authAddressString = authAddressString + TwitchUtil.getUserReadSendScope();
            //logic
        }
        if (requestedLevel.equalsIgnoreCase("3")) {
            authAddressString = authAddressString + TwitchUtil.getUserModerateScope();
            //logic
        }
        System.out.println("Please visit this link to give this application the authorization to interact with Twitch");
        System.out.println(authAddressString);
        TwitchAuthServer.run();
    }
    
    private static void displayAuthMenu() {
        System.out.println("""
                What level of permission would you like to give this application? Valid responses are:\s
                [1] Read Only\s
                [2] Read and Send
                [3] Moderator Privileges""");
    }
    
    
    private static void setUpConfig() {
        ChatConfig.getInstance().loadConfig();
    }
    
    public static TwitchClient getTwitchClient() {
        return twitchClient;
    }
    
    
}