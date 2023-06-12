package simplexity.streamchatcli.kick;

import okhttp3.EventListener;
import okhttp3.Response;
import simplexity.streamchatcli.Main;

import java.io.IOException;
import java.net.URL;

public class MessageListener extends EventListener {
    URL kickBaseURL = Main.getKickURL();

    public void responseRecieved(Response response) throws IOException {

    }


}
