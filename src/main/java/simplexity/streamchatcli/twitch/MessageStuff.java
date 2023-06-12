package simplexity.streamchatcli.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import simplexity.streamchatcli.Main;

public class MessageStuff {
    private static MessageStuff instance;
    private MessageStuff(){}
    public static MessageStuff getInstance() {
        if (instance == null) instance = new MessageStuff();
        return instance;
    }

    private final TwitchClient twitchClient = Main.getTwitchClient();

    public void onMessageEvent() {
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, messageEvent -> {
            String userName = messageEvent.getUser().getName();
            String message = messageEvent.getMessage();
            if (message.isEmpty()) return;
            System.out.println(userName + ": " + message);
        });
    }



}
