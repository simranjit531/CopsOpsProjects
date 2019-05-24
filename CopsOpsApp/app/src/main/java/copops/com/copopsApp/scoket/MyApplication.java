package copops.com.copopsApp.scoket;

import android.app.Application;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyApplication extends Application {

    private io.socket.client.Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://82.165.253.201:8080");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
