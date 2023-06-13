package simplexity.streamchatcli.util;

public class TwitchStrings {

    private static TwitchStrings instance;
    private TwitchStrings(){}
    public static TwitchStrings getInstance() {
        if (instance == null) instance = new TwitchStrings();
        return instance;
    }

    public String getLinkMinusScopes() {
        String twitchApplicationID = "r0ret1izmdz1alwyq98d2lgjyo0h5r";
        String twitchRedirectURI = "http://localhost:3000";
        String twitchAuthAddress = "https://id.twitch.tv/oauth2/authorize";
        return twitchAuthAddress + "?response_type=code&client_id="
                    + twitchApplicationID + "&redirect_uri=" + twitchRedirectURI + "&scope=";
    }

    public String getRedirectURI() {
        return  "http://localhost:3000";
    }
    public String getUserModerateScope() {
        return "chat%3Aread+chat%3Aedit+channel%3Amoderate";
    }

    public String getUserReadOnlyScope() {
        return "chat%3Aread";
    }

    public String getUserReadSendScope() {
        return "chat%3Aread+chat%3Aedit";
    }
}
