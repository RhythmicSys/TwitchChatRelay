package simplexity.streamchatcli.twitch;

public class TwitchUtil {

    public static String getLinkMinusScopes() {
        return  "https://id.twitch.tv/oauth2/authorize?response_type" +
                "=code&client_id=r0ret1izmdz1alwyq98d2lgjyo0h5r&redirect_uri=http://localhost:3000&scope=";
    }

    public static String getUserModerateScope() {
        return "chat%3Aread+chat%3Aedit+channel%3Amoderate";
    }

    public static String getUserReadOnlyScope() {
        return "chat%3Aread";
    }

    public static String getUserReadSendScope() {
        return "chat%3Aread+chat%3Aedit";
    }
}
