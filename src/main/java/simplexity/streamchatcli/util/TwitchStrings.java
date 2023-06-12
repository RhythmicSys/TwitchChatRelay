package simplexity.streamchatcli.util;

public class TwitchStrings {

    private static TwitchStrings instance;
    private TwitchStrings(){}
    public static TwitchStrings getInstance() {
        if (instance == null) instance = new TwitchStrings();
        return instance;
    }
    private final String twitchApplicationID = "r0ret1izmdz1alwyq98d2lgjyo0h5r";
    private final String userReadOnlyScope = "chat%3Aread";
    private final String userReadSendScope = "chat%3Aread+chat%3Aedit";
    private final String userModerateScope = "chat%3Aread+chat%3Aedit+channel%3Amoderate";
}
