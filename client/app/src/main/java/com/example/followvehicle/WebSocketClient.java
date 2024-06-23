package com.example.followvehicle;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.followvehicle.api.NotificationHelper;
import com.example.followvehicle.api.StoreUserData;

public class WebSocketClient {
    private static final String TAG = "WebSocketClient";
    private static final String SOCKET_URL = "http://20.2.67.40:1234"; // Thay đổi địa chỉ IP
    private Socket socket;
    private Context context;

    public WebSocketClient(Context context) {
        this.context = context;

        try {
            socket = IO.socket(SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "Socket.IO connection opened");
//                socket.emit("message_from_client", "Hello from Android!");
            }
        }).on("message_from_server", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0) {
                    Object data = args[0];
                    if (data instanceof JSONObject) {
                        JSONObject jsonData = (JSONObject) data;
                        try {
                            String message = jsonData.getString("message");
                            Log.d(TAG, "Message received from server: " + message);
                            NotificationHelper.sendNotification(context, message);
                            StoreUserData.getInstance(context).addNotification(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (data instanceof String) {
                        String message = (String) data;
                        Log.d(TAG, "Message received from server: " + message);
                        NotificationHelper.sendNotification(context, message);
                    } else {
                        Log.d(TAG, "Received unexpected type: " + data.getClass().getName());
                    }
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "Socket.IO connection disconnected");
            }
        });

        socket.connect();
    }

    public void sendMessage(String message) {
        if (socket != null && socket.connected()) {
            socket.emit("message_from_client", message);
            Log.d(TAG, "Message sent: " + message);
        }
    }

    public void disconnectSocket() {
        if (socket != null) {
            socket.disconnect();
            Log.d(TAG, "Socket.IO connection disconnected");
        }
    }
}
